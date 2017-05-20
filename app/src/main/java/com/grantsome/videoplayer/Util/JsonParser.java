package com.grantsome.videoplayer.Util;



import android.util.Log;

import com.grantsome.videoplayer.Model.ContentBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tom on 2017/5/20.
 */

public class JsonParser {

   public static ArrayList<ContentBean> getContentList(String data){
       ArrayList<ContentBean>  contentList = new ArrayList<>();
       try{
           JSONObject resbody = new JSONObject(data);
           JSONObject pagebean = resbody.getJSONObject("pagebean");
           JSONArray contentlist = pagebean.getJSONArray("contentlist");
           for(int i=0;i<contentlist.length();i++){
               ContentBean contentBean = new ContentBean();
               JSONObject js = contentlist.getJSONObject(i);
               contentBean.setText(js.getString("text"));
               contentBean.setHate(js.getString("hate"));
               contentBean.setVideotime(js.getString("videotime"));
               contentBean.setVoicetime(js.getString("voicetime"));
               contentBean.setProfile_image(js.getString("profile_image"));
               contentBean.setWeixin_url(js.getString("weixin_url"));
               contentBean.setWidth(js.getString("width"));
               contentBean.setVoiceuri(js.getString("voiceuri"));
               contentBean.setType(js.getString("type"));
               contentBean.setId(js.getString("id"));
               contentBean.setLove(js.getString("love"));
               contentBean.setHeight(js.getString("height"));
               contentBean.setVideo_uri(js.getString("video_uri"));
               Log.d("JsonParser",contentBean.getVideo_uri());
               contentBean.setVoicelength(js.getString("voicelength"));
               contentBean.setName(js.getString("name"));
               contentBean.setCreate_time(js.getString("create_time"));
               contentList.add(contentBean);
           }
       }catch (Exception e){
           e.printStackTrace();
           ToastUtils.showError("ContentBean解析错误");
       }
       return contentList;
   }

}

