package com.example.justfortest.ota

/**
 * Created by justi on 2017/8/15.
 */
class AppOtaInstaller(configureForOTA: ConfigureForOTA = ConfigureForOTA()) : BaseOTAInstaller(configureForOTA) {

    override fun onDefault() {
        super.onDefault()
    }

    override fun onQuery() {
        super.onQuery()
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
    }

    override fun onInstallFailure() {
        super.onInstallFailure()
    }
}
