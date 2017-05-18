package com.example.justfortest.volume;

import android.app.Service;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import com.example.justfortest.R;
import com.tuyou.tsd.common.android.media.VolumeUtils;
import com.tuyou.tsd.common.base.BaseActivity;

/**
 * Created by justi on 2017/5/15.
 */

public class VolumeActivity extends BaseActivity implements View.OnClickListener {


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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_volume:
                //弹出音量控制
                VolumeUtils.showVolumeUI(this);
                break;
            case R.id.btn_volume_add:
                // 系统音量
//                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
//                VolumeUtils.addMasterVolume(this, true);
                break;
            case R.id.btn_volume_subtract:
//                VolumeUtils.subtractMasterVolume(this,true);
            default:
                break;

        }

    }
}
