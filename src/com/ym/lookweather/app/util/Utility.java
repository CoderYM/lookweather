package com.ym.lookweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.ym.lookweather.app.db.WeatherDB;
import com.ym.lookweather.app.model.City;
import com.ym.lookweather.app.model.County;
import com.ym.lookweather.app.model.Province;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    public synchronized static boolean handleProvincesResponse(WeatherDB weatherDB, XMLHandler response) {
        if (response != null) {
            for (Province province : response.getProvinceList()) {
                weatherDB.saveProvince(province);
            }
            return true;
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
    public static boolean handleCitiesResponse(WeatherDB weatherDB, XMLHandler response, String code,int provinceId) {
        if (response != null) {
            for (City city : response.getCityList()) {
                if (city.getCityCode().substring(0, 2).equals(code)) {
                    city.setProvinceId(provinceId);
                    weatherDB.saveCity(city);
                }
            }
            return true;
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
    public static boolean handleCountiesResponse(WeatherDB weatherDB, XMLHandler response,String code, int cityId) {
        if (response != null) {
            for (County county : response.getCountyList()) {
                if (county.getCountyCode().substring(0, 4).equals(code)) {
                    county.setCityId(cityId);
                    weatherDB.saveCounty(county);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("retData");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("citycode");
            String temp1 = weatherInfo.getString("l_tmp");
            String temp2 = weatherInfo.getString("h_tmp");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("time");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     */
    public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp,
            String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }



}
