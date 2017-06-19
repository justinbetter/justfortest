package com.example.justfortest

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.justfortest.bluetooth.BluetoothHeadsetService
import com.example.justfortest.bluetooth.BluetoothServiceBefore

/**
 * Created by justi on 2017/6/15.
 */
class MainActivity : Activity(), View.OnClickListener {


    var mEtText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.let {
            (findViewById(R.id.title) as TextView).text = bluetoothAdapter.name
        }
        mEtText = findViewById(R.id.et_number_text) as EditText?
        val ids = intArrayOf(R.id.btn_call, R.id.btn_accept, R.id.btn_refuse, R.id.btn_terminal)
        for (id in ids) {
            findViewById(id).setOnClickListener(this)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_call   -> sendHeadsetCallAction(mEtText?.text.toString())
            else            -> return
        }
    }

    private fun sendHeadsetCallAction(number:String) {
        val intent = Intent(this@MainActivity, BluetoothHeadsetService::class.java)
        intent.action = BluetoothServiceBefore.HEADSET_ACTION_CALL
        intent.putExtra("number", number)
        startService(intent)
    }




}