package com.mona.common.reflect;

public interface Callback<T, R> {
    R onCall(T t);
}
