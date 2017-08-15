package com.example.justfortest

import android.app.Activity
import android.os.Bundle
import android.os.Message
import android.view.View
import com.example.justfortest.ota.CommonOTAStateMachine
import com.example.justfortest.ota.ConfigureForOTA
import com.example.justfortest.ota.OTAController

/**
 * Created by justi on 2017/6/15.
 */
class MainActivity4 : Activity(), CommonOTAStateMachine.OnStateChangeListener {

    override fun onStateEnter(state: CommonOTAStateMachine.BaseState) {
    }

    override fun handleCondition(msg: Message?) {

    }


    val commonOTAStateMachine = CommonOTAStateMachine("app_statemachine", this)
    val otaController:OTAController = OTAController(ConfigureForOTA())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
//        commonOTAStateMachine.start()
        otaController.start()
    }



    fun clickButton(v: View?) {
        when (v?.id) {
            R.id.MESSAGE_ACC_ON          -> otaController.sendMessage(CommonOTAStateMachine.MESSAGE_ACC_ON)
            R.id.MESSAGE_ACC_OFF         -> otaController.sendMessage(CommonOTAStateMachine.MESSAGE_ACC_OFF)
            R.id.MESSAGE_DEFAULT         -> otaController.sendMessage(CommonOTAStateMachine.MESSAGE_DEFAULT)
            R.id.MESSAGE_QUERY           -> otaController.sendMessage(CommonOTAStateMachine.MESSAGE_QUERY)
            R.id.MESSAGE_DOWNLOAD        -> otaController.sendMessage(CommonOTAStateMachine.MESSAGE_DOWNLOAD)
            R.id.MESSAGE_VERIFY          -> otaController.sendMessage(CommonOTAStateMachine.MESSAGE_VERIFY)
            R.id.MESSAGE_INSTALL         -> otaController.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL)
            R.id.MESSAGE_INSTALL_SUCCESS -> otaController.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_SUCCESS)
            R.id.MESSAGE_INSTALL_FAILURE -> otaController.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_FAILURE)

            else                              -> return
        }
    }


}