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

	//sax��һ�������ٶȿ첢��ռ���ڴ��ٵ�xml��������
	//sax��ʽ���ص�����Ҫ�����������ĵ��Ż᷵�أ������һ��xml�ĵ�������ֻ��Ҫǰ��һ�������ݣ�����ʹ��sax��ʽ���ǻ�������ĵ����н���.
	private OnClickListener saxListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			//�½�һ��������factory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			textfromxml = "text from sax:";
			channel saxchannel;

			try {
				//�ù��������һ��sax�Ľ�����paser
				SAXParser paser = factory.newSAXParser();
				//��paser�еõ�һ��XMLReaderʵ��
				XMLReader xmlReader = paser.getXMLReader();
				
				//���Լ�д��handlerע�ᵽXMLReader��
				SAXPraser saxPraser = new SAXPraser();
				xmlReader.setContentHandler(saxPraser);
				
				//����channels.xml�ļ�
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

	//��sax��ͬ���ǣ� pull�������������¼���һ�����֣����Ƿ�������˿���ʹ��һ��switch�Ը���Ȥ���¼����д���
	//pull�����ڳ����п��������������Ϳ���ֹͣ����
	private OnClickListener pullListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			textfromxml = "text from pull:";
			XmlResourceParser xrp = getResources().getXml(R.xml.channels);

			try {
				
				while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
					if (xrp.getEventType() == XmlResourceParser.START_TAG) {
						String tagName = xrp.getName();		//��ȡ��ǩ����
						if (tagName.equals("item")) {
							textfromxml += "\nid="
									+ xrp.getAttributeValue(null, "id"); 	//��ȡ��ǩΪid��ֵ
							textfromxml += "\nurl="
									+ xrp.getAttributeValue(null, "url"); 	//��ȡ��ǩΪurl��ֵ
							textfromxml += "\nname=" + xrp.nextText(); 		//��ȡitem��ֵ
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
	
	//dom����xml�ļ�ʱ���Ὣxml�ļ����������ݶ�ȡ���ڴ��У�Ȼ��ʹ��dom api����xml����������������ݡ�
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
