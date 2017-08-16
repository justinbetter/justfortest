package com.example.justfortest.ota

import android.os.Message
import android.support.annotation.CallSuper

/**
 * Created by justi on 2017/8/15.
 */
abstract class BaseOTAInstaller(configureForOTA: ConfigureForOTA = ConfigureForOTA()) : CommonOTAStateMachine.OnStateChangeListener {

    var commonOTAStateMachine:CommonOTAStateMachine = CommonOTAStateMachine(configureForOTA.type, this)

    override fun onStateEnter(state: CommonOTAStateMachine.BaseState) {
        onEnterState(state)
    }

    override fun handleCondition(msg: Message?) {

    }

    @CallSuper
    open fun start(){
        commonOTAStateMachine.start()
    }
    fun  sendMessage(what: Int) {
        commonOTAStateMachine.sendMessage(what)
    }
    fun  sendMessage(msg: Message) {
        commonOTAStateMachine.sendMessage(msg)
    }

    abstract fun onEnterState(state: CommonOTAStateMachine.BaseState)


}
