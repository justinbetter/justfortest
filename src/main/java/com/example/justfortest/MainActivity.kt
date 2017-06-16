package com.example.justfortest

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.example.justfortest.bluetooth.BluetoothService

/**
 * Created by justi on 2017/6/15.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.let {
            (findViewById(R.id.title) as TextView).text = bluetoothAdapter.name
        }
        startService(Intent(this@MainActivity, BluetoothService::class.java))



    }


}