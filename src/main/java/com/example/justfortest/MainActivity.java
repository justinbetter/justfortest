package com.example.justfortest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.RecoverySystem;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.justfortest.rom.Mock;
import com.example.justfortest.rom.RomInfoResult;
import com.example.justfortest.rom.RomTask;
import com.example.justfortest.rom.RomUpdateStateMachine;
import com.example.justfortest.utils.CommonUtils;
import com.example.justfortest.utils.RxUtils;
import com.tuyou.tsd.common.Version;
import com.tuyou.tsd.common.activity.dialog.model.DialogEvent;
import com.tuyou.tsd.common.collect.CollectEvents;
import com.tuyou.tsd.common.collect.CollectManager;
import com.tuyou.tsd.common.tts.TtsUtil;
import com.tuyou.tsd.common.util.ACache;
import com.tuyou.tsd.common.util.DialogUtil;
import com.tuyou.tsd.common.util.L;
import com.tuyou.tsd.common.util.SdScan;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends Activity implements View.OnClickListener {

    private String TAG = "justin";
    private boolean _isRomNotice;
    private RomUpdateStateMachine romUpdateStateMachine;
    private String ROMTASK_SAVE_STRING = "RomInfoResult";
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int[] clickIds = {R.id.button_default,R.id.button_prepare,R.id.button_download,R.id.button_verify,R.id.button_install};
        for (int buttonId : clickIds) {
            findViewById(buttonId).setOnClickListener(this);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bspatch:
                testNewRomInfo();
                break;
            case R.id.button_default:
                romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_DEFAULT));
                break;
            case R.id.button_prepare:
                romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_PREPARE));
                break;
            case R.id.button_download:
                romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_DOWNLOAD));
                break;
            case R.id.button_verify:
                romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_VERIFY));
                break;
            case R.id.button_install:
                romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_INSTALL));
                break;

        }

    }

    public void doPatch(View view)  {
        testNewRomInfo();
    }

    //收到DialogEvent
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void handleDialogEvent(DialogEvent event) {
        if (event.event == DialogEvent.DIALOG_EVENT_OK) {
            //启动状态机 下载 校验 升级
            if (romUpdateStateMachine == null) {
                romUpdateStateMachine = new RomUpdateStateMachine(new RomUpdateStateMachine.OnStateChangeListener() {
                    @Override
                    public void onStateChange(int state) {
                        L.i("state: " + RomUpdateStateMachine.getStateDescription(state));
                        if (state == RomUpdateStateMachine.CMD_DEFAULT) {
                            updateLocalRomTask(RomUpdateStateMachine.CMD_DEFAULT, "进入默认状态");
//                            romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_PREPARE));
                        } else if (state == RomUpdateStateMachine.CMD_PREPARE) {
                            updateLocalRomTask(RomUpdateStateMachine.CMD_PREPARE, "进入预备状态");
                            romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_DOWNLOAD));
                        } else if (state == RomUpdateStateMachine.CMD_DOWNLOAD) {
                            //FIXME 下载逻辑
                            updateLocalRomTask(RomUpdateStateMachine.CMD_DOWNLOAD, "进入下载状态");
                            romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_VERIFY));
                        } else if (state == RomUpdateStateMachine.CMD_VERIFY) {
                            //Verify size md5
                            updateLocalRomTask(RomUpdateStateMachine.CMD_VERIFY, "进入校验状态");
                            String filepath = Environment.getExternalStorageDirectory().toString() + File.separator + "factory_update.zip";
                            RomTask localRomTask = getLocakRomTask();
                            boolean isFileVaild = CommonUtils.validFile(filepath, localRomTask.romInfoResult.rominfo.rom_map.size, localRomTask.romInfoResult.rominfo.rom_map.md5);
                            if (isFileVaild) {
                                romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_INSTALL));
                            } else {
                                //校验错误，删除文件，进入默认
                                //TODO 这里暂时不删除
                                romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_DEFAULT));
                            }

                        } else if (state == RomUpdateStateMachine.CMD_INSTALL) {
                            //TODO 更新rom
                            romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_DEFAULT));
                        }

                    }
                });

            }
            romUpdateStateMachine.getHandler().sendMessage(romUpdateStateMachine.obtainMessage(RomUpdateStateMachine.CMD_PREPARE));
        }
    }

    private void testNewRomInfo() {
        _isRomNotice = false;

        _checkRomVersion()
                .subscribeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RxUtils.RetryWithDelay(6, 10))
                .filter(new Func1<RomInfoResult, Boolean>() {
                    @Override
                    public Boolean call(RomInfoResult romInfoResult) {
                        return romInfoResult != null && romInfoResult.status.code == 0;
                    }
                })
                .subscribe(new Action1<RomInfoResult>() {
                    @Override
                    public void call(RomInfoResult romInfoResult) {
                        _handleRomInfoResult(romInfoResult);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.i("error:" + throwable.toString());
                    }
                });


    }

    private Observable<RomInfoResult> _checkRomVersion() {
        Observable<RomInfoResult> ret;
        Mock._Debug debug = Mock.getInstance().getMockDebug("check_rom");
        if (debug != null) {
            if (debug.scene.equals(Mock.REAL)) {
                ret = _real_checkRomVersion();
            } else {
                if (debug.fake_ret.equals("success")) {
                    ret = _fake_checkRomVersion("success");
                } else {
                    ret = _fake_checkRomVersion(null);
                }
            }
        } else {
            ret = _real_checkRomVersion();
        }
        return ret;
    }

    private Observable<RomInfoResult> _fake_checkRomVersion(final String success) {
        L.i("fake checkRomVersion");
        return Observable.create(new Observable.OnSubscribe<RomInfoResult>() {
            @Override
            public void call(Subscriber<? super RomInfoResult> subscriber) {
                try {
                    String fakeRomSuccess = "{\"status\": {\"code\": 0}, \"rominfo\": {\"tts\": \"奥特曼变身成功\", \"install_mode\": \"force\", \"play_count\": -1, \"rom_map\": {\"version\": \"xxxxx\", \"name\": \"factory_update.zip\", \"time\": \"1493020824\", \"url\": \"D:\\\\tempdir\\\\tinker_multi\\\\factory_update.zip\", \"size\": 324045340, \"version_code\": 170419, \"id\": \"dx0016\", \"md5\": \"e3bb15f778ac11148a3332c4dac6869c\", \"description\": \"xxx\"}, \"dialog_map\": {\"content\": \"升级后唤醒率更高, 识别更准确, 请立即升级\", \"button_negative_text\": \"下次点火提醒我\", \"button_positive_visual\": true, \"default_select\": \"positive\", \"button_positive_text\": \"去升级\", \"title\": \"ROM有新版本发布\", \"button_negative_visual\": true, \"auto_close\": true, \"auto_close_time\": 8}}, \"extend\": {\"LLA00XX_B01_170418\": {\"version_code\": \"170418\", \"extra2\": \"222\"}}}";
                    String fakeRomError = "{\"status\": {\"code\": -1}, \"rominfo\": {\"tts\": \"奥特曼变身失败\", \"install_mode\": \"force\", \"play_count\": -1, \"rom_map\": {\"version\": \"xxxxx\", \"name\": \"factory_update.zip\", \"time\": \"1493020824\", \"url\": \"D:\\\\tempdir\\\\tinker_multi\\\\factory_update.zip\", \"size\": 324045340, \"version_code\": 170419, \"id\": \"dx0016\", \"md5\": \"e3bb15f778ac11148a3332c4dac6869c\", \"description\": \"xxx\"}, \"dialog_map\": {\"content\": \"升级后唤醒率更高, 识别更准确, 请立即升级\", \"button_negative_text\": \"下次点火提醒我\", \"button_positive_visual\": true, \"default_select\": \"positive\", \"button_positive_text\": \"去升级\", \"title\": \"ROM有新版本发布\", \"button_negative_visual\": true, \"auto_close\": true, \"auto_close_time\": 8}}, \"extend\": {\"LLA00XX_B01_170418\": {\"version_code\": \"170418\", \"extra2\": \"222\"}}}";
                    RomInfoResult result = TextUtils.isEmpty(success) ? LoganSquare.parse(fakeRomError, RomInfoResult.class) :
                            LoganSquare.parse(fakeRomSuccess, RomInfoResult.class);
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Observable<RomInfoResult> _real_checkRomVersion() {
        return null;
    }

    private void _handleRomInfoResult(RomInfoResult netRomInfoResult) {
        try {
            String romInfoResultJson = ACache.get(getApplicationContext()).getAsString(ROMTASK_SAVE_STRING);
            RomTask localRomTask = TextUtils.isEmpty(romInfoResultJson) ? null : LoganSquare.parse(romInfoResultJson, RomTask.class);
            RomInfoResult localRomInfoResult = null;
            if (localRomTask != null) {
                localRomInfoResult = localRomTask.romInfoResult;
            }
            int currentRomVersionCode = __findRomVersionCode(netRomInfoResult, Version.ROM_VERSION);
            if (currentRomVersionCode >= 0) {
                if (localRomInfoResult == null && netRomInfoResult == null) {
                    L.i("no any rom update");
                } else if (localRomInfoResult == null) {
                    if (currentRomVersionCode > netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("never");
                    } else if (currentRomVersionCode == netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("current rom version code equal network rom version code");
                    } else if (currentRomVersionCode < netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("network rom has update");
                        _createRomTask(netRomInfoResult);
                    }
                } else if (netRomInfoResult == null) {
                    L.i("only local has rom info and show count:" + localRomInfoResult.rominfo.play_count);
                    _resumeLocalRomTask(localRomTask);
                } else {
                    L.i("local and network both have rom info, wait compare...");
                    if (localRomInfoResult.rominfo.rom_map.version_code > netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("never");
                    } else if (localRomInfoResult.rominfo.rom_map.version_code == netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("local rom info and net rom info are equal");
                        _resumeLocalRomTask(localRomTask);
                    } else if (localRomInfoResult.rominfo.rom_map.version_code < netRomInfoResult.rominfo.rom_map.version_code) {
                        L.i("local rom info small them net rom info");
                        _createRomTask(netRomInfoResult);
                    }
                }
            } else {
                L.e("unknown local rom version code");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _showRomDialog(RomInfoResult romInfoResult) {
        TtsUtil.playTts(getApplicationContext(),romInfoResult.rominfo.tts);

        int buttonFlag;
        if (romInfoResult.rominfo.dialog_map.button_negative_visual && romInfoResult.rominfo.dialog_map.button_positive_visual) {
            buttonFlag = DialogUtil.FLAG_ALL_BUTTON;
        } else if (romInfoResult.rominfo.dialog_map.button_negative_visual) {
            buttonFlag = DialogUtil.FLAG_NEGATIVE_BUTTON;
        } else if (romInfoResult.rominfo.dialog_map.button_positive_visual) {
            buttonFlag = DialogUtil.FLAG_POSITIVE_BUTTON;
        } else {
            buttonFlag = DialogUtil.FLAG_NONE_BUTTON;
        }

        int defaultSelect = romInfoResult.rominfo.dialog_map.default_select.equals("positive") ? DialogUtil.FLAG_POSITIVE_BUTTON : DialogUtil.FLAG_NEGATIVE_BUTTON;

        DialogUtil.showDialog(getApplicationContext(), 10, romInfoResult.rominfo.dialog_map.content,
                romInfoResult.rominfo.dialog_map.button_positive_text, romInfoResult.rominfo.dialog_map.button_negative_text,
                buttonFlag, defaultSelect, romInfoResult.rominfo.dialog_map.auto_close, romInfoResult.rominfo.dialog_map.auto_close_time * 1000);
        _isRomNotice = true;

    }

    private void _createRomTask(RomInfoResult romInfoResult) throws IOException {
        L.i("_createRomTask ——> " +romInfoResult);
        RomTask romTask = new RomTask(RomUpdateStateMachine.CMD_DEFAULT, "create RomTask", romInfoResult);
        ACache.get(getApplicationContext()).put(ROMTASK_SAVE_STRING, LoganSquare.serialize(romTask));
        CollectManager.postEvent(getApplicationContext(), CollectEvents.OTA.OTA_ROM, romInfoResult.rominfo.rom_map.version);
        _showRomDialog(romInfoResult);
    }

    private RomTask getLocakRomTask() {
        RomTask romTask = null;
        try {
            String romTaskJson = ACache.get(getApplicationContext()).getAsString(ROMTASK_SAVE_STRING);
            if (!TextUtils.isEmpty(romTaskJson)) {
                romTask = LoganSquare.parse(romTaskJson, RomTask.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return romTask;
    }

    private void updateLocalRomTask(int status, String description)  {
        try {
            L.i("updateLocalRomTask ——> " + status + "," + description);
            RomTask locakRomTask = getLocakRomTask();
            locakRomTask.status = status;
            locakRomTask.description = description;
            ACache.get(getApplicationContext()).put(ROMTASK_SAVE_STRING, LoganSquare.serialize(locakRomTask));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _resumeLocalRomTask(RomTask localRomTask) {
        /*
            default: showdialog
            beforedownload: download
            beforeverify: verify
            beforeintall: install ——>install done
            installdone: return
         */

        try {
            if (!_isRomNotice) {
                if (localRomTask.romInfoResult.rominfo.play_count == -1) {
                    _showRomDialog(localRomTask.romInfoResult);
                } else {
                    if (localRomTask.romInfoResult.rominfo.play_count > 0) {
                        _showRomDialog(localRomTask.romInfoResult);
                        localRomTask.romInfoResult.rominfo.play_count--;
                        ACache.get(getApplicationContext()).put(ROMTASK_SAVE_STRING,LoganSquare.serialize(localRomTask));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int __findRomVersionCode(RomInfoResult result, String romVersion) {
        int ret = -1;
        if (result != null && result.rominfo != null && result.extend != null) {
            Map<String, String> stringMap = result.extend.get(romVersion);
            if (stringMap != null) {
                try {
                    ret = Integer.parseInt(stringMap.get("version_code"));
                } catch (Exception e) {
                    e.printStackTrace();
                    L.e("version code has error");
                }
            }
        }
        return ret;
    }

    private void testRomInfo()  {
        String fakeRominfo = "{\"status\": {\"code\": 0}, \"rominfo\": {\"tts\": \"妈妈问我为什么跪着, 因为我要变身啦, 不更新啦, 哈哈哈\", \"install_mode\": \"force\", \"play_count\": -1, \"rom_map\": {\"version\": \"xxxxx\", \"name\": \"factory_update.zip\", \"time\": \"1493020824\", \"url\": \"D:\\\\tempdir\\\\tinker_multi\\\\factory_update.zip\", \"size\": 324045340, \"version_code\": \"123\", \"id\": \"dx0016\", \"md5\": \"e3bb15f778ac11148a3332c4dac6869c\", \"description\": \"xxx\"}, \"dialog_map\": {\"content\": \"升级后唤醒率更高, 识别更准确, 请立即升级\", \"button_negative_text\": \"下次点火提醒我\", \"button_positive_visual\": true, \"default_select\": \"positive\", \"button_positive_text\": \"去升级\", \"title\": \"ROM有新版本发布\", \"button_negative_visual\": true, \"auto_close\": true, \"auto_close_time\": 8}}, \"extend\": {\"extend1\": {\"extra1\": \"111\", \"extra2\": \"222\"}}}";
        RomInfoResult romInfoResult = null;
        try {
            romInfoResult = LoganSquare.parse(fakeRominfo, RomInfoResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("justin", "--> " + romInfoResult.toString());

    }


    private void testRecovery() {
        String extSDCardPath = SdScan.getExtSDCardPath();
        if (TextUtils.isEmpty(extSDCardPath)) {
            return;
        }
        String address = extSDCardPath + "/factory_update.zip";
        Log.e("justin", "path: " + address);
        try {
            File packageFile = new File(address);
            RecoverySystem.verifyPackage(packageFile, new RecoverySystem.ProgressListener() {
                @Override
                public void onProgress(int progress) {
                    Log.e("justin", "progress: " + progress);
                }
            },null);
            Log.e("justin", "verify done");
            RecoverySystem.installPackage(this, packageFile);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
