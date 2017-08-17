package com.example.justfortest.ota

import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/8/15.
 */
class AppOtaInstaller(configureForOTA: ConfigureForOTA = ConfigureForOTA(), commonOTAStateMachine: CommonOTAStateMachine) : BaseOTAInstaller(configureForOTA, commonOTAStateMachine) {

    override fun onDefault() {
        super.onDefault()
        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_QUERY)
    }

    override fun onQuery() {
        super.onQuery()
        L.w("do query")
        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_DOWNLOAD)
    }

    override fun onDownload() {
        super.onDownload()
        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_VERIFY)
    }

    override fun onVerify() {
        super.onVerify()
        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL)

    }

    override fun onInstall() {
        super.onInstall()
        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_SUCCESS)
    }

    override fun onInstallSuccess() {
        super.onInstallSuccess()
        commonOTAStateMachine.sendInstallerResetMessage(ConfigureForOTA(ConfigureForOTA.Constants.TYPE_PATCH,ConfigureForOTA.Constants.MODE_BSDIFF))
    }

    override fun onInstallFailure() {
        super.onInstallFailure()
        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_DEFAULT)
    }
}
