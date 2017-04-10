package com.example.justfortest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.tuyou.tsd.common.base.BaseService;
import com.tuyou.tsd.common.util.PackageManagerReflectUtils;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by justi on 2017/4/6.
 */

public class BsdiffFakeService extends BaseService {

    private String TAG = "justin";
    public static final String DO_COMPOSE = "do_compose";
    public static final String APK_RESPONSE = "apk_response";
    public static final String APK_INSTALL = "apk_install";
    private String NEW_APK_PARENT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/testpatch/";
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private int totalPatchCount = -1;
    private static final String[][] patch_config = {
            //pacakge,patch,newApk
            {"com.tuyou.tsd.collector", "collector.patch", "collector.apk"},
            {"com.tuyou.tsd.navigation", "navigation.patch", "navigation.apk"},
            {"com.tuyou.tsd.audio", "audio.patch", "audio.apk"},
            {"com.tuyou.tsd.podcast", "podcast.patch", "podcast.apk"},
            {"com.tuyou.tsd.voice", "voice.patch", "voice.apk"},
            {"com.tuyou.tsd.cardvr", "cardvr.patch", "cardvr.apk"},
            {"com.tuyou.tsd.settings", "settings.patch", "settings.apk"},

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    /*
        doPatch 发送patch请求
        apk 各自合成newApk，返回成功响应，都成功，通知安装;否则删除newApk
        成功弹个toast
     */

    @Override
    public void handleMessage(Message msg) {
        Intent intent = (Intent) msg.obj;
        String action = intent.getAction();
        if (action.equals(DO_COMPOSE)) {
            //合成newapk
//            FileUtils.prepareDir(NEW_APK_PARENT_PATH);
//
//            for (String[] config : patch_config) {
//                String packageName = config[0];
//                File patch = new File(NEW_APK_PARENT_PATH, config[1]);
//                File newApk = new File(NEW_APK_PARENT_PATH, config[2]);
//                if (patch.exists()) {
//                    Log.i(TAG, "doPatch： " + packageName + " " + patch.getAbsolutePath() + " " + newApk.getAbsolutePath());
//                    BspatchEngine.runPatch(getApplicationContext(), packageName, patch.getAbsolutePath(), newApk.getAbsolutePath());
//                } else {
//                    Log.i(TAG, "patch not exist！");
//                }
//            }
            //加载patch包
            for (String[] config : patch_config) {
                try {
                    final String packageName = config[0];
                    final File patch = new File(NEW_APK_PARENT_PATH, config[1]);
                    final File newApk = new File(NEW_APK_PARENT_PATH, config[2]);
                    PackageManager packageManager = getApplicationContext().getPackageManager();
                    final String oldApkPath = packageManager.getApplicationInfo(packageName, 0).sourceDir;
                    if (patch.exists()) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "doPatch： " + packageName + " " + oldApkPath + " " + patch.getAbsolutePath() + " " + newApk.getAbsolutePath());
                                patchNewApk(packageName, oldApkPath, newApk.getAbsolutePath(), patch.getAbsolutePath());
                            }
                        });
                    } else {
                        Log.i(TAG, "patch not exist！");
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
            totalPatchCount = patch_config.length;

        } else if (action.equals(APK_RESPONSE)) {
            //apk数量-1 patch_config.length
            if (totalPatchCount != -1) {
                totalPatchCount--;
                int result = intent.getIntExtra("result", -1);
                String packageName = intent.getStringExtra("packageName");
                Log.i(TAG, "result: " + result + "pkg: " + packageName);
                if (totalPatchCount == 0) {
                    Message message = Message.obtain(workHandler.getThreadHandler(), Integer.MIN_VALUE);
                    Intent install_intent = new Intent(APK_INSTALL);
                    message.obj = install_intent;
                    workHandler.sendMessage(message);

                }

            } else {
                Log.e(TAG, "no patch?");
            }
            //发送安装

            //超时，删除目录


        } else if (action.equals(APK_INSTALL)) {
            //安装newApk
            for (String[] config : patch_config) {
                File newApk = new File(NEW_APK_PARENT_PATH, config[2]);
                if (newApk.exists()) {
                    installApk(newApk);
//                    install(getApplicationContext(), newApk.getAbsolutePath());
                } else {
                    Log.e(TAG, "what fuck? newApk not existed?");
                }
            }

        }

    }

    private void patchNewApk(String packageName, String oldApkPath, String newApkPath, String patchPath) {
        File patchFile = new File(patchPath);
        long patchTime = 0;
        long startTime = 0;

        Log.i(TAG, "load patch" + patchFile);
        startTime = System.currentTimeMillis();
        int bspatch = BspatchUtils.bspatch(oldApkPath, newApkPath, patchPath);
        patchTime = System.currentTimeMillis();
        Log.e("justin", "patch result: " + bspatch + "\n" + "patch time: " + (patchTime - startTime));
        postPatchResult(packageName, bspatch);
    }


    private void postPatchResult(String pkgName, int isPatchSuccess) {
        Message message = Message.obtain(workHandler.getThreadHandler(), Integer.MIN_VALUE);
        Intent install_intent = new Intent(APK_RESPONSE);
        install_intent.putExtra("packageName", pkgName);
        install_intent.putExtra("result", isPatchSuccess);
        message.obj = install_intent;
        workHandler.sendMessage(message);
    }

    public void installApk(File newApk) {
        if (newApk.exists()) {
            installSilently(newApk);
        } else {
            Log.e("justin", "newApk not existed");
        }

    }

    abstract class PackageInstallObserver extends IPackageInstallObserver.Stub {
    }

    private void installSilently(File newApk) {
        final long installTime = System.currentTimeMillis();

        PackageManagerReflectUtils.installPackage(getPackageManager(), Uri.fromFile(newApk),
                new PackageInstallObserver() {
                    @Override
                    public void packageInstalled(String packageName, int returnCode) throws RemoteException {
                        Log.w(TAG, "packageInstalled:" + packageName + " returnCode=" + returnCode);
                        long endTime = System.currentTimeMillis();
                        Log.e("justin", "install time " + (endTime - installTime) + "\n");

                    }

                }, 0x00000002, getPackageName());
    }

    private void install(Context context, String apkPath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        context.startActivity(i);
    }
}
