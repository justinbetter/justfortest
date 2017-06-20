package com.example.justfortest.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import com.example.justfortest.utils.BluetoothReflectUtils
import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/6/19.
 */
class TricheerBluetoothHeadsetController(context: Context) : AbstractBluetoothHeadsetController(context) {

    companion object {
        @JvmStatic
        val HEADSET_ACTION_DIAL = "bluetooth.TricheerBluetoothHeadsetController.action.dial"
        @JvmStatic
        val HEADSET_ACTION_ACCEPTCALL = "bluetooth.TricheerBluetoothHeadsetController.action.acceptCall"
        @JvmStatic
        val HEADSET_ACTION_REJECTCALL = "bluetooth.TricheerBluetoothHeadsetController.action.rejectCall"
        @JvmStatic
        val HEADSET_ACTION_TERMINATECALL = "bluetooth.TricheerBluetoothHeadsetController.action.terminateCall"




    }
    //hs remote device
    private var mRemoteDevice: BluetoothDevice? = null
    override fun getHFPProfileIndex(): Int {return 16}
    override fun enter() {
        mBluetoothAdapter.enable()
        mBluetoothAdapter.getProfileProxy(context, mServiceListener, getHFPProfileIndex())
        BluetoothReflectUtils.setConnectableDisacoverableMode(mBluetoothAdapter)
    }
    override fun exit() {
        mBluetoothAdapter.closeProfileProxy(getHFPProfileIndex(), mBluetoothHeadset as BluetoothProfile?)
    }

    override fun handleIntent(intent: Intent) {
        with(intent) {
            L.d("action: $action")
            when (action) {
                BluetoothAdapter.ACTION_STATE_CHANGED                   -> handleStateChaned(this)
                BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED        -> handleConnectionState(this)
                BluetoothDevice.ACTION_PAIRING_REQUEST                  -> handleParingRequest(this)
                BluetoothDevice.ACTION_BOND_STATE_CHANGED               -> handleBondState(this)
                HEADSET_ACTION_DIAL, HEADSET_ACTION_ACCEPTCALL, HEADSET_ACTION_REJECTCALL, HEADSET_ACTION_TERMINATECALL
                                                                        -> handleHeadsetAction(this)
                else                                                    -> L.d( "not handle action" + action)
            }
        }
    }

    /**
     * 蓝牙打开关闭状态
     */
    private fun handleStateChaned(intent: Intent) {
        L.d("action: " + intent.action + "/n  BTstate" + intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1))
    }



    /**
     * 连接状态
     */
    private fun handleConnectionState(intent: Intent) {
        val bluetoothConnectState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1)
        val bondedDevice: BluetoothDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        L.d("action: " + intent.action + "\n  connect_state:" + bluetoothConnectState + "\n bondedevice $bondedDevice")
        mBluetoothHeadset?.let {
            when(bluetoothConnectState){
                BluetoothAdapter.STATE_CONNECTED -> {
                    mRemoteDevice = bondedDevice
                    BluetoothReflectUtils.connect(it, bondedDevice)}
                BluetoothAdapter.STATE_DISCONNECTED->{
                    BluetoothReflectUtils.disconnect(it, bondedDevice)
                    mRemoteDevice = null
                }
                else -> {
                }
            }
        }
    }

    /**
     * 配对请求
     */
    private fun handleParingRequest(intent: Intent) {
        val bluetoothDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        bluetoothDevice.setPairingConfirmation(true)
        L.d("设备:$bluetoothDevice  配对成功" )

    }

    /**
     * 绑定状态
     */
    private fun handleBondState(intent: Intent) {
        val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
        val bondDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        L.d("bondstate:$bondState,bonddevice:$bondDevice")
    }

    /**
     * 蓝牙电话协议action
     */
    private fun handleHeadsetAction(intent: Intent) {
        if (mBluetoothHeadset == null ||  mRemoteDevice == null) {
            L.d("mBluetoothHeadset == null || mRemoteDevice == null!")
            return
        }else if (BluetoothReflectUtils.getConnectionState(mBluetoothHeadset as Any, mRemoteDevice as BluetoothDevice) != BluetoothProfile.STATE_CONNECTED){
            L.d("connectionstate is  not BluetoothProfile.STATE_CONNECTED")
            return
        }else{
            when (intent.action) {
                HEADSET_ACTION_DIAL -> {
                    BluetoothReflectUtils.dial(mBluetoothHeadset as Any, mRemoteDevice as BluetoothDevice, intent.getStringExtra("number"))
                }
                HEADSET_ACTION_ACCEPTCALL -> {
                    BluetoothReflectUtils.acceptCall(mBluetoothHeadset as Any, mRemoteDevice as BluetoothDevice,0)
                }
                HEADSET_ACTION_REJECTCALL -> {
                    BluetoothReflectUtils.rejectCall(mBluetoothHeadset as Any,mRemoteDevice as BluetoothDevice)
                }
                HEADSET_ACTION_TERMINATECALL->{
                    BluetoothReflectUtils.terminateCall(mBluetoothHeadset as Any, mRemoteDevice as BluetoothDevice,0)
                }
            }
        }
    }


}