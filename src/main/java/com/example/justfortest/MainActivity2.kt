package com.example.justfortest

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.example.justfortest.statemachine.LoginStateMachine
import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/6/15.
 */
class MainActivity2 : Activity(), LoginStateMachine.OnStateChangeListener {

    override fun onStateEnter(state: LoginStateMachine.BaseState) {
        L.e("enter $state")
    }


    val loginStateMachine = LoginStateMachine(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        loginStateMachine.start()

    }


    fun clickButton(v: View?) {
        when (v?.id) {
            R.id.mDefault  -> loginStateMachine.sendMessage(LoginStateMachine.MESSAGE_DEFAULT)
            R.id.login  -> loginStateMachine.sendMessage(LoginStateMachine.MESSAGE_LOGIN)
            R.id.loginout  -> loginStateMachine.sendMessage(LoginStateMachine.MESSAGE_LOGINOUT)
            R.id.tempLogin  -> loginStateMachine.sendMessage(LoginStateMachine.MESSAGE_TEMP_LOGIN)
            R.id.tempSuccess  -> loginStateMachine.sendMessage(LoginStateMachine.MESSAGE_TEMP_SUCCESS)
            R.id.tempFail  -> loginStateMachine.sendMessage(LoginStateMachine.MESSAGE_TEMP_FAIL)
            R.id.normalLogin  -> loginStateMachine.sendMessage(LoginStateMachine.MESSAGE_NORMAL_LOGIN)
            R.id.normalFail  -> loginStateMachine.sendMessage(LoginStateMachine.MESSAGE_NORMAL_FAIL)
            R.id.normalSuccess  -> loginStateMachine.sendMessage(LoginStateMachine.MESSAGE_NORMAL_SUCCESS)

            else              -> return
        }
    }


}