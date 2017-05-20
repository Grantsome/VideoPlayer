/*
 *
 * PlayerActivity.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.grantsome.videoplayer.Player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.grantsome.videoplayer.Activity.PlayerActivity;
import com.grantsome.videoplayer.Model.ContentBean;
import com.grantsome.videoplayer.R;
import com.grantsome.videoplayer.Util.ApplicationContext;
import com.grantsome.videoplayer.Util.CircleImage;
import com.grantsome.videoplayer.Util.ImageLoader;

import java.util.ArrayList;

/**
 * Description:
 */
public class PlayerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private SurfaceView mPlayerView;

    private MPlayer player;

    private ImageButton mPlayButton;

    private CircleImage mAvatar;

    private TextView mAuthorName;

    private TextView mVideoTitle;

    private TextView mExcitingCount;

    private TextView mNaiveCount;

    private ArrayList<ContentBean> mContentList;

    private TextView mRecentDateText;

    public PlayerHolder(View itemView,ArrayList<ContentBean> contentBeanList) {
        super(itemView);
        mContentList = contentBeanList;
        initView(itemView);
        initPlayer();
    }

    private void initView(View itemView){
        mPlayerView= (SurfaceView) itemView.findViewById(R.id.mPlayerView);
        mPlayButton = (ImageButton) itemView.findViewById(R.id.mPlay);
        mAvatar = (CircleImage) itemView.findViewById(R.id.avatar);
        mAuthorName = (TextView) itemView.findViewById(R.id.authorName);
        mVideoTitle = (TextView) itemView.findViewById(R.id.videoTitle);
        mExcitingCount = (TextView) itemView.findViewById(R.id.excitingCount);
        mNaiveCount = (TextView) itemView.findViewById(R.id.naiveCount);
        mRecentDateText = (TextView) itemView.findViewById(R.id.recentDate);
        setItemViewOnClickListener(itemView);
    }

    public void updata(ContentBean contentBean){
        mAuthorName.setText(contentBean.getName());
        if(contentBean.getProfile_image()==null){
            mAvatar.setImageResource(R.mipmap.default_avatar);
        }else {
            ImageLoader.get(ApplicationContext.getContext(),mContentList.get(getLayoutPosition()).getProfile_image(),mAvatar);
        }
        mVideoTitle.setText(contentBean.getText());
        Log.d("tag",contentBean.getText());
        String mUrl=mContentList.get(getLayoutPosition()).getVideo_uri();
        if(mUrl.length()>0){
            Log.e("wuwang","播放->"+mUrl);
            try {
                player.setSource(mUrl);
                player.play();
            } catch (MPlayerException e) {
                e.printStackTrace();
            }
        }
        mExcitingCount.setText(contentBean.getLove());
        mNaiveCount.setText(contentBean.getHate());
        mRecentDateText.setText(contentBean.getCreate_time());
    }

    private void setItemViewOnClickListener(View itemView){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("contentBean",mContentList.get(getLayoutPosition()));
                intent.putExtra("data",data);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void addOnClickListener(){
        mPlayerView.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
    }

    private void initPlayer(){
        player=new MPlayer();
        player.setDisplay(new MinimalDisplay(mPlayerView));
    }


    public void onResume() {
        player.onResume();
    }


    public void onPause() {
        player.onPause();
    }


    public void onDestroy() {
        player.onDestroy();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.mPlayerView:
            case R.id.mPlay:
                Intent intent = new Intent(view.getContext(), PlayerActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("contentBean",mContentList.get(getLayoutPosition()));
                intent.putExtra("data",data);
                view.getContext().startActivity(intent);
                break;

        }
    }



}
