package com.grantsome.videoplayer.Util;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tom on 2017/4/27.
 */

public class HttpUtils {

    public static void loadImage(String address, Callback callback){
        if(address.endsWith("/")){
            address = address.substring(0,address.length()-1);
        }
        String name = address.substring(address.lastIndexOf('/')+1);
        File file = new File(ApplicationContext.getContext().getExternalCacheDir(),name);
        if(file.exists()){
            FileInputStream inputStream = null;
            try{
                inputStream = new FileInputStream(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] temp = new byte[1024];
                while (inputStream.read(temp)!=-1){
                    outputStream.write(temp);
                }
                callback.onResponse(new Response(outputStream.toByteArray()));
            }catch (Exception e){
                e.printStackTrace();
                callback.onFail(e);
            }finally {
                if(inputStream!=null){
                    try {
                        inputStream.close();
                    }catch (Exception e){
                        e.printStackTrace();
                        callback.onFail(e);
                    }
                }
            }
        }else {
            sendHttpRequest(address,null,callback);
        }
    }

    public static void sendHttpRequest(String address,String param){
        sendHttpRequest(address, param, new Callback() {
            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onFail(Exception e) {
                ToastUtils.showError(e.toString());
            }
        });
    }

    public static void sendHttpRequest(final String address,final String param,final Callback callback){
        if(!NetworkUtils.isNetWorkConnected(ApplicationContext.getContext())){
            callback.onFail(new Exception("�޷����ӵ�����"));
            return;
        }

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection =null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(10000);
                    if(param==null){
                        connection.setRequestMethod("GET");
                    }else {
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        OutputStream os = connection.getOutputStream();
                        os.write(param.getBytes());
                        os.flush();
                        os.close();
                    }
                    {
                        final byte[] temp = read(connection.getInputStream());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResponse(new Response(temp));
                            }
                        });

                        if (connection.getRequestMethod().equals("GET")) {
                            //��ȡ��address����/�Ĳ���+1
                            String name = address.substring(address.lastIndexOf('/') + 1);
                            //ͨ��Context.getExternalCacheDir()�������Ի�ȡ�� SDCard/Android/data/���Ӧ�ð���/cache/Ŀ¼��һ������ʱ��������
                            File file = new File(ApplicationContext.getContext().getExternalCacheDir(), name);
                            FileOutputStream os = new FileOutputStream(file);
                            os.write(temp);
                            os.close();
                        }
                    }

                }catch (final Exception e){
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(e);
                        }
                    });
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static byte[] read(InputStream is) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int len;
        while((len=is.read(temp))!=-1){
            outputStream.write(temp,0,len);
        }
        is.close();
        return outputStream.toByteArray();
    }

    public interface Callback {

        void onResponse(Response response);

        void onFail(Exception e);
    }

    public static class Response{

        private int mStatus;

        private String mInfo;

        private byte[] mData;

        Response(byte[] response){
            String rawData = new String(response);
            mInfo =getElement(rawData,"showapi_res_body");
            mStatus = 200;
            mData = mInfo.getBytes();
        }

        public int getStatusCode(){
            return mStatus;
        }

        public String getInfo() {
            return mInfo;
        }

        public boolean isSuccess(){
            return mStatus == 200;
        }

        public String message(){
            return "status:" + mStatus + "\ninfo:" + mInfo;
        }

        public String bodyString(){
            return new String(mData);
        }

        public byte[] bodyBytes(){
            return mData;
        }
    }
	
	public static String getElement(String data, String name) {
        try {
            return new JSONObject(data).getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.w("tag", e.toString());
        }
        return null;
    }

    public static int getLength(String address) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(1000 * 600);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int contentLength = connection.getContentLength();
                return contentLength;
            } else {
                ToastUtils.showError("获取文件长度时连接出错");
                return 0;
            }
        } catch (Exception e) {
            //ToastUtils.showError("获取文件长度时失败");
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        return 0;
    }


}