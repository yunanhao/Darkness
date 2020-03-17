package com.mona.rpg.reflect;

public interface Callback<T, R> {
    R onCall(T t);
}
