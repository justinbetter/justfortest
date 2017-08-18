package com.example.justfortest.ota

import android.content.Context
import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/8/15.
 */
abstract class BaseOTAInstaller(var context: Context, var configureForOTA: ConfigureForOTA = ConfigureForOTA(), var commonOTAStateMachine: CommonOTAStateMachine) {

    open fun onDefault(){
        L.w(this::class.java.simpleName,"onDefault")
    }
    open fun onQuery(){
        L.w(this::class.java.simpleName,"onQuery")
    }
    open fun onDownload(){
        L.w(this::class.java.simpleName,"onDownload")
    }
    open fun onVerify(){
        L.w(this::class.java.simpleName,"onVerify")
    }
    open fun onInstall(){
        L.w(this::class.java.simpleName,"onInstall")
    }
    open fun onInstallSuccess(){
        L.w(this::class.java.simpleName,"onInstallSuccess")
    }
    open fun onInstallFailure(){
        L.w(this::class.java.simpleName,"onInstallFailure")
    }



}
