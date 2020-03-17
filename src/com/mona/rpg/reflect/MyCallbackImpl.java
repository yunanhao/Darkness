package com.mona.rpg.reflect;

public class MyCallbackImpl implements Callback<String, String> {
    @Override
    public String onCall(String s) {
        System.out.println(s);
        return "hello";
    }
}
