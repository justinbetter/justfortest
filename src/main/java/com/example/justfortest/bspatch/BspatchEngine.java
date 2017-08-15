package com.example.justfortest.bspatch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by justi on 2017/3/31.
 */

public class BspatchEngine {

    public static void postPatchResult(Context context, int isPatchSuccess, @Nullable String reason) {
        Intent intent = new Intent();
        intent.setClassName("com.example.justfortest", "com.example.justfortest.BsdiffFakeService");
        intent.setAction("apk_response");
        intent.putExtra("result", isPatchSuccess);
        intent.putExtra("packageName", context.getPackageName());
        intent.putExtra("reason", reason == null ? "unknown" : reason);
        context.startService(intent);

    }

    public static void runPatch(Context context, String packageName,String patchPath, String newApkPath) {
        Intent intent = new Intent(BspatchService.BSPATCH_REQUEST);
        intent.putExtra(BspatchService.BSPATCH_PATH, patchPath);
        intent.putExtra(BspatchService.BSPATCH_NEWAPK, newApkPath);
        intent.setClassName(packageName, BspatchService.class.getName());
        context.startService(intent);
    }

}
