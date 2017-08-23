package com.justlab.redux.action;

/**
 * Created by tuyou on 16/6/17.
 */
abstract public class SyncAction<T> extends Action<T> {
    public SyncAction(String action, T parameter) {
        super(action, parameter);
    }
}
