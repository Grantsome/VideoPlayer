package com.grantsome.videoplayer.Util;

import android.app.Application;
import android.content.Context;

/**
 * Created by tom on 2017/4/27.
 * context��ȡ����
 */

public class ApplicationContext extends Application {

    private static Context mContext;

    @Override
    public void onCreate(){
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        if(mContext==null){
            return null;
        }
        return mContext;
    }
}