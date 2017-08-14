package com.justlab.log;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by justi on 2017/7/5.
 */

public class FileLog {
    public FileLog() {
    }

    public static void printFile(String tag, File targetDirectory, String fileName, String headString, String msg) {
        fileName = fileName == null?getFileName():fileName;
        if(save(targetDirectory, fileName, msg)) {
            Log.d(tag, headString + " save log success ! location is >>>" + targetDirectory.getAbsolutePath() + "/" + fileName);
        } else {
            Log.e(tag, headString + "save log fails !");
        }

    }

    private static boolean save(File dic, String fileName, String msg) {
        File file = new File(dic, fileName);

        try {
            FileOutputStream e = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(e, "UTF-8");
            outputStreamWriter.write(msg);
            outputStreamWriter.flush();
            e.close();
            return true;
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
            return false;
        } catch (IOException var8) {
            var8.printStackTrace();
            return false;
        } catch (Exception var9) {
            var9.printStackTrace();
            return false;
        }
    }

    private static String getFileName() {
        Random random = new Random();
        return "KLog_" + Long.toString(System.currentTimeMillis() + (long)random.nextInt(10000)).substring(4) + ".txt";
    }
}
