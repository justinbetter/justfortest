package com.justlab.redux.annotation;


import com.justlab.redux.state.DummyState;
import com.justlab.redux.state.State;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by XMD on 2016/6/22.
 * 定义reducer需要更新的State区域
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StateKey {
    Class<? extends State> value() default DummyState.class;
}
