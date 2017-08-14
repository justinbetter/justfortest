package com.example.justfortest.statemachine

import android.os.Message
import com.tuyou.tsd.common.statemachine.IState
import com.tuyou.tsd.common.statemachine.State
import com.tuyou.tsd.common.statemachine.StateMachine

/**
 * Created by justi on 2017/7/13.
 */
class LoginStateMachine(listener: OnStateChangeListener) : StateMachine("LoginStateMachine") {

    interface OnStateChangeListener {
        fun onStateEnter(state: BaseState)
        fun handleCondition(msg: Message?)
    }

    abstract class BaseState(val stateMachine: LoginStateMachine) : State() {
        var message: Message? = null
        override fun enter() {
            super.enter()
            stateMachine.L.onStateEnter(this)
        }

        override fun exit() {
            super.exit()
        }
    }

    private fun transitionToState(msg: Message?){
        val state = stateMap[msg!!.what] as BaseState
        state.message = msg
        transitionTo(state)
    }

    val L: OnStateChangeListener = listener
    private val mDefault: Default = Default(this)
    private val login: Login = Login(this)
    private val loginOut: LoginOut = LoginOut(this)
    private val tempLogin: TempLogin = TempLogin(this)
    private val tempSuccess: TempSuccess = TempSuccess(this)
    private val tempFail: TempFail = TempFail(this)
    private val normalLogin: NormalLogin = NormalLogin(this)
    private val normalSuccess: NormalSuccess = NormalSuccess(this)
    private val normalFail: NormalFail = NormalFail(this)

    companion object {
        val MESSAGE_DEFAULT = 100
        val MESSAGE_LOGIN = 101
        val MESSAGE_LOGINOUT = 102
        val MESSAGE_TEMP_LOGIN = 103
        val MESSAGE_TEMP_SUCCESS = 104
        val MESSAGE_TEMP_FAIL = 105
        val MESSAGE_NORMAL_LOGIN = 106
        val MESSAGE_NORMAL_SUCCESS = 107
        val MESSAGE_NORMAL_FAIL = 108
        val MESSAGE_TRY_LOGIN = 109
        val MESSAGE_TRY_LOGIN_IF_NETWORK_AVALIABLE = 110

    }

    val stateMap = mapOf<Int, State>(MESSAGE_DEFAULT to mDefault, MESSAGE_LOGIN to login, MESSAGE_LOGINOUT to loginOut, MESSAGE_TEMP_LOGIN to tempLogin, MESSAGE_TEMP_SUCCESS to tempSuccess, MESSAGE_TEMP_FAIL to tempFail, MESSAGE_NORMAL_LOGIN to normalLogin, MESSAGE_NORMAL_SUCCESS to normalSuccess, MESSAGE_NORMAL_FAIL to normalFail)

    init {
        addState(mDefault)
        addState(login, mDefault)
        addState(tempLogin, login)
        addState(tempSuccess, tempLogin)
        addState(tempFail, mDefault)
        addState(normalLogin, login)
        addState(normalSuccess, normalLogin)
        addState(normalFail, mDefault)
        addState(loginOut, mDefault)
        setInitialState(mDefault)

    }

    class Default(stateMachine: LoginStateMachine) : BaseState(stateMachine) {

        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_TRY_LOGIN, MESSAGE_TRY_LOGIN_IF_NETWORK_AVALIABLE ->{
                        stateMachine.L.handleCondition(msg)
                        return IState.HANDLED
                    }
                    MESSAGE_LOGIN, MESSAGE_LOGINOUT                           -> {
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else                                                      -> {
                        return IState.NOT_HANDLED
                    }
                }
            }
        }
    }

    class Login(stateMachine: LoginStateMachine) : BaseState(stateMachine) {
        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_TRY_LOGIN, MESSAGE_LOGIN         ->{
                        return  IState.HANDLED
                    }
                    MESSAGE_TEMP_LOGIN, MESSAGE_NORMAL_LOGIN -> {
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else                                     -> {
                        return super.processMessage(msg)
                    }
                }
            }
        }
    }

    class LoginOut(stateMachine: LoginStateMachine) : BaseState(stateMachine) {
        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_LOGINOUT,MESSAGE_TRY_LOGIN_IF_NETWORK_AVALIABLE -> {
                        return IState.HANDLED
                    }
                    else             -> {
                        return super.processMessage(msg)
                    }
                }
            }
        }
    }

    class TempLogin(stateMachine: LoginStateMachine) : BaseState(stateMachine) {
        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_TEMP_LOGIN, MESSAGE_NORMAL_LOGIN ->{
                        return IState.HANDLED
                    }
                    MESSAGE_TEMP_SUCCESS, MESSAGE_TEMP_FAIL  -> {
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else                                     -> {
                        return super.processMessage(msg)
                    }
                }
            }
        }
    }

    class TempSuccess(stateMachine: LoginStateMachine) : BaseState(stateMachine) {
        override fun processMessage(msg: Message?): Boolean {

            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_TEMP_SUCCESS, MESSAGE_TEMP_FAIL ->{
                        return IState.HANDLED
                    }
                    else                                    -> {
                        return super.processMessage(msg)
                    }
                }
            }

        }
    }

    class TempFail(stateMachine: LoginStateMachine) : BaseState(stateMachine) {
        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_TEMP_SUCCESS, MESSAGE_TEMP_FAIL,MESSAGE_TRY_LOGIN_IF_NETWORK_AVALIABLE ->{
                        return IState.HANDLED
                    }
                    else                                    -> {
                        return super.processMessage(msg)
                    }
                }
            }
        }
    }

    class NormalLogin(stateMachine: LoginStateMachine) : BaseState(stateMachine) {
        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_TEMP_LOGIN, MESSAGE_NORMAL_LOGIN    ->{
                        return IState.HANDLED
                    }
                    MESSAGE_NORMAL_SUCCESS, MESSAGE_NORMAL_FAIL -> {
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else                                        -> {
                        return super.processMessage(msg)
                    }
                }
            }
        }
    }

    class NormalSuccess(stateMachine: LoginStateMachine) : BaseState(stateMachine) {
        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_NORMAL_SUCCESS, MESSAGE_NORMAL_FAIL -> {
                        return IState.HANDLED
                    }
                    else                                        -> {
                        return super.processMessage(msg)
                    }
                }
            }
        }
    }

    class NormalFail(stateMachine: LoginStateMachine) : BaseState(stateMachine) {
        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_NORMAL_SUCCESS, MESSAGE_NORMAL_FAIL,MESSAGE_TRY_LOGIN_IF_NETWORK_AVALIABLE -> {
                        return IState.HANDLED
                    }
                    else                                        -> {
                        return super.processMessage(msg)
                    }
                }
            }
        }
    }


}