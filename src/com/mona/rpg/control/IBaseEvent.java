package com.mona.rpg.control;

import com.mona.rpg.model.IActor;

/**
 * 事件对象
 * 事件ID 事件源 类型
 */
public interface IBaseEvent extends Runnable {

    int getIdentity();

    String getTag();

    int getType();

    long getTime();

    IActor getSource();

    void addListener(IBaseListener listener);

    boolean removeListener(IBaseListener listener);

    @Override
    void run();

    @Override
    String toString();

    @Override
    int hashCode();
}
