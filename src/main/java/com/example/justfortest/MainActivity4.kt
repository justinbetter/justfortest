package com.example.justfortest

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.example.justfortest.ota.CommonOTAStateMachine
import com.example.justfortest.ota.ConfigureForOTA

/**
 * Created by justi on 2017/6/15.
 */
class MainActivity4 : Activity() {


    val commonOTAStateMachine = CommonOTAStateMachine(ConfigureForOTA(ConfigureForOTA.Constants.TYPE_ROM,ConfigureForOTA.Constants.MODE_VENDOR))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        commonOTAStateMachine.start()
    }


    fun clickButton(v: View?) {
        when (v?.id) {
            R.id.APP_INSTALL             -> {
                commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_RESET,
                                                  ConfigureForOTA(ConfigureForOTA.Constants.TYPE_APP, ConfigureForOTA.Constants.MODE_FULL_UPGRADE))
            }
            R.id.ROM_INSTALL             -> {
                commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_RESET,
                                                  ConfigureForOTA(ConfigureForOTA.Constants.TYPE_ROM, ConfigureForOTA.Constants.MODE_VENDOR))
            }
            R.id.PATCH_INSTALL           -> {
                commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_RESET,
                                                  ConfigureForOTA(ConfigureForOTA.Constants.TYPE_PATCH, ConfigureForOTA.Constants.MODE_BSDIFF))
            }
            R.id.MESSAGE_ACC_ON          -> commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_ACC_ON)
            R.id.MESSAGE_ACC_OFF         -> commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_ACC_OFF)
            R.id.MESSAGE_DEFAULT         -> commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_DEFAULT)
            R.id.MESSAGE_QUERY           -> commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_QUERY)
            R.id.MESSAGE_DOWNLOAD        -> commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_DOWNLOAD)
            R.id.MESSAGE_VERIFY          -> commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_VERIFY)
            R.id.MESSAGE_INSTALL         -> commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL)
            R.id.MESSAGE_INSTALL_SUCCESS -> commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_SUCCESS)
            R.id.MESSAGE_INSTALL_FAILURE -> commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_FAILURE)
        }
    }


}