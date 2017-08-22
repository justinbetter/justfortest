package com.example.justfortest.ota

import android.content.Context
import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/8/15.
 */
class AppOtaInstaller(context: Context, configureForOTA: ConfigureForOTA , commonOTAStateMachine: CommonOTAStateMachine) : BaseOTAInstaller(context, configureForOTA, commonOTAStateMachine) {

    override fun onDefault() {
        super.onDefault()
        sendQueryMessage()
    }

    override fun onQuery() {
        super.onQuery()
        L.w("do query")
        sendDownloadMessage()
    }

    override fun onDownload() {
        super.onDownload()
        sendVerifyMessage()
    }

    override fun onVerify() {
        super.onVerify()
        sendInstallMessage()

    }

    override fun onInstall() {
        super.onInstall()
        sendInstallSuccessMessage()
    }

    override fun onInstallSuccess() {
        super.onInstallSuccess()
        sendInstallerResetMessage(ConfigureForOTA(ConfigureForOTA.Constants.TYPE_PATCH,ConfigureForOTA.Constants.MODE_BSDIFF))
    }

    override fun onInstallFailure() {
        super.onInstallFailure()
        sendDefaultMessage()
    }
}
