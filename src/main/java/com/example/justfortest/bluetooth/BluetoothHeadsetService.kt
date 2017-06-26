package com.example.justfortest.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.IBinder
import android.os.Message
import com.tuyou.tsd.common.base.BaseService

/**
 * Created by justi on 2017/6/19.
 */
class BluetoothHeadsetService : BaseService() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    val mBtHeadsetController:AbstractBluetoothHeadsetController by lazy {
        TricheerBluetoothHeadsetController(this)
    }
    override fun onCreate() {
        super.onCreate()
        initBroadcast()
        mBtHeadsetController.enter()

    }
    fun initBroadcast() {
        registerReceiver(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        registerReceiver(BluetoothDevice.ACTION_PAIRING_REQUEST)
        registerReceiver(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver("android.bluetooth.headsetclient.profile.action.AG_CALL_CHANGED")
        registerReceiver("android.bluetooth.headsetclient.profile.action.CONNECTION_STATE_CHANGED")
    }


    override fun onDestroy() {
        super.onDestroy()
        mBtHeadsetController.exit()
    }

    override fun handleMessage(msg: Message?) {
        mBtHeadsetController.handleIntent(msg?.obj as Intent)
    }


}