package com.justlab.redux.action;

/**
 * Created by tuyou on 16/6/17.
 * 耗时任务
 */
abstract public class AsyncAction<T> extends Action<T> {
    public AsyncAction(String action, T parameter) {
        super(action, parameter);
    }
}
