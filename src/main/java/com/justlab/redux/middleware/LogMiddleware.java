package com.justlab.redux.middleware;

import com.justlab.redux.Store;
import com.justlab.redux.action.Action;
import com.justlab.redux.state.State;
import com.tuyou.tsd.common.util.L;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by tuyou on 16/6/17.
 * Append to last two
 */
public class LogMiddleware<A extends Action> implements Middleware<A> {
    public LogMiddleware() {
        try{
            Store.getInstance().subscribe(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void dispatch(Store store, A action, Next<A> next) {
        L.d("--> " + action.toString());
        next.dispatch(action);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onStateChange(State state){
        L.w("onStateChange " + state.toString());
    }
}