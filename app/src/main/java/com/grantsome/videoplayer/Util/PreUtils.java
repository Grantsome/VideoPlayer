package com.grantsome.videoplayer.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Grantsome on 2017/2/15.
 * �������洢������(SharedPreferences)
 */

public class PreUtils {

    public static void putStringToDefault(Context context,String key,String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(key,value).commit();
    }

    public static String getStringFromDefault(Context context,String key,String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key,value);
    }

}