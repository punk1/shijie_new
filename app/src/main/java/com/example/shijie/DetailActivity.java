package com.example.shijie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shijie.base.Constants;
import com.example.shijie.beans.Config;
import com.example.shijie.beans.DynamicItem;
import com.example.shijie.beans.FlyiingOrderBean;
import com.example.shijie.beans.Poetry;
import com.example.shijie.beans.PoetryHistory;
import com.example.shijie.interfaces.AlbumDetailViewCallBack;
import com.example.shijie.presenters.AlbumDetailPresenter;
import com.example.shijie.utils.LogUtil;
import com.example.shijie.utils.StringUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class DetailActivity extends AppCompatActivity implements  AlbumDetailViewCallBack {
    TextView tv_poem_title;
    TextView tv_title;
    TextView tv_poem_author;
    TextView tv_poem_year;
    TextView tv_poem_content;
    TextView tv_play;
    ImageView iv_play;
    ImageView iv_back;
    ImageView iv_title_right;
    private String currentText;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private SpeechSynthesizer mTts;
    private String p_id;
    private String pp_id;//  飞花令的 记录
    private int from;//0是从历史记录过来， 1是从诗词背诵列表进来  2是从搜索进来   3 从飞花令列表进入
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    private int playStatus;//播放状态
    private SharedPreferences mSharedPreferences;
    private String[] mPersonValues;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Bmob.initialize(this, Constants.shi_BMOB_SDK_KEY);
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.xunfei_app_id));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);

        initViewData();
        mPersonValues = getResources().getStringArray(R.array.voicer_cloud_entries);
        initXunfei();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_title_right.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(DetailActivity.this,PlaySettingActivity.class);
               startActivity(intent);
           }
       });


        tv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xiang", "onClick: 点击了播放 ");
                playClick();
                Log.d("xiang", "onClick: 点击播放结束 ");
            }
        });
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("xiang", "onClick: 点击了播放图标 ");
                playClick();
            }
        });
    }
    public void playClick() {
        if (null == mTts) {
            Log.d("xiang", "playClick: 创建单利失败 ");
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip(getResources().getString(R.string.init_xunfei_fail));
            return;
        }

        switch (playStatus) {
            case 0://第一次进入时的默认状态
                if (StringUtils.isEmpty(currentText)) {
                    return;
                }
                // 设置参数
                setParam();
                int code = mTts.startSpeaking(currentText, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.ico";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

                if (code != ErrorCode.SUCCESS) {
                    showTip(getResources().getString(R.string.init_xunfei_fail3));
                }
                break;
            case 1://播放状态
                mTts.pauseSpeaking();
                break;
            case 2://暂停状态
                mTts.resumeSpeaking();
                break;
            default:
                break;
        }
    }
    private void setParam() {
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人,可在values/string.xml中选择合适的人的声音
        mTts.setParameter(SpeechConstant.VOICE_NAME, mPersonValues[Integer.valueOf(mSharedPreferences.getString("person_preference", "0"))]);
        //设置合成语速,可设置(0-100)
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        //设置合成音调,可设置(0-100)
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        //设置合成音量,可设置(0-100)
        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
    }
    private void saveHistory(Poetry mPoemBean) {

           Log.d("li", "initViewData:第五步 ");
           final PoetryHistory history = new PoetryHistory();
//           // shan 1
//           final DynamicItem dynamicItem = new DynamicItem();
           history.setP_id(mPoemBean.getObjectId());
//           //  shan 2
//           dynamicItem.setAuthor_id(Config.getInstance().user.getObjectId());
           Log.d("li", "initViewData: mPoemBean.getObjectId()"+ mPoemBean.getObjectId());
           history.setP_author(mPoemBean.getP_author());

//           //  sahn 3
//           dynamicItem.setTitle(mPoemBean.getP_name());
//           dynamicItem.setQ_content(mPoemBean.getP_content());
//           dynamicItem.setZhushi("瞎几把写吧");
           history.setP_title(mPoemBean.getP_name());
           Log.d("li", "initViewData:第六步 ");
           history.setU_id(Config.getInstance().user.getObjectId());
           Log.d("li", "initViewData:第五步 "+Config.getInstance().user.getObjectId());
//           dynamicItem.save(new SaveListener<String>() {
//               @Override
//               public void done(String s, BmobException e) {
//
//               }
//           });
           history.update(new UpdateListener() {
               @Override
               public void done(BmobException e) {
                   if (e == null) {
                       Log.d("lishi", "done: 更新历史成功");
                   } else {
                       Log.d("lishi", "done: 更新历史失败");
                       history.save(new SaveListener<String>() {
                           @Override
                           public void done(String s, BmobException e) {
                               if (e == null) {
                                   Log.d("lishi", "done: 保存历史成功");
                               } else {
                                   Log.d("lishi", "done: 保存历史失败");
                               }
                           }
                       });
                   }
               }
           });
       }


    private void initXunfei() {
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences(PlaySettingActivity.PREFER_NAME, MODE_PRIVATE);
    }

    public   void initView() {
         tv_title = findViewById(R.id.tv_title);
         tv_poem_title = findViewById(R.id.tv_poem_title);
         tv_poem_author = findViewById(R.id.tv_poem_author);
         tv_poem_year = findViewById(R.id.tv_poem_year);
         tv_poem_content = findViewById(R.id.tv_poem_content);
         iv_back = findViewById(R.id.iv_back);
         iv_title_right = findViewById(R.id.iv_title_right);
         tv_play = findViewById(R.id.tv_play);
         iv_play = findViewById(R.id.iv_play);
    }
    protected void initViewData() {
        Log.d("li", "initViewData:第一步 ");
        Intent intent = getIntent();
        if (intent != null) {
            Log.d("li", "initViewData:第二步 ");
            Bundle extras = intent.getBundleExtra("bundle");
            Bundle eextras = intent.getBundleExtra("bbundle");
            if (eextras!=null){
                pp_id = eextras.getString("ll");
                p_id =pp_id;
                initData();
            }
            if (extras != null) {
                p_id = extras.getString("p_id");

                Log.d("you", "initViewData: "+pp_id);
                from = extras.getInt("from");
                Log.d("xiang", "initViewData: from "+from);
                FlyiingOrderBean poety = (FlyiingOrderBean) extras.getSerializable("poety");
            if (from == 3) {
//                    if (pp_id != null) {
//                        p_id = pp_id;
//                        initData();
//                    }

                } else {
                    initData();
                }
            }
        }

    }
    private void initData() {
        Log.d("li", "initViewData:第三步 ");
        BmobQuery<Poetry> poetryBmobQuery = new BmobQuery<>();
        poetryBmobQuery.getObject(p_id, new QueryListener<Poetry>() {
            @Override
            public void done(Poetry poetry, BmobException e) {
                if (e == null) {
                    Log.d("li", "initViewData:第四步 ");
                    loadSuc(poetry);
                } else {
                    Log.d("bug", "done: p-id"+p_id);
                    Log.d("li", "done: "+e);

                }
            }
        });
    }


    private void loadSuc(Poetry poetry) {
           Log.d("xiang", "jiazai");
            Log.d("xiang", "loadSuc: 保存记录");
            if(from!=0){
                Log.d("bug", "loadSuc: zou 1");
                saveHistory(poetry);
            }else {
                Log.d("bug", "loadSuc: zou 2");
                onAlbumLoaded(poetry);
            }
            
    }


    @Override
    public void onDetailListLoaded(List<Poetry> tracks) {

    }
    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {

            if (code != ErrorCode.SUCCESS) {
                Log.d("xiang", "onInit: 初始化语音失败");
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            playStatus = 1;
            tv_play.setText(getResources().getString(R.string.pause_text));
            iv_play.setImageDrawable(getResources().getDrawable(R.drawable.iv_pause));
            showTip(getResources().getString(R.string.play_start));
        }

        @Override
        public void onSpeakPaused() {
            playStatus = 2;
            tv_play.setText(getResources().getString(R.string.play_text));
            iv_play.setImageDrawable(getResources().getDrawable(R.drawable.iv_play));
            showTip(getResources().getString(R.string.play_pause));
        }

        @Override
        public void onSpeakResumed() {
            playStatus = 1;
            tv_play.setText(getResources().getString(R.string.pause_text));
            iv_play.setImageDrawable(getResources().getDrawable(R.drawable.iv_pause));
            showTip(getResources().getString(R.string.play_continue));
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip(getResources().getString(R.string.play_complete));
                playStatus = 0;
                tv_play.setText(getResources().getString(R.string.replay_text));
                iv_play.setImageDrawable(getResources().getDrawable(R.drawable.iv_play));
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    @Override
    public void onAlbumLoaded(Poetry album) {
        String pTitle = album.getP_name();
        String pAuthor = album.getP_author();
        String pYear = album.getP_source();
        String correctPoem = album.getP_content();
        StringBuffer stringBuffer = new StringBuffer();
        currentText = stringBuffer.append(pTitle)
                .append("，")
                .append(pAuthor)
                .append("，")
                .append(pYear)
                .append("，")
                .append(correctPoem).toString();
        String correctPoem2 = correctPoem.replaceAll("[，。、？！!,.?]", "\r\n");
        correctPoem2 = correctPoem2.replaceAll("<br>","");
        Log.d("xiang", "onAlbumLoaded: album "+album.getP_author());
        tv_poem_title.setText(pTitle);
        tv_title.setText(pTitle);
        Log.d("xiang", "onAlbumLoaded: "+album.getP_name());
        tv_poem_author.setText(pAuthor);
        tv_poem_year.setText(pYear);
        tv_poem_content.setText(correctPoem2);

    }
    @Override
    public void onAlbumLoaded(PoetryHistory album) {

    }

    private void showTip(String message) {
//       ToastUtil.showSingleToast(message);
    }

}
