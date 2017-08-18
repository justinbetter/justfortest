package com.example.justfortest.ota

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.tuyou.tsd.common.TSDDevice
import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/8/15.
 */
class RomOtaInstaller(context: Context,configureForOTA: ConfigureForOTA, commonOTAStateMachine: CommonOTAStateMachine) : BaseOTAInstaller(context,configureForOTA, commonOTAStateMachine) {
    override fun onDefault() {
        super.onDefault()
        L.w("rom begin!")
    }

    override fun onQuery() {
        super.onQuery()
        /**
         *  1. check version
         *  2. 是否自动升级
         *  3. 是否第三方
         */
        //check version


        //if need update
        configureForOTA.apply {
            if (motivate == ConfigureForOTA.Constants.MOTIVATE_MANUAL) {
                //FIXME dialog

            } else {
                L.w("apply mode: $mode")
                when (mode) {
                    ConfigureForOTA.Constants.MODE_VENDOR -> {
                        // 走第三方
                        when (TSDDevice.TSDBrand.BRAND) {
                            TSDDevice.TSDBrand.ZHIXING    -> {
                                val intent = Intent(Intent.ACTION_MAIN)
                                intent.component = ComponentName("com.redstone.ota.ui", "com.redstone.ota.ui.activity.RsMainActivity")
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            }
                            else -> {
                                L.e("not implemented ${TSDDevice.TSDBrand.BRAND}")
                            }
                        }

                    }
                    else                           -> {
                        L.e("sorry...... this mode is not implemented :  $mode")
                    }
                }



            }


        }

//        L.w("query install success")
//        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_SUCCESS)
    }

    override fun onDownload() {
        super.onDownload()
    }

    override fun onVerify() {
        super.onVerify()
    }

    override fun onInstall() {
        super.onInstall()
    }

    override fun onInstallSuccess() {
        super.onInstallSuccess()
        commonOTAStateMachine.sendInstallerResetMessage(ConfigureForOTA(ConfigureForOTA.Constants.TYPE_APP,ConfigureForOTA.Constants.MODE_FULL_UPGRADE))
    }

    override fun onInstallFailure() {
        super.onInstallFailure()
    }
}
