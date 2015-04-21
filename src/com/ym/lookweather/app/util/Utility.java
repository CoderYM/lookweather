package com.ym.lookweather.app.util;

import android.text.TextUtils;
import com.ym.lookweather.app.db.WeatherDB;
import com.ym.lookweather.app.model.City;
import com.ym.lookweather.app.model.County;
import com.ym.lookweather.app.model.Province;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangmin on 2015/4/21.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(WeatherDB weatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String responseJson = response.substring(3, response.length() - 1);
            if (!TextUtils.isEmpty(responseJson)) {
                try {
                    JSONObject jsonObject = new JSONObject(responseJson);
                    JSONObject proviceList = jsonObject.getJSONObject("list");
                    int listSize = Integer.parseInt(jsonObject.getString("zidi"));
                    for (int i = 0; i < listSize; i++) {
                        Province province = new Province();
                        JSONObject subObj = proviceList.getJSONObject("wjr" + (i + 1));
                        province.setProvinceName(subObj.getString("diming"));
                        province.setProvinceCode(subObj.getString("daima"));
                        // 将解析出来的数据存储到Province表
                        weatherDB.saveProvince(province);
                    }
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(WeatherDB weatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String responseJson = response.substring(3, response.length() - 1);
            if (!TextUtils.isEmpty(responseJson)) {
                try {
                    JSONObject jsonObject = new JSONObject(responseJson);
                    JSONObject cityList = jsonObject.getJSONObject("list");
                    int listSize = Integer.parseInt(jsonObject.getString("zidi"));
                    for (int i = 0; i < listSize; i++) {
                        City city = new City();
                        JSONObject subObj = cityList.getJSONObject("wjr" + (i + 1));
                        city.setCityName(subObj.getString("diming"));
                        city.setCityCode(subObj.getString("daima"));
                        city.setProvinceId(provinceId);
                        // 将解析出来的数据存储到Province表
                        weatherDB.saveCity(city);
                    }
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountiesResponse(WeatherDB weatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String responseJson = response.substring(3, response.length() - 1);
            if (!TextUtils.isEmpty(responseJson)) {
                try {
                    JSONObject jsonObject = new JSONObject(responseJson);
                    JSONObject countryList = jsonObject.getJSONObject("list");
                    int listSize = Integer.parseInt(jsonObject.getString("zidi"));
                    for (int i = 0; i < listSize; i++) {
                        County county = new County();
                        JSONObject subObj = countryList.getJSONObject("wjr" + (i + 1));
                        county.setCountyName(subObj.getString("diming"));
                        county.setCountyCode(subObj.getString("daima"));
                        county.setCityId(cityId);
                        // 将解析出来的数据存储到Province表
                        weatherDB.saveCounty(county);
                    }
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
