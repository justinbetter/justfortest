package com.justlab.redux;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.justlab.redux.action.Action;
import com.justlab.redux.annotation.StateKey;
import com.justlab.redux.state.State;


/**
 * Created by tuyou on 16/6/14.
 * @StateValue 定义reducer需要更新的State区域
 */
@StateKey
public interface Reducer<A extends Action, S extends State> {
    /**
     * reducer 一定是一个函数, 他负责状态的改变
     * 一个状态 + 动作 = 新状态
     * 不做耗时业务
     * @param action
     * @param state
     * @return null means no update
     */
    @Nullable
    S call(@NonNull A action, @NonNull StateTree state);
}
