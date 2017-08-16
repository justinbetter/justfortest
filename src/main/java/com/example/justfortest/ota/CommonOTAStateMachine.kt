package com.example.justfortest.ota

import android.os.Message
import com.tuyou.tsd.common.statemachine.IState
import com.tuyou.tsd.common.statemachine.State
import com.tuyou.tsd.common.statemachine.StateMachine

/**
 * Created by justi on 2017/8/14.
 */
class CommonOTAStateMachine : StateMachine {

    //         通用流程     query-> download-> verify-> install->result(success/fail)

    /**
     *                                       default
     *                                           |
     *                    ------------------------------------------------------------------
     *                   |         |           |             |         |                   |
     *                 query     download     verify      install     installsuccess   installfail
     *
     *
     *
     *
     */

    constructor(name: String, listener: OnStateChangeListener) : super(name){
        this.L = listener
        addState(mDefault)
        addState(query,mDefault)
        addState(download,mDefault)
        addState(verify,mDefault)
        addState(install,mDefault)
        addState(installSuccess,mDefault)
        addState(installFailure,mDefault)
        setInitialState(mDefault)
    }

    companion object {
        //inner
        private val BASEINDEX = 1000
        val MESSAGE_DEFAULT                         = BASEINDEX + 1
        val MESSAGE_QUERY                           = BASEINDEX + 2
        val MESSAGE_DOWNLOAD                        = BASEINDEX + 3
        val MESSAGE_VERIFY                          = BASEINDEX + 4
        val MESSAGE_INSTALL                         = BASEINDEX + 5
        val MESSAGE_INSTALL_SUCCESS                 = BASEINDEX + 6
        val MESSAGE_INSTALL_FAILURE                 = BASEINDEX + 7

        //external
        val MESSAGE_ACC_ON                          = BASEINDEX + 101
        val MESSAGE_ACC_OFF                         = BASEINDEX + 102

    }
    private var L : OnStateChangeListener
    private var mDefault: Default = Default(this)
    private var query: Query = Query(this)
    private var download: Download = Download(this)
    private var verify: Verify = Verify(this)
    private var install: Install = Install(this)
    private var installSuccess: InstallSuccess = InstallSuccess(this)
    private var installFailure: InstallFailure = InstallFailure(this)
    private val stateMap = mapOf<Int, BaseState>(
            MESSAGE_DEFAULT to mDefault,
            MESSAGE_QUERY to query,
            MESSAGE_DOWNLOAD to download,
            MESSAGE_VERIFY to verify,
            MESSAGE_INSTALL to install,
            MESSAGE_INSTALL_SUCCESS to installSuccess,
            MESSAGE_INSTALL_FAILURE to installFailure
                                     )

    /**
     * according to map for transition
     */
    private fun transitionToState(msg: Message?) {
        val state = stateMap[msg!!.what]
        state?.message = msg
        transitionTo(state)
    }

    /**
     * 状态接口
     */
    interface OnStateChangeListener {
        fun onStateEnter(state: BaseState)
        fun handleCondition(msg: Message?)
    }


    /**
     * state 基类
     */
    abstract class BaseState(val stateMachine: CommonOTAStateMachine) : State() {
        var message: Message? = null
        override fun enter() {
            super.enter()
            stateMachine.L.onStateEnter(this)
        }

        override fun exit() {
            super.exit()
        }
    }

    class Default(stateMachine: CommonOTAStateMachine) : BaseState(stateMachine) {

        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_DEFAULT ->{
                        return IState.HANDLED
                    }
                    MESSAGE_QUERY ->{
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else ->{
                        return IState.NOT_HANDLED
                    }
                }
            }
        }
    }


    class Query(stateMachine: CommonOTAStateMachine) : BaseState(stateMachine) {

        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_QUERY ->{
                        return IState.HANDLED
                    }
                    MESSAGE_DEFAULT,MESSAGE_DOWNLOAD, MESSAGE_VERIFY, MESSAGE_INSTALL,
                    MESSAGE_INSTALL_SUCCESS, MESSAGE_INSTALL_FAILURE->{
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else                                                                                                 ->{
                        return IState.NOT_HANDLED
                    }
                }
            }
        }
    }



    class Download(stateMachine: CommonOTAStateMachine) : BaseState(stateMachine) {

        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_DOWNLOAD ->{
                        return IState.HANDLED
                    }
                    MESSAGE_QUERY -> {
                        deferMessage(msg)
                        return IState.HANDLED
                    }
                    MESSAGE_DEFAULT,MESSAGE_VERIFY   ->{
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else             ->{
                        return IState.NOT_HANDLED
                    }
                }
            }
        }
    }


    class Verify(stateMachine: CommonOTAStateMachine) : BaseState(stateMachine) {

        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_VERIFY  ->{
                        return IState.HANDLED
                    }
                    MESSAGE_QUERY -> {
                        deferMessage(msg)
                        return IState.HANDLED
                    }
                    MESSAGE_DEFAULT,MESSAGE_INSTALL ->{
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else            ->{
                        return IState.NOT_HANDLED
                    }
                }
            }
        }
    }


    class Install(stateMachine: CommonOTAStateMachine) : BaseState(stateMachine) {

        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_INSTALL ->{
                        return IState.HANDLED
                    }
                    MESSAGE_QUERY -> {
                        deferMessage(msg)
                        return IState.HANDLED
                    }
                    MESSAGE_DEFAULT,MESSAGE_INSTALL_SUCCESS, MESSAGE_INSTALL_FAILURE ->{
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else ->{
                        return IState.NOT_HANDLED
                    }
                }
            }
        }
    }

    class InstallSuccess(stateMachine: CommonOTAStateMachine) : BaseState(stateMachine) {

        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_INSTALL_SUCCESS, MESSAGE_INSTALL_FAILURE->{
                        return IState.HANDLED
                    }
                    MESSAGE_QUERY -> {
                        deferMessage(msg)
                        return IState.HANDLED
                    }
                    MESSAGE_DEFAULT ->{
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else                                             ->{
                        return IState.NOT_HANDLED
                    }
                }
            }
        }
    }

    class InstallFailure(stateMachine: CommonOTAStateMachine) : BaseState(stateMachine) {

        override fun processMessage(msg: Message?): Boolean {
            with(stateMachine) {
                when (msg?.what) {
                    MESSAGE_INSTALL_SUCCESS,MESSAGE_INSTALL_FAILURE ->{
                        return IState.HANDLED
                    }
                    MESSAGE_QUERY -> {
                        deferMessage(msg)
                        return IState.HANDLED
                    }
                    MESSAGE_DEFAULT ->{
                        transitionToState(msg)
                        return IState.HANDLED
                    }
                    else ->{
                        return IState.NOT_HANDLED
                    }
                }
            }
        }
    }


}