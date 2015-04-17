package com.ym.lookweather.app.util;

/**
 * Created by yangmin on 2015/4/17.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
