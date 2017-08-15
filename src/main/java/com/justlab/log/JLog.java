package com.justlab.log;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by justi on 2017/7/5.
 */


public class JLog {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NULL_TIPS = "Log with null object";
    private static final String DEFAULT_MESSAGE = "execute";
    private static final String PARAM = "Param";
    private static final String NULL = "null";
    private static final String TAG_DEFAULT = "L";
    private static final String SUFFIX = ".java";
    public static final int JSON_INDENT = 4;
    public static final int V = 1;
    public static final int D = 2;
    public static final int I = 3;
    public static final int W = 4;
    public static final int E = 5;
    public static final int A = 6;
    private static final int JSON = 7;
    private static final int XML = 8;
    private static final int STACK_TRACE_INDEX = 5;
    private static String mGlobalTag;
    private static boolean mIsGlobalTagEmpty = true;
    private static boolean IS_SHOW_LOG = true;

    public JLog() {
    }

    public static void init(boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
    }

    public static void init(boolean isShowLog, @Nullable String tag) {
        IS_SHOW_LOG = isShowLog;
        mGlobalTag = tag;
        mIsGlobalTagEmpty = TextUtils.isEmpty(mGlobalTag);
    }

    public static void v() {
        printLog(1, (String)null, new Object[]{"execute"});
    }

    public static void v(Object msg) {
        printLog(1, (String)null, new Object[]{msg});
    }

    public static void v(String tag, Object... objects) {
        printLog(1, tag, objects);
    }

    public static void d() {
        printLog(2, (String)null, new Object[]{"execute"});
    }

    public static void d(Object msg) {
        printLog(2, (String)null, new Object[]{msg});
    }

//    public static void d(String tag, Object... objects) {
//        printLog(2, tag, objects);
//    }

    public static void i() {
        printLog(3, (String)null, new Object[]{"execute"});
    }

    public static void i(Object msg) {
        printLog(3, (String)null, new Object[]{msg});
    }

//    public static void i(String tag, Object... objects) {
//        printLog(3, tag, objects);
//    }

    public static void w() {
        printLog(4, (String)null, new Object[]{"execute"});
    }

    public static void w(Object msg) {
        printLog(4, (String)null, new Object[]{msg});
    }

//    public static void w(String tag, Object... objects) {
//        printLog(4, tag, objects);
//    }

    public static void e() {
        printLog(5, (String)null, new Object[]{"execute"});
    }

    public static void e(Object msg) {
        printLog(5, (String)null, new Object[]{msg});
    }

//    public static void e(String tag, Object... objects) {
//        printLog(5, tag, objects);
//    }

    public static void a() {
        printLog(6, (String)null, new Object[]{"execute"});
    }

    public static void a(Object msg) {
        printLog(6, (String)null, new Object[]{msg});
    }

//    public static void a(String tag, Object... objects) {
//        printLog(6, tag, objects);
//    }

    public static void json(String jsonFormat) {
        printLog(7, (String)null, new Object[]{jsonFormat});
    }

//    public static void json(String tag, String jsonFormat) {
//        printLog(7, tag, new Object[]{jsonFormat});
//    }

    public static void xml(String xml) {
        printLog(8, (String)null, new Object[]{xml});
    }

    public static void xml(String tag, String xml) {
        printLog(8, tag, new Object[]{xml});
    }

    public static void file(File targetDirectory, Object msg) {
        printFile((String)null, targetDirectory, (String)null, msg);
    }

    public static void file(String tag, File targetDirectory, Object msg) {
        printFile(tag, targetDirectory, (String)null, msg);
    }

    public static void file(String tag, File targetDirectory, String fileName, Object msg) {
        printFile(tag, targetDirectory, fileName, msg);
    }

    private static void printLog(int type, String tagStr, Object... objects) {
        if(IS_SHOW_LOG) {
            String[] contents = wrapperContent(tagStr, objects);
            String tag = contents[0];
            String msg = contents[1];
            String headString = contents[2];
            switch(type) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    BaseLog.printDefault(type, tag, headString + msg);
                    break;
                case 7:
                    JsonLog.printJson(tag, msg, headString);
                    break;
                case 8:
                    XmlLog.printXml(tag, msg, headString);
            }

        }
    }

    private static void printFile(String tagStr, File targetDirectory, String fileName, Object objectMsg) {
        if(IS_SHOW_LOG) {
            String[] contents = wrapperContent(tagStr, new Object[]{objectMsg});
            String tag = contents[0];
            String msg = contents[1];
            String headString = contents[2];
            FileLog.printFile(tag, targetDirectory, fileName, headString, msg);
        }
    }

    private static String[] wrapperContent(String tagStr, Object... objects) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[5];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if(classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + ".java";
        }

        if(className.contains("$")) {
            className = className.split("\\$")[0] + ".java";
        }

        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();
        if(lineNumber < 0) {
            lineNumber = 0;
        }

        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        String tag = tagStr == null?className:tagStr;
        if(mIsGlobalTagEmpty && TextUtils.isEmpty(tag)) {
            tag = "L";
        } else if(!mIsGlobalTagEmpty) {
            tag = mGlobalTag;
        }

        String msg = objects == null?"Log with null object":getObjectsString(objects);
        String headString = "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";
        return new String[]{tag, msg, headString};
    }

    private static String getObjectsString(Object... objects) {
        if(objects.length > 1) {
            StringBuilder var4 = new StringBuilder();
            var4.append("\n");

            for(int i = 0; i < objects.length; ++i) {
                Object object1 = objects[i];
                if(object1 == null) {
                    var4.append("Param").append("[").append(i).append("]").append(" = ").append("null").append("\n");
                } else {
                    var4.append("Param").append("[").append(i).append("]").append(" = ").append(object1.toString()).append("\n");
                }
            }

            return var4.toString();
        } else {
            Object object = objects[0];
            return object == null?"null":object.toString();
        }
    }
}
