package com.example.justfortest.ota

import android.content.Context
import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/8/15.
 */
class PatchOtaInstaller(context: Context,configureForOTA: ConfigureForOTA, commonOTAStateMachine: CommonOTAStateMachine) : BaseOTAInstaller(context,configureForOTA, commonOTAStateMachine) {

    override fun onDefault() {
        super.onDefault()
        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_QUERY)
    }

    override fun onQuery() {
        super.onQuery()
        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_SUCCESS)

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
