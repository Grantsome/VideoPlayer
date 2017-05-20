package com.grantsome.videoplayer.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.grantsome.videoplayer.Player.PlayerRvAdapter;
import com.grantsome.videoplayer.R;
import com.grantsome.videoplayer.Util.ApiString;
import com.grantsome.videoplayer.Util.HttpUtils;
import com.grantsome.videoplayer.Util.JsonParser;
import com.grantsome.videoplayer.Util.ToastUtils;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPlayerRv;

    private PlayerRvAdapter mPlayerRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayerRv = (RecyclerView) findViewById(R.id.playerRv);
        setUpAnswerRv();
        initData();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        initData();
    }

    public void initData(){
        HttpUtils.sendHttpRequest(ApiString.getParams(), null, new HttpUtils.Callback() {
            @Override
            public void onResponse(HttpUtils.Response response) {
                mPlayerRvAdapter.addContentBean(JsonParser.getContentList(response.bodyString()));
            }

            @Override
            public void onFail(Exception e) {
                ToastUtils.showError("获取内容失败");
            }
        });
    }

    private void setUpAnswerRv(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPlayerRvAdapter = new PlayerRvAdapter();
        mPlayerRv.setAdapter(mPlayerRvAdapter);
        mPlayerRv.setLayoutManager(layoutManager);
    }

}
