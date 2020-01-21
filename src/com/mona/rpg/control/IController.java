package com.mona.rpg.control;

import com.mona.rpg.model.IBaseEvent;
import com.mona.rpg.model.IRole;

/**
 * 控制器
 */
public interface IController extends Runnable {
    IRole getRole();

    void start();

    void pause();

    void stop();

    void run();

    void postEvent(IBaseEvent event);

    void postEvent(String tag, int x, int y);

    IBaseEvent createEvent(String tag, int x, int y);
}
