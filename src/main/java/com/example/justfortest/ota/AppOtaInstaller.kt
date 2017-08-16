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
