package com.example.justfortest.volume;

import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
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
import com.tuyou.tsd.common.util.L;

import java.util.HashMap;

/**
 * Created by justi on 2017/5/16.
 */

public class VolumeController extends Handler implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    public static int MSG_SHOW_VOLUME = 10;
    public static int MSG_DISMISS_VOLUME = 11;


    private AudioManager mAudioManager;
    private ViewGroup mPanel;
    private View mView;
    private ViewGroup mSliderGroup;
    private ImageView mMoreButton;
    private ImageView mDivider;
    private Dialog mDialog;
    private HashMap<Integer, StreamControl> mStreamControls;
    private final Context mContext;
    private int mActiveStreamType;


    private enum StreamResources {
        SystemStream(AudioManager.STREAM_SYSTEM,
                R.string.volume_icon_description_media,
                R.drawable.ic_audio_vol,
                R.drawable.ic_audio_vol_mute,
                true),
        RingerStream(AudioManager.STREAM_RING,
                R.string.volume_icon_description_ringer,
                R.drawable.ic_audio_ring_notif,
                R.drawable.ic_audio_ring_notif_mute,
                false),
        VoiceStream(AudioManager.STREAM_VOICE_CALL,
                R.string.volume_icon_description_incall,
                R.drawable.ic_audio_phone,
                R.drawable.ic_audio_phone_am,
                false),
        AlarmStream(AudioManager.STREAM_ALARM,
                R.string.volume_alarm,
                R.drawable.ic_audio_alarm,
                R.drawable.ic_audio_alarm_mute,
                false),
        MediaStream(AudioManager.STREAM_MUSIC,
                R.string.volume_icon_description_media,
                R.drawable.ic_audio_vol,
                R.drawable.ic_audio_vol_mute,
                true),
        NotificationStream(AudioManager.STREAM_NOTIFICATION,
                R.string.volume_icon_description_notification,
                R.drawable.ic_audio_notification,
                R.drawable.ic_audio_notification_mute,
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
            StreamResources.SystemStream,
            StreamResources.RingerStream,
            StreamResources.VoiceStream,
            StreamResources.MediaStream,
            StreamResources.NotificationStream,
            StreamResources.AlarmStream,
    };

    private class StreamControl {
        int streamType;
        ViewGroup group;
        ImageView icon;
        SeekBar seekbarView;
        int iconRes;
        int iconMuteRes;
    }

    public VolumeController(Context context) {
        mContext = context;
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mContext.getSystemService(Service.AUDIO_SERVICE);
        }
        //初始化音量模块
        createSliders();
        //register broadcaster
        listenToVolumeChange();

    }

    private void listenToVolumeChange() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        mContext.registerReceiver(new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                //如果音量发生变化则更改seekbar的位置
                if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                    L.e("justin","volume change");
                    if (!mDialog.isShowing()) {
                        sendMessage(obtainMessage(MSG_SHOW_VOLUME));
                    }

                }

            }
        }, filter);
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg != null) {
            int flag = msg.what;
            if (MSG_SHOW_VOLUME == flag) {
                //显示UI
                showVolume(AudioManager.STREAM_SYSTEM);
            } else if (MSG_DISMISS_VOLUME == flag ) {
                hideVolume();
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stream_icon:
                break;
            default:
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //更新
        final Object tag = seekBar.getTag();
        if (fromUser && tag instanceof StreamControl) {
            StreamControl sc = (StreamControl) tag;
            if (mAudioManager.getStreamVolume(sc.streamType) != progress) {
                mAudioManager.setStreamVolume(sc.streamType, progress, 0);
                sc.icon.setImageResource(progress == 0 ? sc.iconMuteRes : sc.iconRes);
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


    /**
     * show
     */
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


        int index = mAudioManager.getStreamVolume(streamType);
        int max = mAudioManager.getStreamMaxVolume(streamType);
        L.e("justin", index + " / " + max);
        StreamControl sc = mStreamControls.get(streamType);
        if (sc != null) {
            if (sc.seekbarView.getMax() != max) {
                sc.seekbarView.setMax(max);
            }
            sc.icon.setImageResource(index == 0 ? sc.iconMuteRes : sc.iconRes);
            sc.seekbarView.setProgress(index);
            sc.seekbarView.setEnabled(true);
        }

        if (!mDialog.isShowing()) {
            // when the stream is for remote playback, use -1 to reset the stream type evaluation
            // Showing dialog - use collapsed state
            mDialog.show();

        }
        resetTimeout();

    }

    private void hideVolume() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }

    }

    private void createSliders() {
        //初始化音量UI
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        mDialog = new Dialog(mContext, R.style.Theme_Panel_Volume);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        mDialog.setTitle("Volume control"); // No need to localize
        mDialog.setContentView(mView);

        // Change some window properties
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.token = null;
        // Offset from the top
        lp.y = mContext.getResources().getDimensionPixelOffset(R.dimen.volume_panel_top);
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        //初始化音量类型
        mStreamControls = new HashMap<Integer, StreamControl>(STREAMS.length);
        Resources res = mContext.getResources();
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
            sc.seekbarView.setMax(mAudioManager.getStreamMaxVolume(streamType));
            sc.seekbarView.setOnSeekBarChangeListener(this);
            sc.seekbarView.setTag(sc);
            mStreamControls.put(streamType, sc);
        }


    }

    /**
     * 定时消失
     */
    private void resetTimeout() {
        L.e("justin", "resetTimeout");
        removeMessages(MSG_DISMISS_VOLUME);
        sendMessageDelayed(obtainMessage(MSG_DISMISS_VOLUME), 3000);
    }

    private void forceTimeout() {
        L.e("justin", "forceTimeout");
        removeMessages(MSG_DISMISS_VOLUME);
        sendMessage(obtainMessage(MSG_DISMISS_VOLUME));
    }


}
