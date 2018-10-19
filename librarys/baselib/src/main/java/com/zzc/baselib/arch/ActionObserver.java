package com.zzc.baselib.arch;

import android.arch.lifecycle.Observer;

/**
 * @author Roye
 * @date 2018/7/5
 */
public interface ActionObserver<T> extends Observer<ActionEntity<T>> {

}