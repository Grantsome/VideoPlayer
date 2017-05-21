package com.grantsome.videoplayer.Util;

import android.os.Handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tom on 2017/5/21.
 */

public class DownloadUtils {

    public static void sendHttpRequest(final String address,final String param,final HttpUtils.Callback callback){
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
                                callback.onResponse(new HttpUtils.Response(temp));
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

    private static byte[] read(InputStream is) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int len;
        while((len=is.read(temp))!=-1){
            outputStream.write(temp,0,len);
        }
        is.close();
        return outputStream.toByteArray();
    }
}
