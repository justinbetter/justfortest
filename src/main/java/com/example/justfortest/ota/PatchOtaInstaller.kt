package com.example.justfortest.ota

import android.content.Context
import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/8/15.
 */
class PatchOtaInstaller(context: Context,configureForOTA: ConfigureForOTA, commonOTAStateMachine: CommonOTAStateMachine) : BaseOTAInstaller(context,configureForOTA, commonOTAStateMachine) {

    override fun onDefault() {
        super.onDefault()
        sendQueryMessage()
    }

    override fun onQuery() {
        super.onQuery()
        sendInstallSuccessMessage()
    }

    override fun onDownload() {
        super.onDownload()
    }

    override fun onVerify() {
        super.onVerify()
    }

    override fun onInstall() {
        super.onInstall()
    }

    override fun onInstallSuccess() {
        super.onInstallSuccess()
        L.w("a good ending")
    }

    override fun onInstallFailure() {
        super.onInstallFailure()
    }
}
