package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Area;
public class AreaListHandler extends DefaultHandler {
	private List<Area> areaList = null;
	private Area area;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (area != null) { 
            String valueString = new String(ch, start, length); 
            if ("areaId".equals(tempString)) 
            	area.setAreaId(new Integer(valueString).intValue());
            else if ("areaName".equals(tempString)) 
            	area.setAreaName(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Area".equals(localName)&&area!=null){
			areaList.add(area);
			area = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		areaList = new ArrayList<Area>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Area".equals(localName)) {
            area = new Area(); 
        }
        tempString = localName; 
	}

	public List<Area> getAreaList() {
		return this.areaList;
	}
}
