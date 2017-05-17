package com.example.justfortest.volume;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.justfortest.R;
import com.tuyou.tsd.common.android.media.AudioManagerCompat;
import com.tuyou.tsd.common.util.L;

import java.util.HashMap;

public class ShowVolumeActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static int MSG_DISMISS_VOLUME = 11;
    public static int MASTER_STREAM = 100;

    private ViewGroup mPanel;
    private View mView;
    private ViewGroup mSliderGroup;
    private ImageView mMoreButton;
    private ImageView mDivider;
    private HashMap<Integer, StreamControl> mStreamControls;
    private int mActiveStreamType;
    private AudioManagerCompat audioManagerCompat;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                int flag = msg.what;
                if (MSG_DISMISS_VOLUME == flag) {
                    finish();
                }
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //更新
        final Object tag = seekBar.getTag();
        if (fromUser && tag instanceof StreamControl) {
            audioManagerCompat.setMasterMute(false, 0, ShowVolumeActivity.this);
            StreamControl sc = (StreamControl) tag;
            if (audioManagerCompat.getMasterStream() != progress) {
                audioManagerCompat.setMasterVolume(progress, 0, ShowVolumeActivity.this);
                if (progress == 0) {
                    audioManagerCompat.setMasterMute(true, 0, ShowVolumeActivity.this);
                } else {
                    audioManagerCompat.setMasterMute(false, 0, ShowVolumeActivity.this);

                }
                updateSeekBar(MASTER_STREAM);
            }
        }
        resetTimeout();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private enum StreamResources {
        MasterStream(MASTER_STREAM,
                R.string.volume_icon_description_media,
                R.drawable.ic_audio_vol,
                R.drawable.ic_audio_vol_mute,
                true);

        int streamType;
        int descRes;
        int iconRes;
        int iconMuteRes;
        // RING, VOICE_CALL & BLUETOOTH_SCO are hidden unless explicitly requested
        boolean show;

        StreamResources(int streamType, int descRes, int iconRes, int iconMuteRes, boolean show) {
            this.streamType = streamType;
            this.descRes = descRes;
            this.iconRes = iconRes;
            this.iconMuteRes = iconMuteRes;
            this.show = show;
        }
    }

    ;

    // List of stream types and their order
    private static final StreamResources[] STREAMS = {
            StreamResources.MasterStream,
    };

    private class StreamControl {
        int streamType;
        ViewGroup group;
        ImageView icon;
        SeekBar seekbarView;
        int iconRes;
        int iconMuteRes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("justin", "ShowVolumeActivity onCreate");
        sendBroadcast(new Intent(VolumeService.BROADCASR_ACTION_IS_VOLUME_SHOW).putExtra("volume", true));
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.verticalMargin = 0.1f;
        lp.gravity = Gravity.TOP;//设置对话框置顶显示
        window.setAttributes(lp);
        audioManagerCompat = AudioManagerCompat.getInstance();
        //初始化音量模块
        createSliders();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e("justin", "ShowVolumeActivity onDestroy");
        sendBroadcast(new Intent(VolumeService.BROADCASR_ACTION_IS_VOLUME_SHOW).putExtra("volume", false));
    }

    private void createSliders() {
        //初始化音量UI
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.volume_adjust, null);
        mPanel = (ViewGroup) mView.findViewById(R.id.visible_panel);
        mSliderGroup = (ViewGroup) mView.findViewById(R.id.slider_group);
        mDivider = (ImageView) mView.findViewById(R.id.expand_button_divider);
        mView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                resetTimeout();
                return false;
            }
        });
        setContentView(mView);
        //初始化音量类型
        mStreamControls = new HashMap<Integer, StreamControl>(STREAMS.length);
        Resources res = this.getResources();
        for (int i = 0; i < STREAMS.length; i++) {
            StreamResources streamRes = STREAMS[i];
            int streamType = streamRes.streamType;
            StreamControl sc = new StreamControl();
            sc.streamType = streamType;
            sc.group = (ViewGroup) inflater.inflate(R.layout.volume_adjust_item, null);
            sc.group.setTag(sc);
            sc.icon = (ImageView) sc.group.findViewById(R.id.stream_icon);
            sc.icon.setOnClickListener(this);
            sc.icon.setTag(sc);
            sc.icon.setContentDescription(res.getString(streamRes.descRes));
            sc.iconRes = streamRes.iconRes;
            sc.iconMuteRes = streamRes.iconMuteRes;
            sc.icon.setImageResource(sc.iconRes);
            sc.seekbarView = (SeekBar) sc.group.findViewById(R.id.seekbar);
            sc.seekbarView.setMax(audioManagerCompat.getMasterMaxVolume());
            sc.seekbarView.setOnSeekBarChangeListener(this);
            sc.seekbarView.setTag(sc);
            mStreamControls.put(streamType, sc);
        }
        showVolume(MASTER_STREAM);
    }

    private void showVolume(int streamType) {
        L.e("justin", "show dialog");
        mSliderGroup.removeAllViews();
        StreamControl active = mStreamControls.get(streamType);
        if (active == null) {
            Log.e("VolumePanel", "Missing stream type! - " + streamType);
            mActiveStreamType = -1;
        } else {
            mSliderGroup.addView(active.group);
            mActiveStreamType = streamType;
            active.group.setVisibility(View.VISIBLE);
        }

        updateSeekBar(streamType);
        resetTimeout();
    }

    /**
     * 更新progress & icon
     *
     * @param streamType
     */
    private void updateSeekBar(int streamType) {
        int index = audioManagerCompat.getMasterStream();
        int max = audioManagerCompat.getMasterMaxVolume();

        L.e("justin", index + " / " + max);
        StreamControl sc = mStreamControls.get(streamType);
        if (sc != null) {
            if (sc.seekbarView.getMax() != max) {
                sc.seekbarView.setMax(max);
            }
            sc.icon.setImageResource(audioManagerCompat.isMasterMute() ? sc.iconMuteRes : sc.iconRes);
            sc.seekbarView.setProgress(index);
            sc.seekbarView.setEnabled(true);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stream_icon:
                audioManagerCompat.setMasterMute(!audioManagerCompat.isMasterMute(), 0, ShowVolumeActivity.this);
                updateSeekBar(MASTER_STREAM);
                break;
            default:
                break;
        }
    }

    /**
     * 定时消失
     */
    private void resetTimeout() {
        L.e("justin", "resetTimeout");
        mHandler.removeMessages(MSG_DISMISS_VOLUME);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_DISMISS_VOLUME), 5000);
    }


}
