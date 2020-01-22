package com.mona.rpg.control;

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
