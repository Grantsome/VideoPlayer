/*
 *
 * PlayerActivity.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.grantsome.videoplayer.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.grantsome.videoplayer.Model.ContentBean;
import com.grantsome.videoplayer.Player.MPlayer;
import com.grantsome.videoplayer.Player.MPlayerException;
import com.grantsome.videoplayer.Player.MinimalDisplay;
import com.grantsome.videoplayer.R;
import com.grantsome.videoplayer.Util.ApplicationContext;
import com.grantsome.videoplayer.Util.ChangeUtils;
import com.grantsome.videoplayer.Util.CircleImage;
import com.grantsome.videoplayer.Util.ImageLoader;
import com.grantsome.videoplayer.Util.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.grantsome.videoplayer.Player.MPlayer.currentPosition;
import static com.grantsome.videoplayer.Player.MPlayer.duration;

/**
 * Description:
 */
public class PlayerActivity extends AppCompatActivity implements View.OnClickListener{

    private SurfaceView mPlayerView;
    private MPlayer player;
    private ImageButton mPlayButton;
    private CircleImage mAvatar;
    private TextView mAuthorName;
    private TextView mVideoTitle;
    private ContentBean contentBean;
    private List<ContentBean> contentlist;
    private TextView mExcitingCount;
    private TextView mNaiveCount;
    private TextView mRecentDateText;
    private SeekBar seekBar_progress;
    private Timer timer;
    private TimerTask timerTask;
    private Boolean ischanging = false;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private ImageButton downloadButton;
    private int position;
    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
           IBinder temp = service;
           downloadBinder = (DownloadService.DownloadBinder) temp;
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_player);
        Bundle data = getIntent().getBundleExtra("data");
        contentlist =(ArrayList<ContentBean>) data.getSerializable("contentBeanList");
        contentBean = (ContentBean) data.getSerializable("contentBean");
        initView();
        initPlayer();
        query();
        bind();
    }

    private void bind(){
        Intent intent = new Intent(PlayerActivity.this, DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE); // 绑定服务
    }

    private void query(){
        for(int i=0;i<contentlist.size();i++){
            if(contentlist.get(i)==contentBean){
                position = i;
            }
        }
    }

    private void initView(){
        mPlayerView= (SurfaceView) findViewById(R.id.mPlayerView);
        mPlayButton = (ImageButton) findViewById(R.id.media_controller_play_pause);
        mAvatar = (CircleImage) findViewById(R.id.avatar);
        mAuthorName = (TextView) findViewById(R.id.authorName);
        mVideoTitle = (TextView) findViewById(R.id.videoTitle);
        mExcitingCount = (TextView) findViewById(R.id.excitingCount);
        mNaiveCount = (TextView) findViewById(R.id.naiveCount);
        mRecentDateText = (TextView) findViewById(R.id.recentDate);
        seekBar_progress = (SeekBar) findViewById(R.id.media_controller_seekbar);
        seekBar_progress.setOnSeekBarChangeListener(new SeekListener());
        previousButton = (ImageButton) findViewById(R.id.media_controller_previous);
        nextButton = (ImageButton) findViewById(R.id.media_controller_next);
        downloadButton = (ImageButton) findViewById(R.id.media_controller_download);
        initData();
    }

    private void initData(){
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
        seekBarChanging();
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
        unbindService(connection);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.media_controller_play_pause:
                playVedio();
                break;
            case R.id.media_controller_next:
                ChangeUtils.setNext(true);
                if(position!=contentlist.size()-1) {
                    contentBean =contentlist.get(position+1);
                }else {
                    ToastUtils.showHint("已经到了最后一个,没有下一个视频了");
                }
                initData();
                playVedio();
                query();
                break;
            case R.id.media_controller_previous:
                ChangeUtils.setPrevious(true);
                if(position!=0){
                    contentBean = contentlist.get(position-1);
                }else {
                    ToastUtils.showHint("已经是第一个了，没有上一个视频了");
                }
                initData();
                playVedio();
                query();
                break;
            case R.id.media_controller_download:
                query();
                if (downloadBinder == null) {
                    Log.w("download","返回");
                   return;
                }
                Log.w("download","开始执行");
                ToastUtils.showHint("正在准备下载");
                downloadBinder.startDownload(contentlist.get(position).getVideo_uri());
        }
    }

    private void playVedio(){
        String mUrl = contentBean.getVideo_uri();
        if(mUrl.length()>0){
            Log.e("wuwang","播放->"+mUrl);
            ToastUtils.showHint("很用力的加载中");
            try {
                player.setSource(mUrl);
                if(player.isPlaying()) {
                    player.pause();
                    setInvisable();
                }else {
                    player.play();
                    setVisable();
                }
            } catch (MPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    private void setInvisable(){
        mPlayButton.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        downloadButton.setVisibility(View.INVISIBLE);
    }

    private void setVisable(){
        mPlayButton.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        downloadButton.setVisibility(View.VISIBLE);
    }

    //开启进度条
    private void seekBarChanging() {
        seekBar_progress.setMax(1024*100);
        Log.d("duration",duration+"");
        seekBar_progress.setProgress(player.get());
        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                if (ischanging == true) {
                    return;
                }
                seekBar_progress.setProgress(player.get());
                Log.d("seekbar",player.get()+"");
            }
        };
        timer.schedule(timerTask,0,1000);
    }


    private class SeekListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (player == null) {
                return;
            }
            if (fromUser) {
                // 如果用户拖动才执行 否则造成进度条线程 多重调用
                ischanging = true;
                currentPosition = progress;
                //player.seekTo(progress);
                seekBar_progress.setProgress(progress);
                player.seekTo(seekBar.getProgress());
                seekBarChanging();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            ischanging = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (player == null) {
                return;
            }
            seekBar_progress.setProgress(currentPosition);
            player.seekTo(seekBar.getProgress());
            seekBarChanging();
            ischanging = false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

}
