package com.example.justfortest.ota

import android.os.Message

/**
 * Created by justi on 2017/8/14.
 */
class OTAController {

    //--------------read me--------------//
    /**
     * common：
     *  1. 升级类型     type:"rom","app","patch"
     *  2. 升级模式     mode: "full","bsdiff","vendor"
     *  3. 配置文件
     *  4. 通用流程     query-> download-> verify-> install->result(success/fail)
     */

    private var otaStrategy: BaseOTAStrategy = AppOtaStrategy()

    constructor(configureForOTA: ConfigureForOTA){
        with(configureForOTA) {
            otaStrategy = when (type) {
                ConfigureForOTA.Constants.TYPE_APP -> {
                    AppOtaStrategy(this)
                }

                else                               -> {
                    AppOtaStrategy(this)
                }
            }

        }

    }

    fun start() {
        otaStrategy.start()
    }

    fun sendMessage(what: Int) {
        otaStrategy.commonOTAStateMachine.sendMessage(what)
    }


    fun sendMessage(msg: Message) {
        otaStrategy.commonOTAStateMachine.sendMessage(msg)
    }


}