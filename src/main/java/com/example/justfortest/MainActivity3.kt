package com.example.justfortest

import android.app.Activity
import android.os.Bundle
import android.os.Message
import android.view.View
import com.example.justfortest.statemachine.BluetoothStateMachine
import com.justlab.log.JLog

/**
 * Created by justi on 2017/6/15.
 */
class MainActivity3 : Activity(), BluetoothStateMachine.StateListener {

    override fun handleRequest(msg: Message?): Boolean {
        return true
    }

    override fun notHandleRequest(msg: Message?) {
    }

    override fun handleEnter(state: BluetoothStateMachine.BaseState) {

    }
    override fun handleExit(state: BluetoothStateMachine.BaseState) {
    }


    val bluetoothStateMachine = BluetoothStateMachine(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        bluetoothStateMachine.start()
        JLog.d("onCreate")

    }


    fun clickButton(v: View?) {
        when (v?.id) {
            R.id.MESSAGE_TURNING_ON           -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_TURNING_ON)
            R.id.MESSAGE_TURNED_ON            -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_TURNED_ON)
            R.id.MESSAGE_TURNING_OFF          -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_TURNING_OFF)
            R.id.MESSAGE_TURNED_OFF           -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_TURNED_OFF)
            R.id.MESSAGE_CONNECTING_DEVICE    -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_CONNECTING_DEVICE)
            R.id.MESSAGE_CONNECTED_DEVICE     -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_CONNECTED_DEVICE)
            R.id.MESSAGE_DISCONNECTING_DEVICE -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_DISCONNECTING_DEVICE)
            R.id.MESSAGE_DISCONNECTED_DEVICE  -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_DISCONNECTED_DEVICE)
            R.id.MESSAGE_READY                -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_READY)
            R.id.MESSAGE_DIALING              -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_DIALING)
            R.id.MESSAGE_INCALL               -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_INCALL)
            R.id.MESSAGE_INCOMMINGCALL        -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_INCOMMINGCALL)
            R.id.MESSAGE_TERMINATINGCALL      -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_TERMINATINGCALL)
            R.id.MESSAGE_TERMINATEDCALL       -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_TERMINATEDCALL)
            R.id.MESSAGE_CONNECTED_FAIL       -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_CONNECTED_FAIL)
            R.id.MESSAGE_REMOTE_DISCONNECTED  -> bluetoothStateMachine.sendMessage(BluetoothStateMachine.MESSAGE_REMOTE_DISCONNECTED)

            else                              -> return
        }
    }


}