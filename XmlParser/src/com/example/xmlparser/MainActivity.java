package com.example.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button sax;
	private Button pull;
	private Button dom;
	private TextView text;
	private String textfromxml;

	List<channel>list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sax = (Button) findViewById(R.id.sax);
		pull = (Button) findViewById(R.id.pull);
		dom = (Button) findViewById(R.id.dom);
		text = (TextView) findViewById(R.id.text);

		sax.setOnClickListener(saxListener);
		pull.setOnClickListener(pullListener);
		dom.setOnClickListener(domListener);
	}

	//sax是一个解析速度快并且占用内存少的xml解析器。
	//sax方式的特点是需要解析完整个文档才会返回，如果在一个xml文档中我们只需要前面一部分数据，但是使用sax方式还是会对整个文档进行解析.
	private OnClickListener saxListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			//新建一个工厂类factory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			textfromxml = "text from sax:";
			channel saxchannel;

			try {
				//让工厂类产生一个sax的解析类paser
				SAXParser paser = factory.newSAXParser();
				//从paser中得到一个XMLReader实例
				XMLReader xmlReader = paser.getXMLReader();
				
				//把自己写的handler注册到XMLReader中
				SAXPraser saxPraser = new SAXPraser();
				xmlReader.setContentHandler(saxPraser);
				
				//读入channels.xml文件
				InputStream stream = getResources().openRawResource(
						R.raw.channels);
				InputSource is = new InputSource(stream);
				
				xmlReader.parse(is);
				list = saxPraser.getList();

				for (int i = 0; i < list.size(); i++) {
					saxchannel = list.get(i);
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

	//跟sax不同的是， pull解析器产生的事件是一个数字，而非方法，因此可以使用一个switch对感兴趣的事件进行处理。
	//pull可以在程序中控制想解析到哪里就可以停止解析
	private OnClickListener pullListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			textfromxml = "text from pull:";
			XmlResourceParser xrp = getResources().getXml(R.xml.channels);

			try {
				
				while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
					if (xrp.getEventType() == XmlResourceParser.START_TAG) {
						String tagName = xrp.getName();		//获取标签名字
						if (tagName.equals("item")) {
							textfromxml += "\nid="
									+ xrp.getAttributeValue(null, "id"); 	//获取标签为id的值
							textfromxml += "\nurl="
									+ xrp.getAttributeValue(null, "url"); 	//获取标签为url的值
							textfromxml += "\nname=" + xrp.nextText(); 		//获取item的值
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
	
	//dom解析xml文件时，会将xml文件的所有内容读取到内存中，然后使用dom api遍历xml树、检索所需的数据。
	private OnClickListener domListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			textfromxml = "text from dom:";
			InputStream stream = getResources().openRawResource(R.raw.channels);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			try {
				
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(stream);
				Element root = document.getDocumentElement();
				NodeList items = root.getElementsByTagName("item");
				
				for (int i=0;i<items.getLength();i++)
				{
					Element item = (Element)items.item(i);
					textfromxml += "\nid="
							+ item.getAttribute("id");
					textfromxml += "\nurl="
							+ item.getAttribute("url");
					textfromxml += "\nname=" + item.getFirstChild().getNodeValue();
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
}
