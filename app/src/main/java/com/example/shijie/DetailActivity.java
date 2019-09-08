package com.example.shijie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.shijie.beans.Poetry;
import com.example.shijie.interfaces.AlbumDetailViewCallBack;
import com.example.shijie.interfaces.IAlbumDetailPresenter;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements IAlbumDetailPresenter, AlbumDetailViewCallBack {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        initView();
    }

    public   void initView() {
        TextView tv_poem_title = findViewById(R.id.tv_poem_title);
        TextView tv_poem_author = findViewById(R.id.album_author);
        TextView tv_poem_year = findViewById(R.id.tv_poem_year);
    }


    @Override
    public void onDetailListLoaded(List<Poetry> tracks) {

    }

    @Override
    public void onAlbumLoaded(Poetry album) {

    }

    @Override
    public void pullRefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int albumId, int page) {

    }

    @Override
    public void registerViewCallback(AlbumDetailViewCallBack callBack) {

    }

    @Override
    public void unregisterViewCallback(AlbumDetailViewCallBack callBack) {

    }
}
