package com.ym.lookweather.app.util;

/**
 * Created by yangmin on 2015/4/22.
 */
public interface CallbackListener {
    void onFinish(XMLHandler xmlHandler);

    void onError(Exception e);
}
