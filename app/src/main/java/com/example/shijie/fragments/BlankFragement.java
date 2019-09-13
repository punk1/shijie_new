package com.example.shijie.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shijie.R;
import com.example.shijie.base.BaseFragment;
import com.example.shijie.beans.Config;
import com.example.shijie.beans.PoemBean;
import com.example.shijie.beans.PoemWordBean;
import com.example.shijie.beans.PoetryHistory;
import com.example.shijie.utils.DisplayUtil;
import com.example.shijie.utils.LogUtil;
import com.example.shijie.utils.StringUtils;
import com.example.shijie.widget.base.SimpleRecyclertViewAdater;
import com.example.shijie.widget.base.ViewHolder;
import com.example.shijie.widget.decoration.SpacesItemDecoration;
import com.example.shijie.widget.interfaces.OnItemClickListener;
import com.example.shijie.widget.interfaces.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BlankFragement extends BaseFragment {
    private TextView tv_poem_title;
    private RelativeLayout rl_poem_info;
    private TextView tv_poem_author;
    private TextView tv_poem_year;
    private RecyclerView rv_top;
    private TextView tv_submit;
    private TextView tv_show_correct;
    private  TextView tv_reblank;
    private RecyclerView rv_bottom;

    private SimpleRecyclertViewAdater<PoemWordBean> mTopAdapter;
    private List<PoemWordBean> topData = new ArrayList<>();
    private List<PoemWordBean> bottomData = new ArrayList<>();
    private List<Integer> blankIndex = new LinkedList<>();
    private PoemBean mPoemBean;
    private SimpleRecyclertViewAdater<PoemWordBean> mBottomAdapter;
    private int currentLongClickPosition = -1;//当前长按的角标
    private int fragmentPosition;
    private int poemType;
    private boolean showCorrect = false;//是否显示正确答案
    private boolean firstPoint;
    private int maxTopCount;//最大显示字数
    private int topCount;//一行显示字数

    @Override
    protected View onSubViewLoader(LayoutInflater layoutInflater, ViewGroup container) {
        View rootView = layoutInflater.inflate(R.layout.blank_fragment,container,false);
        init(rootView);
        int screenWidth = DisplayUtil.getScreenWidth(getActivity());
        maxTopCount = (screenWidth - DisplayUtil.dp2px(getActivity(), 10)) / (10 + DisplayUtil.dp2px(getActivity(), 30));
        Bundle arguments = getArguments();
        if (arguments != null) {
            fragmentPosition = arguments.getInt("position");
            poemType = arguments.getInt("poemType");
            mPoemBean = (PoemBean) arguments.getSerializable("poemBean");
            if (mPoemBean != null) {
                tv_poem_title.setText(mPoemBean.getPoemTitle());
                tv_poem_author.setText(mPoemBean.getPoemAuthor());
                tv_poem_year.setText(mPoemBean.getPoemYear());
            }
        }
        initData();
        initTopRecycleView();
        initBottomRecycleView();
        tv_show_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showCorrect){
                    tv_show_correct.setText(getActivity().getResources().getString(R.string.show_correct));
                }else{
                    tv_show_correct.setText(getActivity().getResources().getString(R.string.continue_text));

                }
                showCorrect = !showCorrect;
                if (mTopAdapter != null) {
                    mTopAdapter.notifyDataSetChanged();
                }

            }
        });

        tv_reblank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
                currentLongClickPosition = -1;
                showCorrect = false;
                tv_show_correct.setText(getActivity().getResources().getString(R.string.show_correct));
                if (mTopAdapter != null) {
                    mTopAdapter.notifyDataSetChanged();
                }
                if (mBottomAdapter != null) {
                    mBottomAdapter.notifyDataSetChanged();
                }

            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHistory(mPoemBean);
                showCorrect = false;
                tv_show_correct.setText(getActivity().getResources().getString(R.string.show_correct));
                if (mTopAdapter != null) {
                    mTopAdapter.notifyDataSetChanged();
                }
                if (mBottomAdapter != null) {
                    mBottomAdapter.notifyDataSetChanged();
                }
                boolean flag = true;
                for (int i = 0; i < topData.size(); i++) {
                    PoemWordBean poemWordBean = topData.get(i);
                    String word = poemWordBean.getWord();
                    String correctWord = poemWordBean.getCorrectWord();
                    if (correctWord != null && !correctWord.equals(word)) {
                        flag = false;
                        poemWordBean.setErrorWord(true);
                        reflashTopAdapter(i);
                    }
                }
                if(flag){
                    Toast.makeText(getContext(),"恭喜你，全部正确",Toast.LENGTH_SHORT);

                }
            }
        });

        return  rootView;
    }
    public void init(View rootView){
        tv_poem_title = rootView.findViewById(R.id.tv_poem_title);
        rl_poem_info = rootView.findViewById(R.id.rl_poem_info);
        tv_poem_author = rootView.findViewById(R.id.tv_poem_author);
        tv_poem_year = rootView.findViewById(R.id.tv_poem_year);
        rv_top= rootView.findViewById(R.id.rv_top);
        tv_submit = rootView.findViewById(R.id.tv_submit);
        tv_show_correct = rootView.findViewById(R.id.tv_show_correct);
        tv_reblank = rootView.findViewById(R.id.tv_reblank);
        rv_bottom = rootView.findViewById(R.id.rv_bottom);
    }
    private void initData() {
        blankIndex.clear();
        topData.clear();
        bottomData.clear();
        String correctPoem = mPoemBean.getCorrectPoem();
        correctPoem = correctPoem.replaceAll(" ","");
        for (int i = 0; i < correctPoem.length(); i++) {
            String word = String.valueOf(correctPoem.charAt(i));
            PoemWordBean poemWordBean = new PoemWordBean();
            poemWordBean.setBottomPosition(-1);
            poemWordBean.setCorrectWord(word);
            boolean chinesePunctuation = StringUtils.isChinesePunctuation(word.charAt(0));
            if (chinesePunctuation) {
                poemWordBean.setWord(word);
                if (!firstPoint) {
                    firstPoint = true;
                    topCount = i + 1;
                }
            } else {
                poemWordBean.setWord("");
                blankIndex.add(i);
            }
            poemWordBean.setPunctuation(chinesePunctuation);
            poemWordBean.setErrorWord(false);
            topData.add(poemWordBean);
        }
        String disturbPoem = StringUtils.getShuffleString(correctPoem);
        for (int i = 0; i < disturbPoem.length(); i++) {
            String word = String.valueOf(disturbPoem.charAt(i));
            boolean chinesePunctuation = StringUtils.isChinesePunctuation(word.charAt(0));
            if (!chinesePunctuation) {
                PoemWordBean poemWordBean = new PoemWordBean();
                poemWordBean.setBottomPosition(-1);
                poemWordBean.setWord(word);
                bottomData.add(poemWordBean);
            }
        }
        if (mTopAdapter != null) {
            mTopAdapter.notifyDataSetChanged();
        }
        if (mBottomAdapter != null) {
            mBottomAdapter.notifyDataSetChanged();
        }
    }
    //保存历史记录
    private void saveHistory(PoemBean mPoemBean) {
        PoetryHistory history = new PoetryHistory();
        history.setP_id(mPoemBean.getPoemId());
        history.setP_author(mPoemBean.getPoemAuthor());
        history.setP_title(mPoemBean.getPoemTitle());
        history.setU_id(Config.getInstance().user.getObjectId());
        history.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){

                }else{

                    history.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {

                            }else{

                            }
                        }
                    });
                }
            }
        });
    }

    private void initTopRecycleView() {
        if(topCount <= 0){
            topCount = maxTopCount;
        } else if(topCount >= maxTopCount){
            topCount = maxTopCount;
        }
        SpacesItemDecoration universalDecoration = new SpacesItemDecoration(5);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), topCount, GridLayoutManager.VERTICAL, false);
        rv_top.setLayoutManager(gridLayoutManager);
        mTopAdapter = new SimpleRecyclertViewAdater<PoemWordBean>(getActivity(), topData, R.layout.item_top_blank) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, PoemWordBean itemBean, int position) {
                TextView tvItem = holder.getView(R.id.tv_item_text);
                LinearLayout llContainer = holder.getView(R.id.ll_top_container);
                if (itemBean != null) {
                    if(showCorrect){
                        String word = itemBean.getCorrectWord();
                        tvItem.setText(word == null ? "" : word);
//                        tvItem.setBackground(null);
                        llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bottom_bg));
                    }else{
                        String word = itemBean.getWord();
                        tvItem.setText(word == null ? "" : word);
                        if(itemBean.isErrorWord()){
                            llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_error_bg));
                        }else{
                            if (!StringUtils.isEmpty(word)) {
                                llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bottom_bg));
                            } else {
                                llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bg));
                            }
                        }
                    }
                }
            }
        };
        rv_top.addItemDecoration(universalDecoration);
        rv_top.setAdapter(mTopAdapter);
        mTopAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, ViewHolder holder) {
                if(showCorrect){
                    return;
                }
                PoemWordBean topPoemWordBean = topData.get(position);
                if (topPoemWordBean != null) {
                    String word = topPoemWordBean.getWord();
                    boolean punctuation = topPoemWordBean.isPunctuation();
                    int bottomPosition = topPoemWordBean.getBottomPosition();
                    if (currentLongClickPosition != -1) {
                        if(!punctuation){
                            if (StringUtils.isEmpty(word)) {
                                blankIndex.remove(Integer.valueOf(position));
                            } else {
                                PoemWordBean poemWordBean = bottomData.get(bottomPosition);
                                poemWordBean.setWord(word);
                                reflashBottomAdapter(bottomPosition);
                            }
                            PoemWordBean poemWordBean = bottomData.get(currentLongClickPosition);
//                            PoemWordBean topBean = topData.get(position);
                            String word1 = poemWordBean.getWord();
                            poemWordBean.setLongClick(false);
                            poemWordBean.setWord("");
                            reflashBottomAdapter(currentLongClickPosition);

                            topPoemWordBean.setWord(word1);
                            topPoemWordBean.setBottomPosition(currentLongClickPosition);
                            reflashTopAdapter(position);
                            currentLongClickPosition = -1;
                        }
                    } else {
                        if (!StringUtils.isEmpty(word)) {
                            if (!punctuation) {
                                if (bottomPosition != -1 && bottomPosition < bottomData.size()) {
                                    PoemWordBean bottomPoemBean = bottomData.get(bottomPosition);
                                    bottomPoemBean.setWord(word);
                                    reflashBottomAdapter(bottomPosition);

                                    topPoemWordBean.setWord("");
                                    reflashTopAdapter(position);
                                    blankIndex.add(position);
                                    Collections.sort(blankIndex);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void initBottomRecycleView() {
        SpacesItemDecoration universalDecoration = new SpacesItemDecoration(5);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), topCount, GridLayoutManager.VERTICAL, false);
        rv_bottom.setLayoutManager(gridLayoutManager);
        mBottomAdapter = new SimpleRecyclertViewAdater<PoemWordBean>(getActivity(), bottomData, R.layout.item_bottom_blank) {
            @Override
            protected void onBindViewHolder(ViewHolder holder, int itemType, PoemWordBean itemBean, int position) {
                TextView tvItem = holder.getView(R.id.tv_item_text);
                LinearLayout llContainer = holder.getView(R.id.ll_bottom_container);
                if (itemBean != null) {
                    String word = itemBean.getWord();
                    boolean longClick = itemBean.isLongClick();
                    tvItem.setText(word == null ? "" : word);
                    if (!StringUtils.isEmpty(word)) {
                        if (longClick) {
                            llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bottom_long_bg));
                        } else {
                            llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bottom_bg));
                        }
                    } else {
                        llContainer.setBackground(getResources().getDrawable(R.drawable.blank_item_bg));
                    }
                }
            }
        };
        rv_bottom.addItemDecoration(universalDecoration);
        rv_bottom.setAdapter(mBottomAdapter);
        mBottomAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position, ViewHolder holder) {
                if(showCorrect){
                    return;
                }
                if (currentLongClickPosition != -1) {
                    PoemWordBean poemWordBean = bottomData.get(currentLongClickPosition);
                    poemWordBean.setLongClick(false);
                    reflashBottomAdapter(currentLongClickPosition);
                    currentLongClickPosition = -1;
                } else {
                    PoemWordBean bottomPoemWordBean = bottomData.get(position);
                    if (!StringUtils.isEmpty(bottomPoemWordBean.getWord())) {
                        PoemWordBean topPoemWordBean = topData.get(blankIndex.get(0));
                        topPoemWordBean.setWord(bottomPoemWordBean.getWord());
                        topPoemWordBean.setBottomPosition(position);
                        bottomPoemWordBean.setWord("");
                        reflashTopAdapter(blankIndex.get(0));
                        reflashBottomAdapter(position);
                        blankIndex.remove(0);
                    }
                }
            }
        });
        mBottomAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position, ViewHolder holder) {
                if(showCorrect){
                    return false;
                }
                if (currentLongClickPosition == -1) {
                    PoemWordBean bottomPoemBean = bottomData.get(position);
                    bottomPoemBean.setLongClick(true);
                    reflashBottomAdapter(position);
                    currentLongClickPosition = position;
                } else {
                    PoemWordBean poemWordBean = bottomData.get(currentLongClickPosition);
                    poemWordBean.setLongClick(false);
                    reflashBottomAdapter(currentLongClickPosition);

                    PoemWordBean bottomPoemBean = bottomData.get(position);
                    bottomPoemBean.setLongClick(true);
                    reflashBottomAdapter(position);
                    currentLongClickPosition = position;
                }
                return false;
            }
        });
    }

    private void reflashBottomAdapter(int position) {
        if (mBottomAdapter != null) {
            mBottomAdapter.notifyItemChanged(position);
        }
    }

    private void reflashTopAdapter(int position) {
        if (mTopAdapter != null) {
            mTopAdapter.notifyItemChanged(position);
        }
    }
    public static BlankFragement newInstance(int position,PoemBean poemBean) {

        Bundle args = new Bundle();
        args.putInt("position",position);
        args.putSerializable("poemBean",poemBean);
        BlankFragement fragment = new BlankFragement();
        fragment.setArguments(args);
        return fragment;
    }

}
