package com.example.justfortest.utils

import android.bluetooth.BluetoothDevice
import com.tuyou.tsd.common.util.ReflectUtil

/**
 * Created by justi on 2017/6/16.
 */
object BluetoothReflectUtils{


    @JvmField
    val BluetoothHeadsetClassName = "android.bluetooth.BluetoothHeadsetClient"

    @JvmStatic
    fun connect(paramObject: Any, paramBluetoothDevice: BluetoothDevice): Boolean {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "connect", *arrayOf<Class<*>>(BluetoothDevice::class.java)), *arrayOf<Any>(paramBluetoothDevice)) as Boolean
    }

    @JvmStatic
    fun dial(paramObject: Any, paramBluetoothDevice: BluetoothDevice, paramString: String): Boolean {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "dial", *arrayOf(BluetoothDevice::class.java, String::class.java)), *arrayOf(paramBluetoothDevice, paramString)) as Boolean
    }

    @JvmStatic
    fun getConnectionState(paramObject: Any, paramBluetoothDevice: BluetoothDevice): Int {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "getConnectionState", *arrayOf<Class<*>>(BluetoothDevice::class.java)), *arrayOf<Any>(paramBluetoothDevice)) as Int
    }





}