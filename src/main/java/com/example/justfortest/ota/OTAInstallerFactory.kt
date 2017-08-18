package com.example.justfortest.ota

import android.content.Context
import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/8/14.
 */
object OTAInstallerFactory {

    //--------------read me--------------//
    /**
     * common：
     *  1. 升级类型     type:"rom","app","patch"
     *  2. 升级模式     mode: "full","bsdiff","vendor"
     *  3. 配置文件     ConfigureForOTA
     *  4. 通用流程     query-> download-> verify-> install->result(success/fail)
     */

    fun createInstaller(context: Context,configureForOTA: ConfigureForOTA,commonOTAStateMachine: CommonOTAStateMachine): BaseOTAInstaller? = when (configureForOTA.type) {
        ConfigureForOTA.Constants.TYPE_APP -> {
            AppOtaInstaller(configureForOTA,commonOTAStateMachine)
        }
        ConfigureForOTA.Constants.TYPE_ROM -> {
            RomOtaInstaller(configureForOTA,commonOTAStateMachine)
        }
        ConfigureForOTA.Constants.TYPE_PATCH -> {
            PatchOtaInstaller(configureForOTA,commonOTAStateMachine)
        }
        else                               -> {
            L.e("cannot  create installer $configureForOTA,return null!")
            null
        }
    }



}