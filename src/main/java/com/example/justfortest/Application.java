package com.example.justfortest;

import com.justlab.log.JLog;
import com.tuyou.tsd.common.util.L;

/**
 * Created by justi on 2017/4/25.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        L.init(getApplicationContext());
        JLog.init(true,"justin");
    }
}
