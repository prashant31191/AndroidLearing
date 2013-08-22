package com.example.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button sax;
	private Button pull;
	private Button dom;
	private Button iSax;
	private Button iPull;
	private Button iDom;
	private static TextView text;
	private static String textfromxml;
	private static InputStream inStream;

	private static final int DONE = 0;

	static List<Channel> list_channel;
	static List<Song> list_song;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sax = (Button) findViewById(R.id.sax);
		pull = (Button) findViewById(R.id.pull);
		dom = (Button) findViewById(R.id.dom);
		iSax = (Button) findViewById(R.id.sax_internet);
		iPull = (Button) findViewById(R.id.pull_internet);
		iDom = (Button) findViewById(R.id.dom_internet);
		text = (TextView) findViewById(R.id.text);

		sax.setOnClickListener(saxListener);
		pull.setOnClickListener(pullListener);
		dom.setOnClickListener(domListener);
		iSax.setOnClickListener(iSaxListener);
		iPull.setOnClickListener(iPullListener);
		iDom.setOnClickListener(iDomListener);
	}

	// sax��һ�������ٶȿ첢��ռ���ڴ��ٵ�xml��������
	// sax��ʽ���ص�����Ҫ�����������ĵ��Ż᷵�أ������һ��xml�ĵ�������ֻ��Ҫǰ��һ�������ݣ�����ʹ��sax��ʽ���ǻ�������ĵ����н���.
	private OnClickListener saxListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			text.setText("");

			// �½�һ��������factory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			textfromxml = "text from sax:";
			Channel saxchannel;

			try {
				// �ù��������һ��sax�Ľ�����paser
				SAXParser paser = factory.newSAXParser();
				// ��paser�еõ�һ��XMLReaderʵ��
				XMLReader xmlReader = paser.getXMLReader();

				// ���Լ�д��handlerע�ᵽXMLReader��
				SaxParser saxParser = new SaxParser();
				xmlReader.setContentHandler(saxParser);

				// ����channels.xml�ļ�
				InputStream stream = getResources().openRawResource(
						R.raw.channels);
				InputSource is = new InputSource(stream);

				xmlReader.parse(is);
				list_channel = saxParser.getList();

				for (int i = 0; i < list_channel.size(); i++) {
					saxchannel = list_channel.get(i);
					textfromxml += "\nid=" + saxchannel.getId();
					textfromxml += "\nurl=" + saxchannel.getUrl();
					textfromxml += "\nname=" + saxchannel.getName();
				}
				text.setText(textfromxml);

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	// ��sax��ͬ���ǣ� pull�������������¼���һ�����֣����Ƿ�������˿���ʹ��һ��switch�Ը���Ȥ���¼����д���
	// pull�����ڳ����п��������������Ϳ���ֹͣ����
	private OnClickListener pullListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			text.setText("");

			textfromxml = "text from pull:";
			XmlResourceParser xrp = getResources().getXml(R.xml.channels);

			try {

				while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
					if (xrp.getEventType() == XmlResourceParser.START_TAG) {
						String tagName = xrp.getName(); // ��ȡ��ǩ����
						if (tagName.equals("item")) {
							textfromxml += "\nid="
									+ xrp.getAttributeValue(null, "id"); // ��ȡ��ǩΪid��ֵ
							textfromxml += "\nurl="
									+ xrp.getAttributeValue(null, "url"); // ��ȡ��ǩΪurl��ֵ
							textfromxml += "\nname=" + xrp.nextText(); // ��ȡitem��ֵ
						}
					}
					xrp.next();
				}
				text.setText(textfromxml);

			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	// dom����xml�ļ�ʱ���Ὣxml�ļ����������ݶ�ȡ���ڴ��У�Ȼ��ʹ��dom api����xml����������������ݡ�
	private OnClickListener domListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			text.setText("");

			textfromxml = "text from dom:";
			InputStream stream = getResources().openRawResource(R.raw.channels);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();

			try {

				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(stream);
				Element root = document.getDocumentElement();
				NodeList items = root.getElementsByTagName("item");

				for (int i = 0; i < items.getLength(); i++) {
					Element item = (Element) items.item(i);
					textfromxml += "\nid=" + item.getAttribute("id");
					textfromxml += "\nurl=" + item.getAttribute("url");
					textfromxml += "\nname="
							+ item.getFirstChild().getNodeValue();
				}
				text.setText(textfromxml);

			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private OnClickListener iSaxListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			text.setText("");
			inStream = null;

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {

						Song song;
						textfromxml = "text from isax:";

						URL url = null;
						url = new URL(
								"http://api.androidhive.info/music/music.xml");
						URLConnection connection = url.openConnection();
						HttpURLConnection httpConnection = (HttpURLConnection) connection;
						int responseCode = httpConnection.getResponseCode();

						if (responseCode == HttpURLConnection.HTTP_OK) {
							inStream = httpConnection.getInputStream();

							SAXParserFactory factory = SAXParserFactory
									.newInstance();

							// �ù��������һ��sax�Ľ�����paser
							SAXParser paser = factory.newSAXParser();
							// ��paser�еõ�һ��XMLReaderʵ��
							XMLReader xmlReader = paser.getXMLReader();

							// ���Լ�д��handlerע�ᵽXMLReader��
							ISaxParser isaxPraser = new ISaxParser();
							xmlReader.setContentHandler(isaxPraser);

							// ����channels.xml�ļ�
							InputSource is = new InputSource(inStream);

							xmlReader.parse(is);
							list_song = isaxPraser.getList();

							for (int i = 0; i < list_song.size(); i++) {
								song = list_song.get(i);
								textfromxml += "\nid=" + song.getId();
								textfromxml += "\ntitle=" + song.getTitle();
								textfromxml += "\nartist=" + song.getArtist();
								textfromxml += "\nduration="
										+ song.getDuration();
								textfromxml += "\nthumb_url="
										+ song.getThumb_url();
							}

							Message m = new Message();
							m.what = DONE;
							connectHanlder.sendMessage(m);
						}

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					} catch (SAXException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	};

	private static Handler connectHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DONE:
				text.setText(textfromxml);
				break;
			}
		}
	};

	private OnClickListener iPullListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			text.setText("");

			inStream = null;

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {

						textfromxml = "text from ipull:";

						URL url = new URL("http://api.androidhive.info/music/music.xml");
						URLConnection connection = url.openConnection();
						HttpURLConnection httpConnection = (HttpURLConnection) connection;
						int responseCode = httpConnection.getResponseCode();

						if (responseCode == HttpURLConnection.HTTP_OK) {
							inStream = httpConnection.getInputStream();
							
							XmlPullParser parser = Xml.newPullParser();  
							parser.setInput(inStream, "UTF-8");

							while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
								if (parser.getEventType() == XmlResourceParser.START_TAG) {
									String tagName = parser.getName();
									if (tagName.equals("id")) {
										textfromxml += "\nid=" + parser.nextText();
									}
									if (tagName.equals("title")) {
										textfromxml += "\ntitle=" + parser.nextText();
									}
									if (tagName.equals("artist")) {
										textfromxml += "\nartist=" + parser.nextText();
									}
									if (tagName.equals("duration")) {
										textfromxml += "\nduration=" + parser.nextText();
									}
									if (tagName.equals("thumb_url")) {
										textfromxml += "\nthumb_url=" + parser.nextText();
									}
								}
								parser.next();
							}

							Message m = new Message();
							m.what = DONE;
							connectHanlder.sendMessage(m);
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
	};

	private OnClickListener iDomListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			text.setText("");

			inStream = null;

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {

						textfromxml = "text from idom:";

						URL url = new URL("http://api.androidhive.info/music/music.xml");
						URLConnection connection = url.openConnection();
						HttpURLConnection httpConnection = (HttpURLConnection) connection;
						int responseCode = httpConnection.getResponseCode();

						if (responseCode == HttpURLConnection.HTTP_OK) {
							inStream = httpConnection.getInputStream();

							DocumentBuilderFactory factory = DocumentBuilderFactory
									.newInstance();
							DocumentBuilder builder = factory.newDocumentBuilder();
							Document document = builder.parse(inStream);
							Element root = document.getDocumentElement();
							NodeList items = root.getElementsByTagName("song");
							
							for (int i = 0; i < items.getLength(); i++) {
								Element item = (Element) items.item(i);
								
								NodeList item2 = item.getChildNodes();
								
								for(int j=0; j < item2.getLength(); j++)
								{
									Node node  = item2.item(j);
									if(node.getNodeName().equals("id"))
									{
										textfromxml += "\nid="+ node.getFirstChild().getNodeValue();
									}
									else if(node.getNodeName().equals("title"))
									{
										textfromxml += "\ntitle="+ node.getFirstChild().getNodeValue();
									}
									else if(node.getNodeName().equals("artist"))
									{
										textfromxml += "\nartist="+ node.getFirstChild().getNodeValue();
									}
									else if(node.getNodeName().equals("duration"))
									{
										textfromxml += "\nduration="+ node.getFirstChild().getNodeValue();
									}
									else if(node.getNodeName().equals("thumb_url"))
									{
										textfromxml += "\nthumb_url="+ node.getFirstChild().getNodeValue();
									}
								}
								
							}
							
							Message m = new Message();
							m.what = DONE;
							connectHanlder.sendMessage(m);
						}

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					} catch (SAXException e) {
						e.printStackTrace();
					}
				}
			}).start();

		}
	};
}
