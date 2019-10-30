package com.mona.event;

import java.util.List;

public abstract class Runner implements Runnable {
    protected Message msg;
    /**
     * 处理对象的监听回调
     */
    protected List<Callback> callbacks;

    public abstract void handleMessage(Message msg);

    public final void setMessage(Message msg) {
        this.msg = msg;
    }

    public final void setCallback(List<Callback> callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public final void run() {
        handleMessage(msg);
        if (callbacks == null) {
            return;
        }
        // 调用回调函数
        for (Callback callback : callbacks) {
            callback.handleMessage(msg);
        }
    }
}
