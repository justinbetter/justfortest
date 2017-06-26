package com.example.justfortest

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.justfortest.R.id.number
import com.example.justfortest.bluetooth.BluetoothHeadsetService
import com.example.justfortest.bluetooth.TricheerBluetoothHeadsetController

/**
 * Created by justi on 2017/6/15.
 */
class MainActivity : Activity(), View.OnClickListener {


    var mEtText: EditText? = null
    var mEtText2: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.dialpad_fragment)
        setContentView(R.layout.activity_show)
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.let {
            (findViewById(R.id.title) as TextView).text = bluetoothAdapter.name
        }
        mEtText = findViewById(R.id.et_number_text) as EditText?
        mEtText2 = findViewById(R.id.et_number_text2) as EditText?
        val ids = intArrayOf(R.id.btn_audio,R.id.btn_sendtmf,R.id.btn_call, R.id.btn_accept, R.id.btn_refuse, R.id.btn_terminal)
        for (id in ids) {
            findViewById(id).setOnClickListener(this)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_audio    -> startBtHeadsetService(TricheerBluetoothHeadsetController.HEADSET_ACTION_CONNECTAUDIO)
            R.id.btn_sendtmf  -> startBtHeadsetService(TricheerBluetoothHeadsetController.HEADSET_ACTION_SENDDTMF,"number",mEtText2?.text.toString())
            R.id.btn_call     -> sendHeadsetCallAction(mEtText?.text.toString())
            R.id.accept       -> sendHeadsetAcceptAcion()
            R.id.btn_refuse   -> sendHeadsetRejectAcion()
            R.id.btn_terminal -> sendHeadsetTerminateAction()

            else            -> return
        }
    }

    private fun sendHeadsetCallAction(number:String) {
        startBtHeadsetService(TricheerBluetoothHeadsetController.HEADSET_ACTION_DIAL,"number",number)
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
    private fun startBtHeadsetService(action:String,name:String,value: String) {
        val intent = Intent(this@MainActivity, BluetoothHeadsetService::class.java)
        intent.action = action
        intent.putExtra(name, value)
        startService(intent)
    }


}