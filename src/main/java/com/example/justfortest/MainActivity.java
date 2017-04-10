package com.example.justfortest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    private String TAG = "justin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doPatch(View view) {
        Log.i(TAG, "doPatch");
        Intent service = new Intent("do_compose");
        service.setClassName(getPackageName(), "com.example.justfortest.BsdiffFakeService");
        startService(service);

//        for (String[] config : patch_config) {
//            String packageName = config[0];
//            File patch = new File(Environment.getExternalStorageDirectory(), config[1]);
//            File newApk = new File(Environment.getExternalStorageDirectory(), config[2]);
//            if (patch.exists()) {
//                BspatchEngine.runPatch(getApplicationContext(), packageName, patch.getAbsolutePath(), newApk.getAbsolutePath());
//            }
//        }

//        String oldApkPath = getApplicationContext().getApplicationInfo().sourceDir;
//        Log.i("justin", oldApkPath);
//        final File newApk = new File(Environment.getExternalStorageDirectory(), "new2.apk");
//        final File patch = new File(Environment.getExternalStorageDirectory(), "patch2.patch");
//        if (patch.exists()) {
//            BspatchEngine.runPatch(getApplicationContext(), getPackageName(), patch.getAbsolutePath(), newApk.getAbsolutePath());
//        }

    }


}
