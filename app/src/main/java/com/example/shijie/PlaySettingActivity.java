package com.example.shijie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.example.shijie.utils.SettingTextWatcher;

public class PlaySettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String PREFER_NAME = "com.iflytek.setting";
    private EditTextPreference mSpeedPreference;
    private EditTextPreference mPitchPreference;
    private EditTextPreference mVolumePreference;
    private ListPreference mStreamPreference;
    private ListPreference mPersonPreference;

    private String[] mStreams;
    private String[] mPersons ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mStreams = getResources().getStringArray(R.array.stream_entries);
        mPersons = getResources().getStringArray(R.array.voicer_cloud_entries);
        // 指定保存文件名字
        getPreferenceManager().setSharedPreferencesName(PREFER_NAME);
        addPreferencesFromResource(R.xml.tts_setting);
        mSpeedPreference = (EditTextPreference)findPreference("speed_preference");
        mSpeedPreference.getEditText().addTextChangedListener(new SettingTextWatcher(this,mSpeedPreference,0,200));
        mPitchPreference = (EditTextPreference)findPreference("pitch_preference");
        mPitchPreference.getEditText().addTextChangedListener(new SettingTextWatcher(this,mPitchPreference,0,100));
        mVolumePreference = (EditTextPreference)findPreference("volume_preference");
        mVolumePreference.getEditText().addTextChangedListener(new SettingTextWatcher(this,mVolumePreference,0,100));
        mStreamPreference = (ListPreference) findPreference("stream_preference");
        mPersonPreference = (ListPreference) findPreference("person_preference");

        Log.d("tiao", "onCreate: play"+"到了");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Setup the initial values
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        mSpeedPreference.setSummary(sharedPreferences.getString("speed_preference", "50"));
        mPitchPreference.setSummary(sharedPreferences.getString("pitch_preference", "50"));
        mVolumePreference.setSummary(sharedPreferences.getString("volume_preference", "50"));
        mStreamPreference.setSummary(mStreams[Integer.valueOf(sharedPreferences.getString("stream_preference", "3"))]);
        mPersonPreference.setSummary(mPersons[Integer.valueOf(sharedPreferences.getString("person_preference", "0"))]);
        // Set up a listener whenever a key changes
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("speed_preference")) {
            mSpeedPreference.setSummary(sharedPreferences.getString(key, "50"));
        } else if(key.equals("pitch_preference")) {
            mPitchPreference.setSummary(sharedPreferences.getString(key, "50"));
        } else if(key.equals("volume_preference")){
            mVolumePreference.setSummary(sharedPreferences.getString(key, "50"));
        } else if("stream_preference".equals(key)){
            mStreamPreference.setSummary(mStreams[Integer.valueOf(sharedPreferences.getString("stream_preference", "3"))]);
        } else if("person_preference".equals(key)){
            mPersonPreference.setSummary(mPersons[Integer.valueOf(sharedPreferences.getString("person_preference", "0"))]);
        }
    }
}

