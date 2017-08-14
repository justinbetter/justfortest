package com.justlab.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by justi on 2017/7/5.
 */

public class JsonLog {
    public JsonLog() {
    }

    public static void printJson(String tag, String msg, String headString) {
        String message;
        try {
            if(msg.startsWith("{")) {
                JSONObject lines = new JSONObject(msg);
                message = lines.toString(4);
            } else if(msg.startsWith("[")) {
                JSONArray var10 = new JSONArray(msg);
                message = var10.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException var9) {
            message = msg;
        }

        Util.printLine(tag, true);
        message = headString + JLog.LINE_SEPARATOR + message;
        String[] var11 = message.split(JLog.LINE_SEPARATOR);
        String[] arr$ = var11;
        int len$ = var11.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String line = arr$[i$];
            Log.d(tag, "║ " + line);
        }

        Util.printLine(tag, false);
    }
}
