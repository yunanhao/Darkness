package com.mona.rpg.control;

import com.mona.rpg.view.Container;

import java.util.ArrayList;
import java.util.List;

public class ControllerImpl implements IController {
    private static volatile IController controller;

    private IBaseMap gameMap;
    private List<IBaseEvent> eventList;
    private List<IBaseListener> listenerList;
    private Container container;

    private ControllerImpl() {
        container = new Container();
        gameMap = BaseMapImpl.getInstance();
        eventList = new ArrayList<>();
    }

    public static IController getInstance() {
        synchronized (IController.class) {
            if (controller == null) {
                synchronized (IController.class) {
                }
                {
                    controller = new ControllerImpl();
                }
            }
        }
        return controller;
    }

    @Override
    public void start() {
        gameMap.init();
        container.init();
        new Thread() {
            @Override
            public void run() {
                container.repaint();
            }
        }.start();
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void run() {

    }

    @Override
    public void postEvent(IBaseEvent event) {
        eventList.add(event);
        event.run();
    }

}
