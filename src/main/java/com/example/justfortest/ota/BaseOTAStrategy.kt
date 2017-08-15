package com.example.justfortest.ota

import android.os.Message
import android.support.annotation.CallSuper

/**
 * Created by justi on 2017/8/15.
 */
abstract class BaseOTAStrategy(configureForOTA: ConfigureForOTA = ConfigureForOTA()) : CommonOTAStateMachine.OnStateChangeListener {

    var commonOTAStateMachine:CommonOTAStateMachine = CommonOTAStateMachine(configureForOTA.type, this)

    override fun onStateEnter(state: CommonOTAStateMachine.BaseState) {
        onEnterState()
    }

    override fun handleCondition(msg: Message?) {

    }

    @CallSuper
    open fun start(){
        commonOTAStateMachine.start()
    }
    abstract fun onEnterState()


}
