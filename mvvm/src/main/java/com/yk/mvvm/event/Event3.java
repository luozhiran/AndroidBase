package com.yk.mvvm.event;

public interface Event3<T,R> {
    R call(T t);
}
