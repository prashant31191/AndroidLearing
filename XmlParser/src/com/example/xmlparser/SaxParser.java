package com.example.xmlparser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParser extends DefaultHandler {

    final int ITEM = 0x0005;

    List<Channel> list;
    Channel chann;
    int currentState = 0;

    public List<Channel> getList() {
        return list;
    }

    
    // �ӿ��ַ���֪ͨ
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    	// super.characters(ch, start, length);
        String theString = String.valueOf(ch, start, length);
        if (currentState != 0) {
            chann.setName(theString);
            currentState = 0;
        }
        return;
    }

    //�ĵ���ʼ֪ͨ
    @Override
    public void startDocument() throws SAXException {
        list = new ArrayList<Channel>();
    }
    
    //�����ĵ�����֪ͨ
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    //��ǩ��ʼ֪ͨ
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        chann = new Channel();
        if (localName.equals("item")) {
            for (int i = 0; i < attributes.getLength(); i++) {
            	//��ȡXML�е��������ԣ�����0����ţ������һ������
                if (attributes.getLocalName(i).equals("id")) {
                    chann.setId(attributes.getValue(i));
                } else if (attributes.getLocalName(i).equals("url")) {
                    chann.setUrl(attributes.getValue(i));
                }
            }
            currentState = ITEM;
            return;
        }
        currentState = 0;
        return;
    }
    
    //���ձ�ǩ����֪ͨ
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (localName.equals("item"))
            list.add(chann);
    }


}