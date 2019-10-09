package com.example.shijie.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shijie.R;
import com.example.shijie.beans.DynamicItem;

import java.util.ArrayList;
import java.util.List;

public class QuanlistAdapter extends RecyclerView.Adapter<QuanlistAdapter.Inneholder> {

    private onquanItemClicklistener mItemClickListener;
    private List<DynamicItem> mData = new ArrayList<>();
    private TextView author_title_quan;
    private TextView poetry_title_quan;
    private TextView poetry_content_quan;
    private TextView poetry_chuagnzuo_quan;
    private TextView zan_text;
    private TextView updata_text;

    @NonNull
    @Override
    public QuanlistAdapter.Inneholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View  itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_quan,viewGroup,false);
        return new Inneholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuanlistAdapter.Inneholder inneholder, int i) {
        inneholder.itemView.setTag(i);
        inneholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mItemClickListener!= null){
                    mItemClickListener.onItemClick((Integer) v.getTag(),mData.get((Integer) v.getTag()));
                }
            }
        });
        inneholder.setData(mData.get(i));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void  setData(List<DynamicItem> dynamicItemList){
        if (mData != null){
            mData.clear();
            mData.addAll(dynamicItemList);
        }
        notifyDataSetChanged();

    }



    public class Inneholder extends RecyclerView.ViewHolder{

        public Inneholder(@NonNull View itemView) {
            super(itemView);
        }

        public void  setData(DynamicItem dynamicItem){
            author_title_quan = itemView.findViewById(R.id.author_title_quan);
            poetry_title_quan = itemView.findViewById(R.id.poetry_title_quan);
            poetry_content_quan = itemView.findViewById(R.id.poetry_content_quan);
            poetry_chuagnzuo_quan = itemView.findViewById(R.id.poetry_chuagnzuo_quan);
            zan_text = itemView.findViewById(R.id.zan_text);
            updata_text = itemView.findViewById(R.id.updata_text);

            author_title_quan.setText(dynamicItem.getAuthor_name());
            poetry_title_quan.setText(dynamicItem.getTitle());

            String content = dynamicItem.getQ_content();
            content = content.replaceAll("<br>","");
            content =  content.replace('\n',' ');
            content = content.trim();
            String content2 = content.replaceAll("。","\r\n");
            Log.d("quan", "setData: "+content2);

            poetry_content_quan.setText(content2);
            Log.d("shi", "setData: "+dynamicItem.getZhushi());
            poetry_chuagnzuo_quan.setText(""+dynamicItem.getZhushi());
            zan_text.setText(""+dynamicItem.getZan_id());
            updata_text.setText(dynamicItem.getUpdatedAt());
        }
    }
    public void setonquanItemClicklistener(onquanItemClicklistener listener){

        this.mItemClickListener =  listener ;
    }
    public interface onquanItemClicklistener{
        void onItemClick(int postion, DynamicItem poe);
    }
}
