package com.example.justfortest.ota

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.bluelinelabs.logansquare.LoganSquare
import com.example.justfortest.rom.Mock
import com.example.justfortest.rom.RomInfoResult
import com.example.justfortest.rom.RomTask
import com.example.justfortest.utils.RxUtils
import com.justlab.log.JLog
import com.tuyou.tsd.common.TSDDevice
import com.tuyou.tsd.common.Version
import com.tuyou.tsd.common.collect.CollectEvents
import com.tuyou.tsd.common.collect.CollectManager
import com.tuyou.tsd.common.tts.TtsUtil
import com.tuyou.tsd.common.util.ACache
import com.tuyou.tsd.common.util.DialogUtil
import com.tuyou.tsd.common.util.L
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.io.IOException

/**
 * Created by justi on 2017/8/15.
 */
class RomOtaInstaller(context: Context,configureForOTA: ConfigureForOTA, commonOTAStateMachine: CommonOTAStateMachine) : BaseOTAInstaller(context,configureForOTA, commonOTAStateMachine) {

    private val ROMTASK_SAVE_STRING = "RomInfoResult"
    private var _isRomNotice = false

    override fun onDefault() {
        super.onDefault()
        L.w("rom begin!")
        _removeRomTask()
    }

    override fun onQuery() {
        super.onQuery()
        /**
         *  1. check version
         *  2. 是否自动升级
         *  3. 是否第三方
         */
        //TODO check version
        _checkRomVersion()
                .subscribeOn(AndroidSchedulers.mainThread())
                .retryWhen(RxUtils.RetryWithDelay(6, 10))
                .filter { romInfoResult ->
                    L.d(TAG,"romInfoResult:$romInfoResult")
                    romInfoResult != null && romInfoResult.status.code == 0 }
                .subscribe({ romInfoResult -> _handleRomInfoResult(romInfoResult) }) { throwable -> L.i("error:" + throwable.toString()) }



//        L.w("query install success")
//        commonOTAStateMachine.sendMessage(CommonOTAStateMachine.MESSAGE_INSTALL_SUCCESS)
    }

    private fun applyConfigure(romTask: RomTask) {
        configureForOTA.apply {
            if (motivate == ConfigureForOTA.Constants.MOTIVATE_MANUAL) {
                //FIXME dialog
                _showRomDialog(romTask.romInfoResult)

            } else {
                L.w("apply mode: $mode")
                when (mode) {
                    ConfigureForOTA.Constants.MODE_VENDOR       -> {
                        // 走第三方
                        when (TSDDevice.TSDBrand.BRAND) {
                            TSDDevice.TSDBrand.ZHIXING -> {
                                val intent = Intent(Intent.ACTION_MAIN)
                                intent.component = ComponentName("com.redstone.ota.ui", "com.redstone.ota.ui.activity.RsMainActivity")
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                                sendDefaultMessage()
                            }
                            else                       -> {
                                L.e("not implemented ${TSDDevice.TSDBrand.BRAND}")
                            }
                        }

                    }
                    ConfigureForOTA.Constants.MODE_FULL_UPGRADE -> {
                        //enter download
                        sendDownloadMessage()

                    }
                    else                                        -> {
                        L.e("sorry...... this mode is not implemented :  $mode")
                    }
                }


            }


        }
    }

    override fun onDownload() {
        super.onDownload()
        sendVerifyMessage()
    }

    override fun onVerify() {
        super.onVerify()
        sendInstallMessage()
    }

    override fun onInstall() {
        super.onInstall()
        sendInstallSuccessMessage()
    }

    override fun onInstallSuccess() {
        super.onInstallSuccess()
        commonOTAStateMachine.sendInstallerResetMessage(ConfigureForOTA(ConfigureForOTA.Constants.TYPE_APP,ConfigureForOTA.Constants.MODE_FULL_UPGRADE))
    }

    override fun onInstallFailure() {
        super.onInstallFailure()
    }


    private fun _checkRomVersion(): Observable<RomInfoResult> {
        val ret: Observable<RomInfoResult>
        val debug = Mock.getInstance().getMockDebug("check_rom")
        if (debug != null) {
            if (debug.scene == Mock.REAL) {
                ret = _real_checkRomVersion()
            } else {
                if (debug.fake_ret == "success") {
                    ret = _fake_checkRomVersion("success")
                } else {
                    ret = _fake_checkRomVersion(null)
                }
            }
        } else {
            ret = _real_checkRomVersion()
        }
        return ret
    }

    @Throws(IOException::class) private fun _createRomTask(romInfoResult: RomInfoResult) {
        L.i("_createRomTask ——> " + romInfoResult)
        val romTask = RomTask(CommonOTAStateMachine.MESSAGE_DEFAULT, "create RomTask", romInfoResult)
        ACache.get(context).put(ROMTASK_SAVE_STRING, LoganSquare.serialize(romTask))
        CollectManager.postEvent(context, CollectEvents.OTA.OTA_ROM, romInfoResult.rominfo.rom_map.version)
        //if need update TODO 根据rominfo，更新configure'
        applyConfigure(romTask)
        //        _showRomDialog(romInfoResult)

    }
    @Throws(IOException::class) private fun _removeRomTask() {
        L.i("_removeRomTask ——> ")
        ACache.get(context).remove(ROMTASK_SAVE_STRING)

    }
    private fun _resumeLocalRomTask(localRomTask: RomTask?) {
        /*
            default: showdialog
            beforedownload: download
            beforeverify: verify
            beforeintall: install ——>install done
            installdone: return
         */

        when (localRomTask?.status) {
            CommonOTAStateMachine.MESSAGE_DEFAULT    -> {
                sendDefaultMessage()
            }
            CommonOTAStateMachine.MESSAGE_QUERY     ->{
                sendQueryMessage()
            }
            CommonOTAStateMachine.MESSAGE_DOWNLOAD     ->{
                sendDownloadMessage()
            }
            CommonOTAStateMachine.MESSAGE_VERIFY     ->{
                sendVerifyMessage()
            }
            CommonOTAStateMachine.MESSAGE_INSTALL     ->{
                sendInstallMessage()
            }
            CommonOTAStateMachine.MESSAGE_INSTALL_SUCCESS     ->{
                sendInstallSuccessMessage()
            }
            CommonOTAStateMachine.MESSAGE_INSTALL_FAILURE     ->{
                sendInstallFailuerMessage()
            }

            else -> {
                L.e(TAG,"not resume task for $localRomTask")
            }
        }
//        localRomTask?.romInfoResult?.let {
//            if (!_isRomNotice) {
//                if (it.rominfo?.play_count == -1) {
//                    _showRomDialog(localRomTask.romInfoResult)
//                    L.i("[$localRomTask]")
//                } else {
//                    if (it.rominfo.play_count > 0) {
//                        _showRomDialog(it)
//                        it.rominfo.play_count--
//                        ACache.get(context).put(ROMTASK_SAVE_STRING, LoganSquare.serialize(localRomTask))
//                    }
//                }
//            }
//
//        }


    }

    private fun _handleRomInfoResult(netRomInfoResult: RomInfoResult?) {
        JLog.d("handleRomInfoResult: $netRomInfoResult")
        try {
            val romInfoResultJson = ACache.get(context).getAsString(ROMTASK_SAVE_STRING)
            val localRomTask = if (TextUtils.isEmpty(romInfoResultJson)) null else LoganSquare.parse<RomTask>(romInfoResultJson, RomTask::class.java)
            var localRomInfoResult: RomInfoResult? = null
            if (localRomTask != null) {
                localRomInfoResult = localRomTask!!.romInfoResult
            }
            val currentRomVersionCode = __findRomVersionCode(netRomInfoResult, Version.ROM_VERSION)
            if (currentRomVersionCode >= 0) {
                if (localRomInfoResult == null && netRomInfoResult == null) {
                    L.i("no any rom update")
                } else if (localRomInfoResult == null) {
                    if (currentRomVersionCode > netRomInfoResult!!.rominfo.rom_map.version_code) {
                        L.i("never")
                    } else if (currentRomVersionCode == netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("current rom version code equal network rom version code")
                    } else if (currentRomVersionCode < netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("network rom has update")
                        _createRomTask(netRomInfoResult)
                    }
                } else if (netRomInfoResult == null) {
                    L.i("only local has rom info and show count:" + localRomInfoResult.rominfo.play_count)
                    _resumeLocalRomTask(localRomTask)
                } else {
                    L.i("local and network both have rom info, wait compare...")
                    if (localRomInfoResult.rominfo.rom_map.version_code > netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("never")
                    } else if (localRomInfoResult.rominfo.rom_map.version_code == netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("local rom info and net rom info are equal")
                        _resumeLocalRomTask(localRomTask)
                    } else if (localRomInfoResult.rominfo.rom_map.version_code < netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("local rom info small them net rom info")
                        _createRomTask(netRomInfoResult)
                    }
                }
            } else {
                L.e("unknown local rom version code")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun _fake_checkRomVersion(success: String?): Observable<RomInfoResult> {
        L.i("fake checkRomVersion")
        return Observable.create { subscriber ->
            try {
                val fakeRomSuccess = "{\"status\": {\"code\": 0}, \"rominfo\": {\"tts\": \"奥特曼变身成功\", \"install_mode\": \"force\", \"play_count\": -1, \"rom_map\": {\"version\": \"xxxxx\", \"name\": \"factory_update.zip\", \"time\": \"1493020824\", \"url\": \"D:\\\\tempdir\\\\tinker_multi\\\\factory_update.zip\", \"size\": 324045340, \"version_code\": 170419, \"id\": \"dx0016\", \"md5\": \"e3bb15f778ac11148a3332c4dac6869c\", \"description\": \"xxx\"}, \"dialog_map\": {\"content\": \"升级后唤醒率更高, 识别更准确, 请立即升级\", \"button_negative_text\": \"下次点火提醒我\", \"button_positive_visual\": true, \"default_select\": \"positive\", \"button_positive_text\": \"去升级\", \"title\": \"ROM有新版本发布\", \"button_negative_visual\": true, \"auto_close\": true, \"auto_close_time\": 8}}, \"extend\": {\"B1405.01.01.01.11\": {\"version_code\": \"170418\", \"extra2\": \"222\"}}}"
                val fakeRomError = "{\"status\": {\"code\": -1}, \"rominfo\": {\"tts\": \"奥特曼变身失败\", \"install_mode\": \"force\", \"play_count\": -1, \"rom_map\": {\"version\": \"xxxxx\", \"name\": \"factory_update.zip\", \"time\": \"1493020824\", \"url\": \"D:\\\\tempdir\\\\tinker_multi\\\\factory_update.zip\", \"size\": 324045340, \"version_code\": 170419, \"id\": \"dx0016\", \"md5\": \"e3bb15f778ac11148a3332c4dac6869c\", \"description\": \"xxx\"}, \"dialog_map\": {\"content\": \"升级后唤醒率更高, 识别更准确, 请立即升级\", \"button_negative_text\": \"下次点火提醒我\", \"button_positive_visual\": true, \"default_select\": \"positive\", \"button_positive_text\": \"去升级\", \"title\": \"ROM有新版本发布\", \"button_negative_visual\": true, \"auto_close\": true, \"auto_close_time\": 8}}, \"extend\": {\"B1405.01.01.01.11\": {\"version_code\": \"170418\", \"extra2\": \"222\"}}}"
                val result = if (TextUtils.isEmpty(success)) LoganSquare.parse(fakeRomError, RomInfoResult::class.java)
                else LoganSquare.parse(fakeRomSuccess, RomInfoResult::class.java)
                subscriber.onNext(result)
                subscriber.onCompleted()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun _real_checkRomVersion(): Observable<RomInfoResult> {
        L.i("success checkRomVersion")
        return Observable.create { subscriber ->
            try {
                val success: String = "success"
                val fakeRomSuccess = "{\"status\": {\"code\": 0}, \"rominfo\": {\"tts\": \"奥特曼变身成功\", \"install_mode\": \"force\", \"play_count\": -1, \"rom_map\": {\"version\": \"xxxxx\", \"name\": \"factory_update.zip\", \"time\": \"1493020824\", \"url\": \"D:\\\\tempdir\\\\tinker_multi\\\\factory_update.zip\", \"size\": 324045340, \"version_code\": 170419, \"id\": \"dx0016\", \"md5\": \"e3bb15f778ac11148a3332c4dac6869c\", \"description\": \"xxx\"}, \"dialog_map\": {\"content\": \"升级后唤醒率更高, 识别更准确, 请立即升级\", \"button_negative_text\": \"下次点火提醒我\", \"button_positive_visual\": true, \"default_select\": \"positive\", \"button_positive_text\": \"去升级\", \"title\": \"ROM有新版本发布\", \"button_negative_visual\": true, \"auto_close\": true, \"auto_close_time\": 8}}, \"extend\": {\"B1405.01.01.01.11\": {\"version_code\": \"170418\", \"extra2\": \"222\"}}}"
                val fakeRomError = "{\"status\": {\"code\": -1}, \"rominfo\": {\"tts\": \"奥特曼变身失败\", \"install_mode\": \"force\", \"play_count\": -1, \"rom_map\": {\"version\": \"xxxxx\", \"name\": \"factory_update.zip\", \"time\": \"1493020824\", \"url\": \"D:\\\\tempdir\\\\tinker_multi\\\\factory_update.zip\", \"size\": 324045340, \"version_code\": 170419, \"id\": \"dx0016\", \"md5\": \"e3bb15f778ac11148a3332c4dac6869c\", \"description\": \"xxx\"}, \"dialog_map\": {\"content\": \"升级后唤醒率更高, 识别更准确, 请立即升级\", \"button_negative_text\": \"下次点火提醒我\", \"button_positive_visual\": true, \"default_select\": \"positive\", \"button_positive_text\": \"去升级\", \"title\": \"ROM有新版本发布\", \"button_negative_visual\": true, \"auto_close\": true, \"auto_close_time\": 8}}, \"extend\": {\"B1405.01.01.01.11\": {\"version_code\": \"170418\", \"extra2\": \"222\"}}}"
                val result = if (TextUtils.isEmpty(success)) LoganSquare.parse(fakeRomError, RomInfoResult::class.java)
                else LoganSquare.parse(fakeRomSuccess, RomInfoResult::class.java)
                subscriber.onNext(result)
                subscriber.onCompleted()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun __findRomVersionCode(result: RomInfoResult?, romVersion: String): Int {
        var ret = -1
        if (result != null && result.rominfo != null && result.extend != null) {
            val stringMap = result.extend[romVersion]
            if (stringMap != null) {
                try {
                    ret = Integer.parseInt(stringMap["version_code"])
                } catch (e: Exception) {
                    e.printStackTrace()
                    L.e("version code has error")
                }

            }
        }
        return ret
    }


    private fun _showRomDialog(romInfoResult: RomInfoResult) {
        TtsUtil.playTts(context, romInfoResult.rominfo.tts)

        val buttonFlag: Int
        if (romInfoResult.rominfo.dialog_map.button_negative_visual && romInfoResult.rominfo.dialog_map.button_positive_visual) {
            buttonFlag = DialogUtil.FLAG_ALL_BUTTON
        } else if (romInfoResult.rominfo.dialog_map.button_negative_visual) {
            buttonFlag = DialogUtil.FLAG_NEGATIVE_BUTTON
        } else if (romInfoResult.rominfo.dialog_map.button_positive_visual) {
            buttonFlag = DialogUtil.FLAG_POSITIVE_BUTTON
        } else {
            buttonFlag = DialogUtil.FLAG_NONE_BUTTON
        }

        val defaultSelect = if (romInfoResult.rominfo.dialog_map.default_select == "positive") DialogUtil.FLAG_POSITIVE_BUTTON else DialogUtil.FLAG_NEGATIVE_BUTTON

        DialogUtil.showDialog(context, 10, romInfoResult.rominfo.dialog_map.content, romInfoResult.rominfo.dialog_map.button_positive_text, romInfoResult.rominfo.dialog_map.button_negative_text, buttonFlag, defaultSelect, romInfoResult.rominfo.dialog_map.auto_close, (romInfoResult.rominfo.dialog_map.auto_close_time * 1000).toLong())
        _isRomNotice = true

    }
}
