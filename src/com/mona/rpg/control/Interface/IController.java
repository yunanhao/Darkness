package com.mona.rpg.control.Interface;

/**
 * 控制器
 */
public interface IController extends Runnable {
    void start();

    void pause();

    void stop();

    void run();

    void postEvent(IBaseEvent event);

}
