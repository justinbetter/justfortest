package com.example.justfortest.bspatch;

import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.example.justfortest.bsdiff.BspatchUtils;
import com.tuyou.tsd.common.base.BaseService;
import com.tuyou.tsd.common.util.L;

import java.io.File;

/**
 * Created by justi on 2017/3/31.
 */

public class BspatchService extends BaseService {

    private String TAG = this.getClass().getSimpleName();
    public static String BSPATCH_REQUEST = "bspatch_request";
    public static String BSPATCH_PATH = "bspatch_path";
    public static String BSPATCH_NEWAPK = "bspatch_newapk";


    @Override
    public void handleMessage(Message msg) {
        Intent intent = (Intent) msg.obj;
        String action = intent.getAction();
        if (action.equals(BSPATCH_REQUEST)) {
            //加载patch包
            String oldApkPath = getApplicationContext().getApplicationInfo().sourceDir;
            String newApkPath = intent.getStringExtra(BSPATCH_NEWAPK);

            String patchPath = intent.getStringExtra(BSPATCH_PATH);
            File patchFile = new File(patchPath);
            long patchTime = 0;
            long startTime = 0;

            L.i(TAG, "load patch" + patchFile);
            startTime = System.currentTimeMillis();
            int bspatch = BspatchUtils.bspatch(oldApkPath, newApkPath, patchPath);
            BspatchEngine.postPatchResult(getApplicationContext(), bspatch, null);
            patchTime = System.currentTimeMillis();
            Log.e("justin", "patch result: " + bspatch + "\n" + "patch time: " + (patchTime - startTime));

        }


    }
}



