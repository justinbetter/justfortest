package com.example.justfortest.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Base64;

import com.tuyou.tsd.common.util.FileUtils;
import com.tuyou.tsd.common.util.L;
import com.tuyou.tsd.common.util.MD5;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import rx.Subscription;

public class CommonUtils {

    public static boolean validFile(String filename, long fileSize, String checksum) {
        boolean ret = false;

        // 文件不存在,或者大小不对
        long realFileSize = FileUtils.getFileSize(filename);
        if (realFileSize == -1) {
            return ret;
        }

        // 计算md5
        String md5 = getMD5String(filename);
        if (TextUtils.isEmpty(md5)) {
            return ret;
        }

        // 文件大小和md5同时满足
        if (fileSize == realFileSize && md5.equals(checksum)) {
            ret = true;
        }

        return ret;
    }

    private static String getMD5String(String filename) {
        try {
            return MD5.md5_file(filename);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }


    public static String getFromAssets(Context context, String fileName) {
        String result = null;
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int length = in.available();
            //创建byte数组
            byte[] buffer = new byte[length];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long getExtSDAvailableSize() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().toString());
        long blockSize = stat.getBlockSizeLong();
        long availCount = stat.getAvailableBlocksLong();
        return blockSize * availCount;
    }

    public static void printThread(String prefix) {
        L.i("------------------------------------------------");
        Thread current = Thread.currentThread();
        L.i(current.toString());
    }


    public static boolean isMobileNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connMgr.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isAvailable() && wifiInfo.isConnected();
    }

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo r : serviceList) {
            if (r.service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = null;
        }
    }


}


