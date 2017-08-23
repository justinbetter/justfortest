package com.justlab.redux;


import com.justlab.redux.action.Action;
import com.justlab.redux.annotation.StateKey;
import com.justlab.redux.state.DummyState;

/**
 * Created by XMD on 2016/6/23.
 */
@StateKey
public class DummyReducer implements Reducer<Action,DummyState> {
    @Override
    public DummyState call(Action action, StateTree state) {
        return new DummyState();
    }
}
