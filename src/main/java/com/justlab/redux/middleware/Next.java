package com.justlab.redux.middleware;


import android.support.annotation.NonNull;

import com.justlab.redux.action.Action;


/**
 * Created by tuyou on 16/6/15.
 */
public interface Next<A extends Action> {
    void dispatch(@NonNull A action);
}
