package com.example.justfortest.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/6/19.
 */
abstract class AbstractBluetoothHeadsetController (context: Context){


    abstract fun enter()
    abstract fun exit()
    abstract fun getHFPProfileIndex(): Int
    abstract fun handleIntent(intent: Intent)

    val context :Context by lazy {
        context
    }
    val mBluetoothAdapter:BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }
    var mBluetoothHeadset: Any? = null

}