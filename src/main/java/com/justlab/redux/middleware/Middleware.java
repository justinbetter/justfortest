package com.justlab.redux.middleware;


import android.support.annotation.NonNull;

import com.justlab.redux.Store;
import com.justlab.redux.action.Action;


/**
 * Created by tuyou on 16/6/15.
 */

public interface Middleware<A extends Action> {
    void dispatch(@NonNull Store store, @NonNull A action, @NonNull Next<A> next);
}
