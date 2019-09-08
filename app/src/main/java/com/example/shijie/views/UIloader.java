package com.example.shijie.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.shijie.R;
import com.example.shijie.base.BaseApplication;


public abstract class UIloader extends FrameLayout {
    private View mloadingView;
    private View mSuccessView;
    private View mNetWorkErrorView;
    private View mEmptyView;
    private OnRetyrclickListenr mOnRetyrclickListenr = null;

    public enum UIStaus {
        LOADING,SUCCESS,NETWORK_ERROR,EMPTY,NONE
    }
    public UIStaus mcurrentUIstatus = UIStaus.NONE;


    public UIloader(Context context) {
        super(context);
    }

    public UIloader( Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public UIloader( Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void updatastatus(UIStaus staus){
        mcurrentUIstatus = staus;
        // 更新 ui  一定在 主线程 上

        BaseApplication.gethandler().post(new Runnable() {
            @Override
            public void run() {
                switchUiBycurrentStatus();
            }
        });

    }

    /**
     * 初始化 ui
     */
    private void init() {
        switchUiBycurrentStatus();
    }

    private void switchUiBycurrentStatus() {
        /**
         * 加载中
         */
        if (mloadingView== null){
            mloadingView= getLoadingView();
            addView(mloadingView);
        }
        /**
         * 根据状态设置是否 可见
         */
        mloadingView.setVisibility(mcurrentUIstatus == UIStaus.LOADING?VISIBLE:GONE);
        //成功
        if (mSuccessView== null){
            mSuccessView= getSuccesView();
            addView(mSuccessView);
        }
        mSuccessView.setVisibility(mcurrentUIstatus == UIStaus.SUCCESS?VISIBLE:GONE);
        //网络错误
        if (mNetWorkErrorView== null){
            mNetWorkErrorView= getNetworkError();
            addView(mNetWorkErrorView);
        }
        mNetWorkErrorView.setVisibility(mcurrentUIstatus == UIStaus.NETWORK_ERROR?VISIBLE:GONE);

        //数据为空
        if (mEmptyView== null){
            mEmptyView= getEmptyerror();
            addView(mEmptyView);
        }
        mEmptyView.setVisibility(mcurrentUIstatus == UIStaus.EMPTY?VISIBLE:GONE);


    }

    private View getEmptyerror() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view,this,false);
    }

    private View getNetworkError() {
        View nenetworkerrorView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_error_view,this,false);
        nenetworkerrorView.findViewById(R.id.network_error_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnRetyrclickListenr == null){
                    mOnRetyrclickListenr.ReTyrClick();
                }

            }
        });
//        nenetworkerrorView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //重新 获取数据
//              if (mOnRetyrclickListenr == null){
//                  mOnRetyrclickListenr.ReTyrClick();
//              }
//
//            }
//        });
        return nenetworkerrorView;

    }

    public abstract View getSuccesView();

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view,this,false);
    }
    public void setOnRetyrclickListenr(OnRetyrclickListenr listenr){
        this.mOnRetyrclickListenr = listenr;
    }
    public interface OnRetyrclickListenr{
        void ReTyrClick();
    }

}

