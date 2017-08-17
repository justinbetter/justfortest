package com.example.justfortest.ota

import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/8/15.
 */
class RomOtaInstaller(configureForOTA: ConfigureForOTA, commonOTAStateMachine: CommonOTAStateMachine) : BaseOTAInstaller(configureForOTA, commonOTAStateMachine) {
    override fun onDefault() {
        super.onDefault()
        L.w("rom begin!")
    }

    override fun onQuery() {
        super.onQuery()
        L.w("query install success")
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
        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_RESET,ConfigureForOTA(ConfigureForOTA.Constants.TYPE_APP,ConfigureForOTA.Constants.MODE_FULL_UPGRADE))
    }

    override fun onInstallFailure() {
        super.onInstallFailure()
    }
}
