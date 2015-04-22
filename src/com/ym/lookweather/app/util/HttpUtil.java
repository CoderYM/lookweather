package com.ym.lookweather.app.util;

import android.content.res.Resources;
import org.apache.http.util.EncodingUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yangmin on 2015/4/17.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void getLocalDataFile(final Resources resources, final CallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = "";
                try {
                    // 得到资源中的asset数据流
                    InputStream in = resources.getAssets().open("provice_city_country.xml");
                    int length = in.available();
                    byte[] buffer = new byte[length];
                    in.read(buffer);
                    in.close();
                    res = EncodingUtils.getString(buffer, "UTF-8");
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    XMLReader xmlReader = factory.newSAXParser().getXMLReader();
                    XMLHandler handler = new XMLHandler();
                    // 将ContentHandler的实例设置到XMLReader中
                    xmlReader.setContentHandler(handler);
                    // 开始执行解析
                    xmlReader.parse(new InputSource(new StringReader(res)));
                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(handler);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }

                }
            }
        }).start();
    }

}
