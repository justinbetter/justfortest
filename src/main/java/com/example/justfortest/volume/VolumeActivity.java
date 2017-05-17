package com.example.justfortest.volume;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import com.example.justfortest.R;

/**
 * Created by justi on 2017/5/15.
 */

public class VolumeActivity extends Activity implements View.OnClickListener {


    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);
        mAudioManager = (AudioManager) this.getSystemService(Service.AUDIO_SERVICE);
//        volumeController = new VolumeController(VolumeActivity.this);
        int[] btnIds = {R.id.btn_volume, R.id.btn_volume_add,R.id.btn_volume_subtract};
        for (int btnId : btnIds) {
            findViewById(btnId).setOnClickListener(this);
        }


//                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//                int max = mAudioManager
//                        .getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
//                int current = mAudioManager
//                        .getStreamVolume(AudioManager.STREAM_VOICE_CALL);
//                Log.d("VIOCE_CALL", "max : " + max + " current : " + current);
//
//                // 系统音量
//                max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
//                current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
//                Log.d("SYSTEM", "max : " + max + " current : " + current);
//
//                // 铃声音量
//                max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
//                current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
//                Log.d("RING", "max : " + max + " current : " + current);
//
//                // 音乐音量
//                max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//                current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                Log.d("MUSIC", "max : " + max + " current : " + current);
//
//                // 提示声音音量
//                max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
//                current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
//                Log.d("ALARM", "max : " + max + " current : " + current);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_volume:
                //弹出音量控制
//                volumeController.sendMessage(volumeController.obtainMessage(VolumeController.MSG_SHOW_VOLUME));
                Intent service = new Intent();
                service.setComponent(new ComponentName("com.example.justfortest", "com.example.justfortest.volume.VolumeService"));
                service.setAction(VolumeService.ACTION_SHOW_VOLUME);
                startService(service);
                break;
            case R.id.btn_volume_add:
                // 系统音量
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                break;
            case R.id.btn_volume_subtract:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            default:
                break;

        }

    }
}
