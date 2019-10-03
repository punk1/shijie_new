package com.example.shijie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shijie.adapter.Albumlitadapter;
import com.example.shijie.beans.Poetry;
import com.example.shijie.interfaces.IsearchCallBack;
import com.example.shijie.presenters.AlbumDetailPresenter;
import com.example.shijie.presenters.searchprecenter;
import com.example.shijie.views.UIloader;

import java.util.List;

public class searchActivity extends AppCompatActivity implements IsearchCallBack ,Albumlitadapter.onRecommendItemClicklistener{

    private ImageView search_back;
    private EditText search_input;
    private TextView search_btn;
    private FrameLayout search_container;
    private searchprecenter msearchprecenter;
    private UIloader mcontent;
    private RecyclerView mresultView;
    private Albumlitadapter malbumlitadapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        initView();
        initPrecenter();
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = "";
                keyword = search_input.getText().toString().trim();
                if (msearchprecenter!=null){
                    msearchprecenter.doSearch(keyword);
                    mcontent.updatastatus(UIloader.UIStaus.LOADING);
                }

            }
        });
        mcontent.setOnRetyrclickListenr(new UIloader.OnRetyrclickListenr() {
            @Override
            public void ReTyrClick() {
                if (msearchprecenter!=null){
                    msearchprecenter.research();
                    mcontent.updatastatus(UIloader.UIStaus.LOADING);
                }
            }
        });

    }

    private void initPrecenter() {
        //注册ui 更新的接口
        msearchprecenter=  searchprecenter.getSearchprecenter();
        msearchprecenter.registerViewCallback(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (msearchprecenter!=null){
            //干掉ui更新的接口
            msearchprecenter.unregisterViewCallback(this);
            msearchprecenter=null;
        }
    }

    public void initView(){
        search_back = findViewById(R.id.search_back);
        search_input = findViewById(R.id.search_input);
        search_btn = findViewById(R.id.search_btn);
        search_container = findViewById(R.id.search_container);
        if (mcontent==null){
            mcontent = new UIloader(this) {
                @Override
                public View getSuccesView() {
                    return creatSucessView();
                }
            };
           if(mcontent.getParent()instanceof ViewGroup){
              ((ViewGroup) mcontent.getParent()).removeView(mcontent);
           }
           search_container.addView(mcontent);
        }


    }

    /**
     * 创建数据请求成功的View
     * @return
     */

    private View creatSucessView() {
       View resultView = LayoutInflater.from(this).inflate(R.layout.search_result_layout,null);
        mresultView = resultView.findViewById(R.id.result_list_view);
       //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mresultView.setLayoutManager(layoutManager);
        // 设置适配器
        malbumlitadapter = new Albumlitadapter();
        malbumlitadapter.setonRecommendItemClicklistener(this);
        mresultView.setAdapter(malbumlitadapter);
        return  resultView;
    }

    @Override
    public void onResultsearchLoad(List<Poetry> tracks) {
        //隐藏键盘
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(search_input.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        if (tracks!=null){
            if (tracks.size()==0){
                if(mcontent!=null){
                    mcontent.updatastatus(UIloader.UIStaus.EMPTY);
                }
            }else{
                //设值数据
                malbumlitadapter.setData(tracks);
                mcontent.updatastatus(UIloader.UIStaus.SUCCESS);
            }
        }

    }

    @Override
    public void onloadmoreResult(List<Poetry> tracks, boolean isok) {

    }

    @Override
    public void onrecommendload(List<Poetry> tracks) {

    }

    @Override
    public void onError(int errorcode, String errorMsg) {
        if (mcontent!=null){
            mcontent.updatastatus(UIloader.UIStaus.NETWORK_ERROR);
        }

    }

    @Override
    public void onItemClick(int postion, Poetry album) {
        Bundle bundle = new Bundle();
        bundle.putString("p_id",album.getObjectId());
        bundle.putInt("from",1);
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        // item  被电 跳转到详情界面
        Intent intent = new Intent(searchActivity.this, DetailActivity.class);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

}
