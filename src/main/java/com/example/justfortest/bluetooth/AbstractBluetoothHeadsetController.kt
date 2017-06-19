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
    var mServiceListener: BluetoothProfile.ServiceListener = object : BluetoothProfile.ServiceListener {

        override fun onServiceDisconnected(profile: Int) {
            L.d("profile onServiceDisconnected：$profile")
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset = null
            }
        }

        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            L.d("profile onServiceConnected：$profile,$proxy")
            if (profile == getHFPProfileIndex()) {
                mBluetoothHeadset = proxy
                L.d("proxy: $profile,mheadset:$mBluetoothHeadset")
            }
        }
    }
}