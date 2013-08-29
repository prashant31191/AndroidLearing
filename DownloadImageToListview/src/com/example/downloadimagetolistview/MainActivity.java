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
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {

	private ListView listview;
	private InputStream inStream;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listview = (ListView) findViewById(R.id.listview);

		String[] from = new String[] { "list_image", "title", "artist",
				"duration" };
		int[] to = new int[] { R.id.list_image, R.id.title, R.id.artist,
				R.id.duration };

		SimpleAdapter adapter = new SimpleAdapter(this, getdata(),
				R.layout.listitem, from, to);
		listview.setAdapter(adapter);

		test();
	}

	public List<Map<String, Object>> getdata() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = null;
		for (int i = 0; i < 50; i++) {
			map = new HashMap<String, Object>();
			map.put("list_image", R.drawable.ic_launcher);
			map.put("title", "Rihanna Love the way lie");
			map.put("artist", "Just gona stand there and ...");
			map.put("duration", "5:45");
			list.add(map);
		}

		return list;
	}

	public void test()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					
					XmlPullParser xrp = Xml.newPullParser();
					Song song = null;
					inStream = null;

					URL url = new URL("http://api.androidhive.info/music/music.xml");
					URLConnection connection = url.openConnection();
					HttpURLConnection httpConnection = (HttpURLConnection) connection;
					int responseCode = httpConnection.getResponseCode();
					Log.d("wy","responseCode="+responseCode);
					if (responseCode == HttpURLConnection.HTTP_OK) {
						inStream = httpConnection.getInputStream();

						xrp.setInput(inStream, "UTF-8");
						while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
							if (xrp.getEventType() == XmlResourceParser.START_TAG) {
								
								String tagName = xrp.getName();		//获取标签名字
								
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
								}
							}
							xrp.next();
						}
						
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private String getNextText(XmlPullParser parser) throws XmlPullParserException, IOException{
	    String result = parser.nextText();
	    if (parser.getEventType() != XmlPullParser.END_TAG) {
	        parser.nextTag();
	    }
	    return result;
	}
}
