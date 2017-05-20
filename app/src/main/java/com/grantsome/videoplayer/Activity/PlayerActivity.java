/*
 *
 * PlayerActivity.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.grantsome.videoplayer.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.grantsome.videoplayer.Model.ContentBean;
import com.grantsome.videoplayer.Player.MPlayer;
import com.grantsome.videoplayer.Player.MPlayerException;
import com.grantsome.videoplayer.Player.MinimalDisplay;
import com.grantsome.videoplayer.R;
import com.grantsome.videoplayer.Util.ApplicationContext;
import com.grantsome.videoplayer.Util.CircleImage;
import com.grantsome.videoplayer.Util.ImageLoader;

/**
 * Description:
 */
public class PlayerActivity extends Activity {

    private SurfaceView mPlayerView;
    private MPlayer player;
    private ImageButton mPlayButton;
    private CircleImage mAvatar;
    private TextView mAuthorName;
    private TextView mVideoTitle;
    private ContentBean contentBean;
    private TextView mExcitingCount;
    private TextView mNaiveCount;
    private TextView mRecentDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_player);
        Bundle data = getIntent().getBundleExtra("data");
        contentBean =(ContentBean) data.getSerializable("contentBean");
        initView();
        initPlayer();
    }

    private void initView(){
        mPlayerView= (SurfaceView) findViewById(R.id.mPlayerView);
        mPlayButton = (ImageButton) findViewById(R.id.mPlay);
        mAvatar = (CircleImage) findViewById(R.id.avatar);
        mAuthorName = (TextView) findViewById(R.id.authorName);
        mVideoTitle = (TextView) findViewById(R.id.videoTitle);
        mExcitingCount = (TextView) findViewById(R.id.excitingCount);
        mNaiveCount = (TextView) findViewById(R.id.naiveCount);
        mRecentDateText = (TextView) findViewById(R.id.recentDate);
        mAuthorName.setText(contentBean.getName());
        if(contentBean.getProfile_image()==null){
            mAvatar.setImageResource(R.mipmap.default_avatar);
        }else {
            ImageLoader.get(ApplicationContext.getContext(),contentBean.getProfile_image(),mAvatar);
        }
        mVideoTitle.setText(contentBean.getText());
        mExcitingCount.setText(contentBean.getLove());
        mNaiveCount.setText(contentBean.getHate());
        mRecentDateText.setText(contentBean.getCreate_time());
    }

    private void initPlayer(){
        player=new MPlayer();
        player.setDisplay(new MinimalDisplay(mPlayerView));
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.onDestroy();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.mPlay:
                String mUrl = contentBean.getVideo_uri();
                if(mUrl.length()>0){
                    Log.e("wuwang","播放->"+mUrl);
                    try {
                        player.setSource(mUrl);
                        player.play();
                        if(player.isPlaying()) {
                            mPlayButton.setVisibility(View.GONE);
                        }else {
                            mPlayButton.setVisibility(View.VISIBLE);
                        }
                    } catch (MPlayerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.mPlayerView:
                if(player.isPlaying()){
                    player.pause();
                    mPlayButton.setVisibility(View.VISIBLE);
                }else{
                    try {
                        player.play();
                        mPlayButton.setVisibility(View.GONE);
                    } catch (MPlayerException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}
