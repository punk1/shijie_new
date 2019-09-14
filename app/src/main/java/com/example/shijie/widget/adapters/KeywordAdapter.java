package com.example.shijie.widget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.shijie.R;
import com.example.shijie.widget.CustomTextView;
import com.example.shijie.widget.base.ViewHolder;

public class KeywordAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private String[] datas;
    private Context mContext;

    public KeywordAdapter(Context context,String[] datas){
        this.mContext = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (datas != null) {
            return datas.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (datas != null) {
            return datas[position];
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            // 根布局为我们自定义的LsCheckedableTextView
            convertView = inflater.inflate(R.layout.item_keyword_choose,null);
            holder.checkedableTextView = (CustomTextView) convertView;
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checkedableTextView.setText(datas[position]);
        return convertView;
    }
    private static class ViewHolder{
        CustomTextView checkedableTextView;
    }
}
