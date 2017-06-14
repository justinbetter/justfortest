package com.example.justfortest.volume;

import android.app.Service;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import com.example.justfortest.R;
import com.example.justfortest.utils.RxUtils;
import com.tuyou.tsd.common.android.app.power.ScreenSaverHelper;
import com.tuyou.tsd.common.base.BaseActivity;

import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by justi on 2017/5/15.
 */

public class VolumeActivity extends BaseActivity implements View.OnClickListener {


    private AudioManager mAudioManager;
    private PowerManager.WakeLock wakeLock;

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


//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
//                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
//                        | PowerManager.ON_AFTER_RELEASE
//                , "VolumeActivity");
//        wakeLock.acquire();


    }

    @Override
    protected void onResume() {
        super.onResume();
        RxUtils.countDown(10)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        Log.e("justin", "count: "+integer.toString());
                        return integer == 0;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        ScreenSaverHelper.closeScreenSaver(VolumeActivity.this);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        wakeLock.release();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_volume:
                //弹出音量控制
//                VolumeUtils.showVolumeUI(this);
                ScreenSaverHelper.openScreenSaver(VolumeActivity.this);
                break;
            case R.id.btn_volume_add:
                // 系统音量
                ScreenSaverHelper.closeScreenSaver(VolumeActivity.this);
                break;
            case R.id.btn_volume_subtract:
//                VolumeUtils.subtractMasterVolume(this,true);
            default:
                break;

        }

    }
}
