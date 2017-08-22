package com.example.justfortest.ota

import android.content.Context
import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/8/15.
 */
abstract class BaseOTAInstaller(var context: Context, var configureForOTA: ConfigureForOTA = ConfigureForOTA(), var commonOTAStateMachine: CommonOTAStateMachine) {
    companion object{
        val TAG: String = this::class.java.simpleName
    }

    open fun onDefault(){
        L.w(TAG, "onDefault")
    }
    open fun onQuery(){
        L.w(TAG, "onQuery")
    }
    open fun onDownload(){
        L.w(TAG, "onDownload")
    }
    open fun onVerify(){
        L.w(TAG, "onVerify")
    }
    open fun onInstall(){
        L.w(TAG, "onInstall")
    }
    open fun onInstallSuccess(){
        L.w(TAG, "onInstallSuccess")
    }
    open fun onInstallFailure(){
        L.w(TAG, "onInstallFailure")
    }

    fun sendInstallerResetMessage(configureForOTA: ConfigureForOTA)             = commonOTAStateMachine.sendInstallerResetMessage(configureForOTA)
    fun sendDefaultMessage()                                                    = commonOTAStateMachine.sendDefaultMessage()
    fun sendQueryMessage()                                                      = commonOTAStateMachine.sendQueryMessage()
    fun sendDownloadMessage()                                                   = commonOTAStateMachine.sendDownloadMessage()
    fun sendVerifyMessage()                                                     = commonOTAStateMachine.sendVerifyMessage()
    fun sendInstallMessage()                                                    = commonOTAStateMachine.sendInstallMessage()
    fun sendInstallSuccessMessage()                                             = commonOTAStateMachine.sendInstallSuccessMessage()
    fun sendInstallFailuerMessage()                                             = commonOTAStateMachine.sendInstallFailureMessage()
    fun sendAccOnMessage()                                                      = commonOTAStateMachine.sendAccOnMessage()
    fun sendAccOffMessage()                                                     = commonOTAStateMachine.sendAccOffMessage()



}
