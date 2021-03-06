package com.example.justfortest.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import com.tuyou.tsd.common.TSDDevice
import com.tuyou.tsd.common.util.ReflectUtil

/**
 * Created by justi on 2017/6/16.
 */
object BluetoothReflectUtils{


    @JvmField
    val BluetoothHeadsetClassName =when(TSDDevice.TSDBrand.BRAND){
        TSDDevice.TSDBrand.ZHIXING ->"android.bluetooth.BluetoothHandsfreeClient"
        TSDDevice.TSDBrand.TRICHEER -> "android.bluetooth.BluetoothHeadsetClient"
        else->"android.bluetooth.BluetoothHandsfreeClient"
    }

    @JvmStatic
    fun connect(paramObject: Any, paramBluetoothDevice: BluetoothDevice): Boolean {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "connect", *arrayOf<Class<*>>(BluetoothDevice::class.java)), *arrayOf<Any>(paramBluetoothDevice)) as Boolean
    }

    @JvmStatic
    fun disconnect(paramObject: Any, paramBluetoothDevice: BluetoothDevice): Boolean {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "disconnect", *arrayOf<Class<*>>(BluetoothDevice::class.java)), *arrayOf<Any>(paramBluetoothDevice)) as Boolean
    }

    @JvmStatic
    fun connectAudio(paramObject: Any): Boolean {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "connectAudio", *arrayOfNulls<Class<*>>(0)), *arrayOfNulls<Any>(0)) as Boolean
    }

    @JvmStatic
    fun disconnectAudio(paramObject: Any): Boolean {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "disconnectAudio", *arrayOfNulls<Class<*>>(0)), *arrayOfNulls<Any>(0)) as Boolean
    }

    @JvmStatic
    fun getAudioState(paramObject: Any, paramBluetoothDevice: BluetoothDevice): Int {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "getAudioState", *arrayOf<Class<*>>(BluetoothDevice::class.java)), *arrayOf<Any>(paramBluetoothDevice)) as Int
    }

    @JvmStatic
    fun dial(paramObject: Any, paramBluetoothDevice: BluetoothDevice, paramString: String): Boolean {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "dial", *arrayOf(BluetoothDevice::class.java, String::class.java)), *arrayOf(paramBluetoothDevice, paramString)) as Boolean
    }

    @JvmStatic
    fun getConnectionState(paramObject: Any, paramBluetoothDevice: BluetoothDevice): Int {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "getConnectionState", *arrayOf<Class<*>>(BluetoothDevice::class.java)), *arrayOf<Any>(paramBluetoothDevice)) as Int
    }

    @JvmStatic
    fun acceptCall(paramObject: Any, paramBluetoothDevice: BluetoothDevice, paramInt: Int): Boolean {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "acceptCall", BluetoothDevice::class.java, Int::class.javaPrimitiveType),
                paramBluetoothDevice, paramInt) as Boolean
    }

    @JvmStatic
    fun rejectCall(paramObject: Any, paramBluetoothDevice: BluetoothDevice): Boolean {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "rejectCall", *arrayOf<Class<*>>(BluetoothDevice::class.java)), *arrayOf<Any>(paramBluetoothDevice)) as Boolean
    }


    @JvmStatic
    fun terminateCall(paramObject: Any, paramBluetoothDevice: BluetoothDevice, paramInt: Int): Boolean {
        return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName, "terminateCall", BluetoothDevice::class.java, Int::class.javaPrimitiveType),
                paramBluetoothDevice, paramInt) as Boolean
    }

    @JvmStatic
    fun setConnectableDisacoverableMode(mBluetoothAdapter:BluetoothAdapter) {
        ReflectUtil.inVoke(mBluetoothAdapter, ReflectUtil.getMethod(BluetoothAdapter::class.java.canonicalName,
                "setScanMode", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType), BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 0)
    }

    @JvmStatic
    fun sendDTMF(paramObject: Any, device: BluetoothDevice, digit: String) : Boolean{
       return ReflectUtil.inVoke(paramObject, ReflectUtil.getMethod(BluetoothHeadsetClassName,"sendDTMF",BluetoothDevice::class.java,Byte::class.java),device,digit.toByteArray()[0]) as Boolean
    }


}