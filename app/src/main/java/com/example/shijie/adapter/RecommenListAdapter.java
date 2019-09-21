package com.example.shijie.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shijie.R;
import com.example.shijie.beans.Poetry;

import java.util.ArrayList;
import java.util.List;

public class RecommenListAdapter extends RecyclerView.Adapter<RecommenListAdapter.InnerHoler>{
    private List<Poetry> mData = new ArrayList<>();
    private String TAG = "tui";
    private TextView title_tv;
    private TextView album_description;
    private TextView album_author;
    private TextView album_dynasty;

    public onRecommendItemClicklistener mItemClickListener;

    @NonNull
    @Override
    public RecommenListAdapter.InnerHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //这里是 载View
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.iten_recommend,viewGroup,false);
        return new InnerHoler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommenListAdapter.InnerHoler innerHoler, int i) {
        // 设置数据
        innerHoler.itemView.setTag(i);
        innerHoler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mItemClickListener!= null){
                    mItemClickListener.onItemClick((Integer) v.getTag(),mData.get((Integer) v.getTag()));
                }
            }
        });
        innerHoler.setData(mData.get(i));

    }

    @Override
    public int getItemCount() {
        if(mData!=null){
            return mData.size();
        }
        Log.d(TAG, "getItemCount: mData  --->"+mData.size());
        return 0;
    }

    public void setData(List<Poetry> albumList) {
        if(mData !=null){
            mData.clear();
            mData.addAll(albumList);
        }
        // 更新一下ui
        notifyDataSetChanged();
    }

    public class InnerHoler extends RecyclerView.ViewHolder{
        public InnerHoler(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Poetry album) {
            title_tv = itemView.findViewById(R.id.album_title_tv);
            album_description = itemView.findViewById(R.id.album_description);
            album_author = itemView.findViewById(R.id.album_author);

            album_dynasty = itemView.findViewById(R.id.album_dynasty);

            title_tv.setText(album.getP_name());
            album_description.setText(album.getP_content());
            album_author.setText(album.getP_author());
            album_dynasty.setText(album.getP_source());


        }
    }
    public void setonRecommendItemClicklistener(onRecommendItemClicklistener listener){

        this.mItemClickListener =  listener ;
    }
    public interface onRecommendItemClicklistener{
        void onItemClick(int postion, Poetry album);
    }
}
