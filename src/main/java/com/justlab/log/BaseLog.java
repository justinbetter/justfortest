package com.justlab.log;

import android.os.Looper;
import android.util.Log;

/**
 * Created by justi on 2017/7/5.
 */

public class BaseLog {
    public BaseLog() {
    }

    public static void printDefault(int type, String tag, String msg) {
        int index = 0;
        short maxLength = 4000;
        int countOfSub = msg.length() / maxLength;
        if(countOfSub > 0) {
            for(int i = 0; i < countOfSub; ++i) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }

            printSub(type, tag, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }

    }

    private static void printSub(int type, String tag, String sub) {
        String thread = Looper.myLooper() == Looper.getMainLooper()?" (main thread) ":" (NOT main thread) ";
        sub = sub + thread;
        switch(type) {
            case 1:
                Log.v(tag, sub);
                break;
            case 2:
                Log.d(tag, sub);
                break;
            case 3:
                Log.i(tag, sub);
                break;
            case 4:
                Log.w(tag, sub);
                break;
            case 5:
                Log.e(tag, sub);
                break;
            case 6:
                Log.wtf(tag, sub);
        }

    }
}
