package com.example.justfortest.rom;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tuyou.tsd.common.util.FileUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by tuyou on 16/7/18.
 */

public class Mock {
    private static Context _context;

    private static class LazyHolder {
        private static final Mock INSTANCE = new Mock(_context);
    }

    private Mock(Context context) {
        Log.i("justin","mock load");
        String debug_file = Environment.getExternalStorageDirectory().toString() + File.separator + "debug_ota";
        if (FileUtils.isFileExist(debug_file)) {
            String debugJson = FileUtils.readFile(Environment.getExternalStorageDirectory().toString() + File.separator + "debug_ota", "utf8").toString();
            if (!TextUtils.isEmpty(debugJson)) {
                s = new Gson().fromJson(debugJson, _S.class);
            }
        }
    }

    public static Mock getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static final String REAL = "real";
    public static final String FAKE = "fake";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public static class _Debug {
        public String scene;    // "real,fake"
        public String fake_ret; // "success, fail"
    }

    public static class _S {
        public String version;
        public String deviceid;
        public String tts;
        public String reboot;
        public HashMap<String, _Debug> maps;
    }

    private _S s;

    static public void init(Context context) {
        _context = context;
    }

    public _Debug getMockDebug(String name) {
        return s != null ? s.maps.get(name) : null;
    }

    public String getVersion() {
        return s != null && !TextUtils.isEmpty(s.version) ? s.version : null;
    }

    public String getDeviceId() {
        return s != null && !TextUtils.isEmpty(s.deviceid) ? s.deviceid : null;
    }

    public String getTTS() {
        return s != null && !TextUtils.isEmpty(s.tts) ? s.tts : null;
    }

    public String getReboot() {
        return s != null && !TextUtils.isEmpty(s.reboot) ? s.reboot : null;
    }

}
