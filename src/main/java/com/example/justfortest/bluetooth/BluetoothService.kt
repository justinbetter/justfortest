package com.example.justfortest.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.content.Intent
import android.os.Message
import android.util.Log
import com.example.justfortest.utils.BluetoothReflectUtils
import com.tuyou.tsd.common.base.BaseService
import com.tuyou.tsd.common.util.ReflectUtil

/**
 * Created by justi on 2017/6/14.
 */
class BluetoothService : BaseService() {


    /**
     * init BT
     * init Broadcast
     *
     */
    val mBluetoothAdapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }
    private val HFP_PROFILE = getHFPProfileIndex()

    private fun getHFPProfileIndex(): Int {
        return 16
    }
    var mBluetoothHeadset: Any? = null

    var mServiceListener: BluetoothProfile.ServiceListener = object : BluetoothProfile.ServiceListener {

        override fun onServiceDisconnected(profile: Int) {
            Log.d(TAG, "接听搭建取消：$profile")
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset = null
            }
        }
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            Log.d(TAG, "接听搭建成功：$profile,$proxy")
            if (profile == getHFPProfileIndex()) {
                mBluetoothHeadset = proxy
                Log.d(TAG, "proxy: $profile,mheadset:$mBluetoothHeadset")


            }

        }

    }


    private val TAG = "justin"
    override fun onCreate() {
        super.onCreate()
        initBT()
        initBroadcast()
    }

    override fun onDestroy() {
        super.onDestroy()
//        mBluetoothAdapter.closeProfileProxy(getHFPProfileIndex(), mBluetoothHeadset)
    }

    private fun initBT() {
        mBluetoothAdapter.enable()
        //设置可被连接
        setConnectableDisacoverableMode()

        //设置协议
        mBluetoothAdapter.getProfileProxy(applicationContext, mServiceListener, getHFPProfileIndex())

    }

    private fun initBroadcast() {
        //获取蓝牙状态变更
        registerReceiver(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        registerReceiver(BluetoothDevice.ACTION_PAIRING_REQUEST)
        registerReceiver(BluetoothDevice.ACTION_BOND_STATE_CHANGED)


    }


    override fun handleMessage(msg: Message?) {
        msg?.obj ?: return
        with(msg.obj as Intent) {
            Log.d(TAG, "action: $action")
            when (action) {
                BluetoothAdapter.ACTION_STATE_CHANGED               -> Log.d(TAG, "action: " + action + "/n  state" + getIntExtra(BluetoothAdapter.EXTRA_STATE, -1))
                BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED    -> handleConnectionState(this)
                BluetoothDevice.ACTION_PAIRING_REQUEST              -> handleParingRequest(this)
                BluetoothDevice.ACTION_BOND_STATE_CHANGED           -> handleBondState(this)
                else                                                -> Log.d(TAG, "not handle  action" + action)

            }
        }


    }

    /**
     * 连接状态
     */
    private fun handleConnectionState(intent: Intent) {
        val bluetoothConnectState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1)
        val bondedDevice: BluetoothDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)


        var headsetConnectState:Boolean = false
        Log.d(TAG, "action: " + intent.action + "\n  connect_state:" + bluetoothConnectState+"\n bondedevice $bondedDevice")
        if (bluetoothConnectState == BluetoothAdapter.STATE_CONNECTED) {
            BluetoothReflectUtils.connect(mBluetoothHeadset!!, bondedDevice)
//            Log.d(TAG, "hfp: connect $headsetConnectState")

            val connectionState = BluetoothReflectUtils.getConnectionState(mBluetoothHeadset!!, bondedDevice)
            Log.d(TAG, "connstate: $connectionState")

            if (connectionState == BluetoothProfile.STATE_CONNECTED) {
                BluetoothReflectUtils.dial(mBluetoothHeadset!!,bondedDevice,"17602142076")

            }


        }



    }

    /**
     * 绑定状态
     */
    private fun handleBondState(intent: Intent) {
        val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
        val bondDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        Log.d(TAG, "bondstate:$bondState,bonddevice:$bondDevice")
        if (bondState == BluetoothDevice.BOND_BONDED) {
            //hfp
            Log.d(TAG, "已连接！可以打电话了")
        } else {


        }


    }

    /**
     * 配对请求
     */
    private fun handleParingRequest(intent: Intent) {
        val bluetoothDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        bluetoothDevice.setPairingConfirmation(true)
        Log.d(TAG, "配对成功" + intent.action)


    }


    private fun setConnectableDisacoverableMode() {
        ReflectUtil.inVoke(mBluetoothAdapter, ReflectUtil.getMethod(BluetoothAdapter::class.java.canonicalName,
                "setScanMode", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType), BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 0)
    }


}

