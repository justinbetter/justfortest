package com.example.justfortest.ota

import com.tuyou.tsd.common.util.L

/**
 * Created by justi on 2017/8/15.
 */
class AppOtaInstaller(configureForOTA: ConfigureForOTA = ConfigureForOTA()) : BaseOTAInstaller(configureForOTA) {

    override fun onEnterState(state: CommonOTAStateMachine.BaseState) {
        when (state) {
            is CommonOTAStateMachine.Default    -> {
            }
            is CommonOTAStateMachine.Query  ->{
                //
                L.d("查询")
                sendMessage(CommonOTAStateMachine.MESSAGE_DOWNLOAD)
            }
            is CommonOTAStateMachine.Download ->{
                L.e("下载失败，回到默认")
                sendMessage(CommonOTAStateMachine.MESSAGE_DEFAULT)
            }
            else -> {
                L.e("not handle enter : $state")
            }
        }

    }

    override fun start() {
        super.start()

    }




}
