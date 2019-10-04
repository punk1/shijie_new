package com.example.shijie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BlockedNumberContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shijie.adapter.AdapterComment;
import com.example.shijie.adapter.IndicatorAdapter;
import com.example.shijie.beans.Comment;
import com.example.shijie.beans.Config;
import com.example.shijie.beans.DynamicItem;
import com.example.shijie.widget.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class Detail_write_poetry extends AppCompatActivity implements View.OnClickListener{
    private ImageView quan_back;
    private TextView write_btn;

    private TextView detail_write_title;
    private TextView detail_write_content;
    private TextView detail_write_zhushi;
    private ImageView zan;
    private TextView zan_text;
    private ImageView commit;
    private TextView commit_text;
    private TextView updata_text;

    private String id;
    private Integer zan_id;

    private ImageView comment;
    private TextView hide_down;
    private EditText comment_content;
    private Button comment_send;

    private LinearLayout rl_enroll;
    private RelativeLayout rl_comment;

    private ListView comment_list;
    private AdapterComment adapterComment;
    private List<Comment> data;

    private final   Comment comment1 = new Comment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_write);

        initData();
        initView();

        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zan_id++;
                DynamicItem dynamicItem = new DynamicItem();
                dynamicItem.setZan_id(zan_id);
                dynamicItem.update(id, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e !=null){
                            Log.d("write", "done: e"+e);
                        }
                        Log.d("write", "done: 点击更新");
                    }
                });
                chaxun(id);

            }


        });

    }

    private void initData() {
        Intent intent = getIntent();
        if (intent!=null){
            Bundle bundle = intent.getBundleExtra("detail");
            if (bundle!=null){
                 id =  bundle.getString("objectid");
                 Log.d("ceshi", "initData: "+id);
                 chaxun(id);
            }
        }


    }

    private void chaxun(String id) {
        BmobQuery<DynamicItem> dynamicItemBmobQuery = new BmobQuery<>();
        dynamicItemBmobQuery.getObject(id, new QueryListener<DynamicItem>() {
            @Override
            public void done(DynamicItem dynamicItem, BmobException e) {
                if (e==null){
                    //加载数据
                    dynamicload(dynamicItem);
                }   else{

                }
            }
        });
    }

    private void dynamicload(DynamicItem dynamicItem) {
        detail_write_title.setText(dynamicItem.getTitle());
        String content = dynamicItem.getQ_content();
        content = content.replaceAll("<br>","");
        content =  content.replace('\n',' ');
        content = content.trim();
        String content2 = content.replaceAll("。","\r\n");
        detail_write_content.setText(content2);

        detail_write_zhushi.setText(dynamicItem.getZhushi());
        updata_text.setText(dynamicItem.getCreatedAt());
        zan_id = dynamicItem.getZan_id();
        Log.d("write", "dynamicload: "+dynamicItem.getObjectId());
        Log.d("write", "dynamicload: "+dynamicItem.getZan_id());
        zan_text.setText(""+dynamicItem.getZan_id());


    }

    public void initView(){
        quan_back = findViewById(R.id.quan_back);
        write_btn = findViewById(R.id.write_btn);
        quan_back.setVisibility(View.VISIBLE);
        write_btn.setVisibility(View.INVISIBLE);
        detail_write_title = findViewById(R.id.detail_write_title);
        detail_write_content = findViewById(R.id.detail_write_content);
        detail_write_zhushi = findViewById(R.id.detail_write_zhushi);
        zan = findViewById(R.id.zan);
        zan_text = findViewById(R.id.zan_text);
        commit = findViewById(R.id.commit);
        commit_text = findViewById(R.id.commit_text);
        updata_text = findViewById(R.id.updata_text);

        quan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // 初始化评论列表
        comment_list = (ListView) findViewById(R.id.comment_list);
        // 初始化数据
        data = new ArrayList<Comment>();
        BmobQuery<Comment> bmobQuery = new BmobQuery<>();
        bmobQuery.order("-updatedAt");

        bmobQuery.addWhereEqualTo("biao_id",id);
        Log.d("ceshi", "initView: "+id);
        bmobQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                Log.d("comment", "done: list"+list.size());
                data = list;
                Log.d("comment", "done: "+data.size());
                // 初始化适配器
                adapterComment = new AdapterComment(getApplicationContext(), list);
                // 为评论列表设置适配器
                comment_list.setAdapter(adapterComment);
            }
        });


        comment = (ImageView) findViewById(R.id.comment);
        hide_down = (TextView) findViewById(R.id.hide_down);
        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_send = (Button) findViewById(R.id.comment_send);

        rl_enroll = (LinearLayout) findViewById(R.id.enroll);
        rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);

        setListener();


    }
    public void setListener(){
        comment.setOnClickListener(this);

        hide_down.setOnClickListener(this);
        comment_send.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.comment:
                Log.d("comment", "onClick: 点击了评论");
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                // 显示评论框
                rl_enroll.setVisibility(View.GONE);
                rl_comment.setVisibility(View.VISIBLE);
                break;
            case R.id.hide_down:
                // 隐藏评论框
                rl_enroll.setVisibility(View.VISIBLE);
                rl_comment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
                break;
            case R.id.comment_send:
                sendComment();
                break;
            default:
                break;
        }

    }
    public void sendComment(){
        if(comment_content.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "评论不能为空！", Toast.LENGTH_SHORT).show();
        }else{
            // 生成评论数据
            String content = comment_content.getText().toString();
            String name = Config.getInstance().user.getUsername();
            String objectid = id;

            comment1.setName(name);
            comment1.setBiao_id(id);
            comment1.setContent(content);
            comment1.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {

                }
            });

            BmobQuery<Comment> bmobQuery = new BmobQuery<>();
            bmobQuery.order("-updatedAt");

            bmobQuery.addWhereEqualTo("biao_id",id);
            Log.d("ceshi", "initView: "+id);
            bmobQuery.findObjects(new FindListener<Comment>() {
                @Override
                public void done(List<Comment> list, BmobException e) {
                    Log.d("comment", "done: list"+list.size());
                    data = list;
                    Log.d("comment", "done: "+data.size());
                    // 初始化适配器
                    adapterComment = new AdapterComment(getApplicationContext(), list);
                    // 为评论列表设置适配器
                    comment_list.setAdapter(adapterComment);
                }
            });
            // 发送完，清空输入框
            comment_content.setText("");
            Toast.makeText(getApplicationContext(), "评论成功！", Toast.LENGTH_SHORT).show();
        }
    }
}
