package com.grantsome.videoplayer.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tom on 2017/3/21.
 */

public class ImageLoader implements Serializable {

    private String url;

    private CircleImage imageView;

    private static ImageLoader sImageLoader;

    private static Context sContext;

    public static ImageLoader get(Context context, String mUri,CircleImage mImageView){
        Log.d("ImageLoader","geHttpBitmapִ����ImageLoader��ʼ��");
        sImageLoader = new ImageLoader(mUri,mImageView);
        sContext = context;
        return sImageLoader;
    }

    private ImageLoader(String mUri,CircleImage mIageView){
        this.url = mUri;
        imageView = mIageView;
        try{
            getHttpBitmap(url);
            Log.d("ImageLoader","geHttpBitmapִ����ImageLoader����");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 666:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    imageView.setImageBitmap(bitmap);
                    break;
                case 555:
                    Bitmap mBitmap = (Bitmap) msg.obj;
                    imageView.setImageBitmap(mBitmap);
                    break;
                case 444:
                    //Toast.makeText(sContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void getHttpBitmap(final String mUri){

        new Thread(new Runnable() {
            URL mURL = null;
            @Override
            public void run() {
                try{

                    if(!isSaveFile()){
                        mURL = new URL(mUri);
                        loadFileFromHttp(mURL);
                    }else if(isSaveFile()){
                        getBitmapFile(mUri);
                        Log.d("ImageLoader","�Ѿ�ִ��getBitmap����");
                    }

                }catch (Exception e){
                    handler.sendMessage(handler.obtainMessage(444,"��������ʧ��"));
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadFileFromHttp(URL mURL){
        Bitmap mBitmap = null;
        try {
              HttpURLConnection mHttpConnection = (HttpURLConnection) mURL.openConnection();
              mHttpConnection.setConnectTimeout(5000);
              mHttpConnection.setUseCaches(true);
              mHttpConnection.setDoInput(true);
              InputStream mInputStream = mHttpConnection.getInputStream();
              final BitmapFactory.Options options = new BitmapFactory.Options();
              mBitmap = BitmapFactory.decodeStream(mInputStream,null,options);
              handler.sendMessage(handler.obtainMessage(666,mBitmap));
              saveFile(mBitmap,mURL.toString());

              Log.d("ImageLoader","�Ѿ�ִ��saveBitmap����");
              if(mInputStream!=null) {
                  mInputStream.close();
              }
        } catch (Exception e){
              e.printStackTrace();
        }
    }

    private boolean isSaveFile() throws Exception{
        File myCaptureFile = new File(sContext.getExternalCacheDir() + url);
        if(myCaptureFile.exists()){
            return true;
        }else {
            return false;
        }
    }

    protected void saveFile(Bitmap bm, String fileName) throws IOException {
        File myCaptureFile = new File(sContext.getExternalCacheDir() + fileName);
        myCaptureFile.getParentFile().mkdirs();
        myCaptureFile.createNewFile();
        Log.d("ImageLoader","save�����path"+myCaptureFile.getPath());
        FileOutputStream out;
        out = new FileOutputStream(myCaptureFile);
        bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
        out.close();
    }

    public void getBitmapFile(String uri) throws Exception{
        File dirFile = new File(sContext.getExternalCacheDir()+uri);
        if(dirFile.exists()){
            final BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap mBitmap = BitmapFactory.decodeFile(dirFile.toString(),options);
            Log.d("ImageLoader","get�����path"+dirFile.toString());
            handler.sendMessage(handler.obtainMessage(555,mBitmap));
        }
        if(!dirFile.exists()){
            Log.d("ImageLoader","get�����path������");
        }
    }

}