package com.grantsome.videoplayer.Util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tom on 2017/5/20.
 */

public class ApiString {

    //https://route.showapi.com/255-1?page=1&showapi_appid=38523&showapi_test_draft=false&showapi_timestamp=20170520074443&title=&type=41&showapi_sign=7e7fe114fa88e2f8a9066973c4831097;

    public static final String PRE="https://route.showapi.com/255-1";

    public static final String PAGE="?page=";

    public static final String ID="&showapi_appid=38523";//无需修改

    public static final String TEST="&showapi_test_draft=false";//无需修改

    public static final String TIMESTAMP="&showapi_timestamp=";

    public static final String TITLE="&title=";//无需修改

    public static final String TYPE="&type=41";//无需修改

    public static final String SIGN="&showapi_sign=4e716dcd6c394a3bb5272306670d3fe6";//无需修改

    public static String getTimestamp(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = dateFormat.format(now.getTime());
        String[] oldTime = time.split("-");
        time=oldTime[0]+oldTime[1]+oldTime[2]+oldTime[3]+oldTime[4]+oldTime[5];
        return time;
    }

    public static String getParams(){
        String uri = PRE+PAGE+"1"+ID+TEST+TIMESTAMP+getTimestamp()+TITLE+TYPE+SIGN;
        Log.d("tag",uri);
        //String uri = "https://route.showapi.com/255-1?page=1&showapi_appid=38523&showapi_test_draft=false&showapi_timestamp=20170520145256&title=&type=41&showapi_sign=d7051c840b2d167d099077f8d843ba2a";
        return uri;
    }


}
