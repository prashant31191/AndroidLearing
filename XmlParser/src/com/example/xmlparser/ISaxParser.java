package com.example.xmlparser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ISaxParser extends DefaultHandler {

	int status;

	List<Song> list;
	Song song;

	public List<Song> getList() {
		return list;
	}

	// 文档开始通知
	@Override
	public void startDocument() throws SAXException {
		list = new ArrayList<Song>();
	}

	// 接收文档结束通知
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	// 接口字符块通知
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String theString = String.valueOf(ch, start, length);
		switch (status) {
		case 1:
			song.setId(theString);
			status=0;
			break;
		case 2:
			song.setTitle(theString);
			status=0;
			break;
		case 3:
			song.setArtist(theString);
			status=0;
			break;
		case 4:
			song.setDuration(theString);
			status=0;
			break;
		case 5:
			song.setThumb_url(theString);
			status=0;
			break;
		}
		return;
	}

	// 标签开始通知
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals("song")) {
			song = new Song();
			status = 0;
		}
		else if (localName.equals("id")) {
			status = 1;
		}
		else if (localName.equals("title")) {
			status = 2;
		}
		else if (localName.equals("artist")) {
			status = 3;
		}
		else if (localName.equals("duration")) {
			status = 4;
		}
		else if (localName.equals("thumb_url")) {
			status = 5;
		}
		else {
			status = 0;
		}
		return;
	}

	// 接收标签结束通知
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("song")) {
			status = 0;
			list.add(song);
		}
	}
}