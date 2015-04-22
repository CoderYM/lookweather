package com.ym.lookweather.app.util;

import com.ym.lookweather.app.model.City;
import com.ym.lookweather.app.model.County;
import com.ym.lookweather.app.model.Province;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangmin on 2015/4/22.
 */
public class XMLHandler extends DefaultHandler {

    private String nodeName;
    private List<Province> provinceList = null;
    private Province province;
    private List<City> cityList = null;
    private City city;
    private List<County> countyList = null;
    private County county;

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public List<County> getCountyList() {
        return countyList;
    }

    @Override
    public void startDocument() throws SAXException {
        provinceList = new ArrayList<Province>();
        cityList = new ArrayList<City>();
        countyList = new ArrayList<County>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        String proviceId = "";
        // 根据当前的结点名判断将内容添加到哪一个List中
        if ("province".equalsIgnoreCase(localName)) {
            province = new Province();
            province.setProvinceName(attributes.getValue("name"));
            province.setProvinceCode(attributes.getValue("id"));
        }
        if ("city".equalsIgnoreCase(localName)) {
            city = new City();
            city.setCityName(attributes.getValue("name"));
            city.setCityCode(attributes.getValue("id"));
        }
        if ("county".equalsIgnoreCase(localName)) {
            county = new County();
            county.setCountyName(attributes.getValue("name"));
            county.setCountyCode(attributes.getValue("id"));
            county.setWeatherCode(attributes.getValue("weatherCode"));
        }
        // 记录当前结点名
        nodeName = localName;
    }

    /**
     * 接收字符数据的通知。该方法用来处理在XML文件中读到的内容，第一个参数用于存放文件的内容，
     * 后面两个参数是读到的字符串在这个数组中的起始位置和长度，使用new String(ch,start,length)就可以获取内容。
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equalsIgnoreCase("province")) {
            provinceList.add(province);
            province = null;
        }else if (localName.equalsIgnoreCase("city")) {
            cityList.add(city);
            city = null;
        }else if (localName.equalsIgnoreCase("county")) {
            countyList.add(county);
            county = null;
        }
    }

    @Override
    public void endDocument() throws SAXException {
    }
}
