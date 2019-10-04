package com.example.shijie.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shijie.R;
import com.example.shijie.beans.Comment;

import java.util.ArrayList;
import java.util.List;

public class AdapterComment extends BaseAdapter {

    Context context;
    private List<Comment> mdata = new ArrayList<>();

    public AdapterComment(Context c, List<Comment> mdata){
        this.context = c;
        this.mdata = mdata;
        Log.d("comment", "AdapterComment: "+mdata.size());
        Log.d("comment", "AdapterComment1: "+mdata.size());
    }

    @Override
    public int getCount() {
        Log.d("comment", "getCount: "+mdata.size());
        return mdata.size();
    }

    @Override
    public Object getItem(int i) {
        return mdata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        // 重用convertView
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
            holder.comment_name = (TextView) convertView.findViewById(R.id.comment_name);
            holder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 适配数据
        holder.comment_name.setText(mdata.get(i).getName());
        holder.comment_content.setText(mdata.get(i).getContent());

        return convertView;
    }

    /**
     * 添加一条评论,刷新列表
     * @param comment
     */
    public void addComment(Comment comment){
        mdata.add(comment);
        notifyDataSetChanged();
    }

    /**
     * 静态类，便于GC回收
     */
    public static class ViewHolder{
        TextView comment_name;
        TextView comment_content;
    }
}
