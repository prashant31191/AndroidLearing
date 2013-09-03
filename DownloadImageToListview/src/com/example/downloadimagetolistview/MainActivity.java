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
//	private ExecutorService executorService = Executors.newFixedThreadPool(5);

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

		//创建图片缓存的文件夹
		Util.makedir();
		
		downloadSongInfo();
	}

	public List<Map<String, Object>> getdata() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = null;
		for (int i = 0; i < 30; i++) {
			map = new HashMap<String, Object>();
			Drawable drawable = getResources().getDrawable(
					R.drawable.ic_launcher);
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
									String text = Util.getNextText(xrp);
									song.setId(text);
								}
								if (tagName.equals("title")) {
									String text = Util.getNextText(xrp);
									song.setTitle(text);
								}
								if (tagName.equals("artist")) {
									String text = Util.getNextText(xrp);
									song.setArtist(text);
								}
								if (tagName.equals("duration")) {
									String text = Util.getNextText(xrp);
									song.setDuration(text);
								}
								if (tagName.equals("thumb_url")) {
									String text = Util.getNextText(xrp);
									song.setThumb_url(text);
									songlist.add(song);
									sendupdate(count++);
								}
							}
							xrp.next();
						}
						//downloadimage();
						new Thread(connectNet).start();

					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void sendupdate(int pos) {
		// 发送消息，通知handler在主线程中更新listview中的文字
		Message m = new Message();
		m.what = UPDATE_TEXT;
		Bundle bundle = new Bundle();
		bundle.putInt("pos", pos);
		m.setData(bundle);
		connectHanlder.sendMessage(m);
	}

	/*private void downloadimage()
	{
		for (int pos = 0; pos < songlist.size(); pos++) {
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					String path = songlist.get(pos).getThumb_url();
					bitmap = getimagefromurl(path);
					
				}
			});
		}
	}*/
	
	private Runnable connectNet = new Runnable() {
		@Override
		public void run() {
			for (int pos = 0; pos < songlist.size(); pos++) {

				String path = songlist.get(pos).getThumb_url();
				Log.d("wy", "--------------------------"+pos+"--------------------------");
				Bitmap mBitmap = Util.getimagefromurl(path);

				// 发送消息，通知handler在主线程中更新UI
				Message m = new Message();
				m.what = UPDATE_IMAGE;
				Bundle bundle = new Bundle();
				bundle.putInt("pos", pos);
				bundle.putParcelable("bitmap", mBitmap);
				m.setData(bundle);
				connectHanlder.sendMessage(m);
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
				Log.d("wy","message: pos = "+pos+", msg.what = "+msg.what);
				Bitmap mBitmap = msg.getData().getParcelable("bitmap");
				updateListviewImage(pos,mBitmap);
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	private static void updateListviewImage(int pos,Bitmap mBitmap) {		//从xml里解析出图片地址后更新listview中的图片
		Map<String, Object> map = (HashMap<String, Object>) adapter
				.getItem(pos);
		Drawable drawable = new BitmapDrawable(mContext.getResources(), mBitmap);
		map.put("list_image", drawable);
		adapter.notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	private static void updateListview(int pos) {			//解析完xml后更新listview里的文本
		Map<String, Object> map = (HashMap<String, Object>) adapter
				.getItem(pos);
		map.put("title", songlist.get(pos).getTitle());
		map.put("artist", songlist.get(pos).getArtist());
		map.put("duration", songlist.get(pos).getDuration());
		adapter.notifyDataSetChanged();
	}
}
