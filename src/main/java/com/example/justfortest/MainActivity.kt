package com.example.justfortest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.justfortest.R.id.number
import com.example.justfortest.bluetooth.BluetoothHeadsetService
import com.example.justfortest.bluetooth.TricheerBluetoothHeadsetController

/**
 * Created by justi on 2017/6/15.
 */
class MainActivity : Activity(), View.OnClickListener {


    var mEtText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.dialpad_fragment)
        setContentView(R.layout.activity_main)
//        initDialpad()
//        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//        bluetoothAdapter?.let {
//            (findViewById(R.id.title) as TextView).text = bluetoothAdapter.name
//        }
//        mEtText = findViewById(R.id.digits) as EditText?
//        val ids = intArrayOf(R.id.btn_audio, R.id.btn_sendtmf, R.id.btn_call, R.id.btn_accept, R.id.btn_refuse, R.id.btn_terminal)
//        for (id in ids) {
//            findViewById(id).setOnClickListener(this)
//        }





    }

//    private val mButtonIds = intArrayOf(R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.star, R.id.pound)

    private fun initDialpad() {

//        val numberIds = intArrayOf(R.string.dialpad_0_number, R.string.dialpad_1_number, R.string.dialpad_2_number,
//                                   R.string.dialpad_3_number, R.string.dialpad_4_number, R.string.dialpad_5_number,
//                                   R.string.dialpad_6_number, R.string.dialpad_7_number, R.string.dialpad_8_number,
//                                   R.string.dialpad_9_number, R.string.dialpad_star_number, R.string.dialpad_pound_number)
//        mButtonIds.forEachIndexed { index, mButtonId ->
//            val dialpadView = findViewById(mButtonId) as DialpadKeyButton
//            val numberTextView = dialpadView.findViewById(R.id.dialpad_key_number) as TextView
//            numberTextView.setText(numberIds[index])
//        }

    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_audio    -> startBtHeadsetService(TricheerBluetoothHeadsetController.HEADSET_ACTION_CONNECTAUDIO)
            R.id.btn_sendtmf  -> startBtHeadsetService(TricheerBluetoothHeadsetController.HEADSET_ACTION_SENDDTMF, "number", mEtText?.text.toString())
            R.id.btn_call     -> sendHeadsetCallAction(mEtText?.text.toString())
            R.id.accept       -> sendHeadsetAcceptAcion()
            R.id.btn_refuse   -> sendHeadsetRejectAcion()
            R.id.btn_terminal -> sendHeadsetTerminateAction()

            else              -> return
        }
    }

    private fun sendHeadsetCallAction(number: String) {
        startBtHeadsetService(TricheerBluetoothHeadsetController.HEADSET_ACTION_DIAL, "number", number)
    }

    private fun sendHeadsetAcceptAcion() {
        startBtHeadsetService(TricheerBluetoothHeadsetController.HEADSET_ACTION_ACCEPTCALL)
    }

    private fun sendHeadsetRejectAcion() {
        startBtHeadsetService(TricheerBluetoothHeadsetController.HEADSET_ACTION_REJECTCALL)
    }

    private fun sendHeadsetTerminateAction() {
        startBtHeadsetService(TricheerBluetoothHeadsetController.HEADSET_ACTION_TERMINATECALL)
    }

    private fun startBtHeadsetService(action: String) {
        val intent = Intent(this@MainActivity, BluetoothHeadsetService::class.java)
        intent.action = action
        intent.putExtra("number", number)
        startService(intent)
    }

    private fun startBtHeadsetService(action: String, name: String, value: String) {
        val intent = Intent(this@MainActivity, BluetoothHeadsetService::class.java)
        intent.action = action
        intent.putExtra(name, value)
        startService(intent)
    }


}