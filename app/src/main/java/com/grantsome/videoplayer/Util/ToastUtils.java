package com.grantsome.videoplayer.Util;

import android.widget.Toast;

/**
 * Created by tom on 2017/4/27.
 * Toast������
 */

public class ToastUtils {

    public static void showError(String error){
        Toast.makeText(ApplicationContext.getContext(), error, Toast.LENGTH_SHORT).show();
    }

    public static void showHint(String result){
        Toast.makeText(ApplicationContext.getContext(),result,Toast.LENGTH_SHORT).show();
    }
}