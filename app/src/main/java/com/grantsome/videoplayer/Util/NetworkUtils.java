package com.grantsome.videoplayer.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tom on 2017/4/27.
 * network��鹤��
 */

public class NetworkUtils {

    public static boolean isNetWorkConnected(Context context){
        if(context!=null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if(networkInfo!=null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
}