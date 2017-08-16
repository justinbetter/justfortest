package com.example.justfortest

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.example.justfortest.ota.BaseOTAInstaller
import com.example.justfortest.ota.CommonOTAStateMachine
import com.example.justfortest.ota.ConfigureForOTA
import com.example.justfortest.ota.OTAInstallerFactory

/**
 * Created by justi on 2017/6/15.
 */
class MainActivity4 : Activity() {


    val otaInstaller: BaseOTAInstaller=OTAInstallerFactory.createInstaller(ConfigureForOTA(ConfigureForOTA.Constants.TYPE_PATCH,ConfigureForOTA.Constants.MODE_BSDIFF))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        otaInstaller.start()
    }

    fun clickButton(v: View?) {
        when (v?.id) {
            R.id.MESSAGE_ACC_ON          -> otaInstaller.sendMessage(CommonOTAStateMachine.MESSAGE_ACC_ON)
            R.id.MESSAGE_ACC_OFF         -> otaInstaller.sendMessage(CommonOTAStateMachine.MESSAGE_ACC_OFF)
            R.id.MESSAGE_DEFAULT         -> otaInstaller.sendMessage(CommonOTAStateMachine.MESSAGE_DEFAULT)
            R.id.MESSAGE_QUERY           -> otaInstaller.sendMessage(CommonOTAStateMachine.MESSAGE_QUERY)
            R.id.MESSAGE_DOWNLOAD        -> otaInstaller.sendMessage(CommonOTAStateMachine.MESSAGE_DOWNLOAD)
            R.id.MESSAGE_VERIFY          -> otaInstaller.sendMessage(CommonOTAStateMachine.MESSAGE_VERIFY)
            R.id.MESSAGE_INSTALL         -> otaInstaller.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL)
            R.id.MESSAGE_INSTALL_SUCCESS -> otaInstaller.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_SUCCESS)
            R.id.MESSAGE_INSTALL_FAILURE -> otaInstaller.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_FAILURE)

            else                              -> return
        }
    }


}