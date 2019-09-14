package com.example.shijie.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shijie.R;


/**
 * @author L
 * @desc
 */
@SuppressLint("AppCompatCustomView")
public class CustomTextView extends LinearLayout implements Checkable {
    private TextView mItemView;
    private RelativeLayout mRelativeLayout;
    private boolean mChecked;

    public CustomTextView(Context context) {
        this(context,null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        mLayoutInflater.inflate(R.layout.single_text_view,CustomTextView.this,true);
        View v = mLayoutInflater.inflate(R.layout.single_text_view, this, true);
        mItemView = (TextView)v.findViewById(R.id.tv_single_item);
        mRelativeLayout = (RelativeLayout)v.findViewById(R.id.rl_single_item);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        mRelativeLayout.setBackground(checked?getContext().getResources().getDrawable(R.drawable.blank_item_bottom_long_bg)
                :null);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(mChecked);
    }
    public void setText(String title) {
        mItemView.setText(title);
    }
}
