package com.example.downloadimagetolistview;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ListView listview;
	private InputStream inStream;
	private static List<Song> songlist;
	public static List<Map<String, Object>> data;
	private static MyAdapter adapter;
	private static final int UPDATE_TEXT = 1;
	private static final int UPDATE_IMAGE = 2;
	private static Context mContext;
	private static Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listview = (ListView) findViewById(R.id.listview);

		mContext = getApplicationContext();

		data = getdata();
		adapter = new MyAdapter(mContext);
		listview.setAdapter(adapter);

		songlist = new ArrayList<Song>();

		downloadSongInfo();
	}

	public List<Map<String, Object>> getdata() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = null;
		for (int i = 0; i < 30; i++) {
			map = new HashMap<String, Object>();
			Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
			map.put("list_image", drawable);
			map.put("title", "Rihanna Love the way lie");
			map.put("artist", "Just gona stand there and ...");
			map.put("duration", "5:45");
			list.add(map);
		}

		return list;
	}

	public void downloadSongInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					XmlPullParser xrp = Xml.newPullParser();
					Song song = null;
					inStream = null;
					int count = 0;

					URL url = new URL(
							"http://api.androidhive.info/music/music.xml");
					URLConnection connection = url.openConnection();
					HttpURLConnection httpConnection = (HttpURLConnection) connection;
					int responseCode = httpConnection.getResponseCode();
					Log.d("wy", "responseCode=" + responseCode);
					if (responseCode == HttpURLConnection.HTTP_OK) {
						inStream = httpConnection.getInputStream();

						xrp.setInput(inStream, "UTF-8");
						while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
							if (xrp.getEventType() == XmlResourceParser.START_TAG) {

								String tagName = xrp.getName(); // 获取标签名字

								if (tagName.equals("song")) {
									song = new Song();
								}
								if (tagName.equals("id")) {
									String text = getNextText(xrp);
									song.setId(text);
								}
								if (tagName.equals("title")) {
									String text = getNextText(xrp);
									song.setTitle(text);
								}
								if (tagName.equals("artist")) {
									String text = getNextText(xrp);
									song.setArtist(text);
								}
								if (tagName.equals("duration")) {
									String text = getNextText(xrp);
									song.setDuration(text);
								}
								if (tagName.equals("thumb_url")) {
									String text = getNextText(xrp);
									song.setThumb_url(text);
									songlist.add(song);
									sendupdate(count++);
								}
							}
							xrp.next();
						}
						new Thread(connectNet).start();

					}

				} catch (MalformedURLException e) {
					Log.d("wy", "e=" + e.toString());
					e.printStackTrace();
				} catch (IOException e) {
					Log.d("wy", "e=" + e.toString());
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					Log.d("wy", "e=" + e.toString());
					e.printStackTrace();
				} catch (Exception e) {
					Log.d("wy", "e=" + e.toString());
					e.printStackTrace();
				}
			}
		}).start();
	}

	public InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000); // 设置连接的时限为5秒
		conn.setRequestMethod("GET");

		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	private void sendupdate(int pos) {

		// 发送消息，通知handler在主线程中更新UI
		Message m = new Message();
		m.what = UPDATE_TEXT;
		Bundle bundle = new Bundle();
		bundle.putInt("pos", pos);
		m.setData(bundle);
		connectHanlder.sendMessage(m);
	}

	private Runnable connectNet = new Runnable() {
		@Override
		public void run() {
			try {
				for (int pos = 0; pos < songlist.size(); pos++) {

					String url = songlist.get(pos).getThumb_url();
					bitmap = BitmapFactory.decodeStream(getImageStream(url));

					// 发送消息，通知handler在主线程中更新UI
					Message m = new Message();
					m.what = UPDATE_IMAGE;
					Bundle bundle = new Bundle();
					bundle.putInt("pos", pos);
					m.setData(bundle);
					connectHanlder.sendMessage(m);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private static Handler connectHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int pos = msg.getData().getInt("pos");
			switch (msg.what) {
			case UPDATE_TEXT:
				updateListview(pos);
				break;
			case UPDATE_IMAGE:
				updatelistviewimage(pos);
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	private static void updatelistviewimage(int pos) {

		Log.d("wy","updatelistviewimage "+pos);
		Map<String, Object> map = (HashMap<String, Object>) adapter
				.getItem(pos);
		Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
		//map.remove("list_image");
		map.put("list_image", drawable);
		adapter.notifyDataSetChanged();
	}

	private String getNextText(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		String result = parser.nextText();
		if (parser.getEventType() != XmlPullParser.END_TAG) {
			parser.nextTag();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static void updateListview(int pos) {
		Map<String, Object> map = (HashMap<String, Object>) adapter
				.getItem(pos);
		map.put("title", songlist.get(pos).getTitle());
		map.put("artist", songlist.get(pos).getArtist());
		map.put("duration", songlist.get(pos).getDuration());
		adapter.notifyDataSetChanged();
	}
}
