package com.example.justfortest.rom;

import android.os.Message;

import com.tuyou.tsd.common.statemachine.State;
import com.tuyou.tsd.common.statemachine.StateMachine;

/**
 * Created by justi on 2017/4/26.
 */

public class RomUpdateStateMachine extends StateMachine {
    /*
            default
                |
            —————————————————————————————————
           |              |                 |
      download          verify            install


     */

    private final DefaultState defaultState;
    private final PrepareState prepareState;
    private final DownloadState downloadState;
    private final VerifyState verifyState;
    private final InstallState installState;

    public static final int CMD_DEFAULT  = 1;
    public static final int CMD_PREPARE = 2;
    public static final int CMD_DOWNLOAD = 3;
    public static final int CMD_VERIFY   = 4;
    public static final int CMD_INSTALL  = 5;

    public static String getStateDescription(int state) {
        switch (state) {
            case CMD_DEFAULT :
                return "CMD_DEFAULT";
            case CMD_PREPARE :
                return "CMD_PREPARE";
            case CMD_DOWNLOAD:
                return "CMD_DOWNLOAD";
            case CMD_VERIFY  :
                return "CMD_VERIFY";
            case CMD_INSTALL :
                return "CMD_INSTALL";

        }
        return "unknown state";
    }

    private final OnStateChangeListener onStateChangeListener;

    public interface OnStateChangeListener {
        void onStateChange(int state);
    }

    public RomUpdateStateMachine(OnStateChangeListener onStateChangeListener) {
        super("RomUpdateStateMachine");
        this.onStateChangeListener = onStateChangeListener;
        addState(defaultState = new DefaultState());
        addState(prepareState = new PrepareState(),defaultState);
        addState(downloadState = new DownloadState(),defaultState);
        addState(verifyState = new VerifyState(),defaultState);
        addState(installState = new InstallState(), defaultState);
        setInitialState(defaultState);
        start();
    }

    //默认负责中转
    private class DefaultState extends State{
        @Override
        public void enter() {
            super.enter();
            onStateChangeListener.onStateChange(CMD_DEFAULT);
        }

        @Override
        public void exit() {
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case CMD_DEFAULT:
                    //myself
                    break;
                case CMD_PREPARE:
                    transitionTo(prepareState);
                    break;
                case CMD_DOWNLOAD:
                    transitionTo(downloadState);
                    break;
                case CMD_VERIFY:
                    transitionTo(verifyState);
                    break;
                case CMD_INSTALL:
                    transitionTo(installState);
                    break;
            }
            return HANDLED;
        }
    }

    //获取rominfo
    private class PrepareState extends State{
        @Override
        public void enter() {
            super.enter();
            onStateChangeListener.onStateChange(CMD_PREPARE);
        }

        @Override
        public void exit() {
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case CMD_DEFAULT:
                    transitionTo(defaultState);
                    break;
                case CMD_PREPARE:
                    break;
                case CMD_DOWNLOAD:
                    transitionTo(downloadState);
                    break;
                case CMD_VERIFY:
                case CMD_INSTALL:
                    break;
            }
            return HANDLED;
        }
    }


    //下载factory_update.zip
    private class DownloadState extends State{
        @Override
        public void enter() {
            super.enter();
            onStateChangeListener.onStateChange(CMD_DOWNLOAD);
        }

        @Override
        public void exit() {
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case CMD_DEFAULT:
                    transitionTo(defaultState);
                    break;
                case CMD_PREPARE:
                    break;
                case CMD_DOWNLOAD:
                    break;
                case CMD_VERIFY:
                    transitionTo(verifyState);
                    break;
                case CMD_INSTALL:
                    break;
            }
            return HANDLED;
        }
    }


    private class VerifyState extends State{
        @Override
        public void enter() {
            super.enter();
            onStateChangeListener.onStateChange(CMD_VERIFY);
        }

        @Override
        public void exit() {
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case CMD_DEFAULT:
                    transitionTo(defaultState);
                    break;
                case CMD_PREPARE:
                case CMD_DOWNLOAD:
                case CMD_VERIFY:
                    break;
                case CMD_INSTALL:
                    transitionTo(installState);
                    break;
            }
            return HANDLED;
        }
    }

    private class InstallState extends State{
        @Override
        public void enter() {
            super.enter();
            onStateChangeListener.onStateChange(CMD_INSTALL);
        }

        @Override
        public void exit() {
            super.exit();
        }

        @Override
        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case CMD_DEFAULT:
                    transitionTo(defaultState);
                    break;
                case CMD_PREPARE:
                case CMD_DOWNLOAD:
                case CMD_VERIFY:
                case CMD_INSTALL:
                    break;
            }
            return HANDLED;
        }
    }

}
