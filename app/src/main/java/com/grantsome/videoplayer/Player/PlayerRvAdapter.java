package com.grantsome.videoplayer.Player;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.grantsome.videoplayer.Model.ContentBean;
import com.grantsome.videoplayer.R;

import java.util.ArrayList;

/**
 * Created by tom on 2017/5/20.
 */

public class PlayerRvAdapter extends RecyclerView.Adapter {

    private PlayerHolder holder;

    private ArrayList<ContentBean> mContentList;

    public PlayerRvAdapter(){
        mContentList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        holder = new PlayerHolder(inflater.inflate(R.layout.item_player,parent,false),mContentList);
        holder.addOnClickListener();
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PlayerHolder questionViewHolder = (PlayerHolder) holder;
        questionViewHolder.updata(mContentList.get(position));
    }


    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    public void addContentBean(ArrayList<ContentBean> contentList){
        mContentList.clear();
        if(mContentList!=null) {
            mContentList.addAll(contentList);
        }
        notifyDataSetChanged();
    }
}
