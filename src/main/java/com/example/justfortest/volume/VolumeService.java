package com.example.justfortest.volume;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.text.TextUtils;

import com.tuyou.tsd.common.TSDEvent;
import com.tuyou.tsd.common.base.BaseService;
import com.tuyou.tsd.common.util.L;

public class VolumeService extends BaseService {

    public static final String ACTION_SHOW_VOLUME = "ACTION_SHOW_VOLUME";
    public static final String BROADCASR_ACTION_IS_VOLUME_SHOW = "BROADCASR_ACTION_IS_VOLUME_SHOW";
    private boolean isVolumeShow = false;

    @Override
    public void onCreate() {
        super.onCreate();
        //register broadcaster
        listenToVolumeChange();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.obj != null) {
            Intent intent = (Intent) msg.obj;
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(ACTION_SHOW_VOLUME)) {
                    //弹出音量控制
                    showVolumeActivity();
                }else if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {
                    //TODO 暂时不做
                } else if (action.equals(BROADCASR_ACTION_IS_VOLUME_SHOW)) {
                    L.d("justin","broadcasr_action_is_volume_show");
                    isVolumeShow = intent.getBooleanExtra("volume", false);
                } else if (action.equals(TSDEvent.System.HARDKEY_VOLUME)) {
                    //弹出音量控制
                    if (!isVolumeShow) {
                        showVolumeActivity();
                    }
                }
            }
        }
    }


    private void showVolumeActivity() {
        Intent activityIntent = new Intent();
        activityIntent.setComponent(new ComponentName("com.example.justfortest", "com.example.justfortest.volume.ShowVolumeActivity"));
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);
    }

    private void listenToVolumeChange() {

        final IntentFilter filter = new IntentFilter();
//        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        filter.addAction(TSDEvent.System.HARDKEY_VOLUME);
        filter.addAction(BROADCASR_ACTION_IS_VOLUME_SHOW);
        registerReceiver(filter);
    }
}
