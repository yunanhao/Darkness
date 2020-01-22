package com.mona.rpg.control;

import com.mona.rpg.model.BaseMapImpl;
import com.mona.rpg.model.IBaseMap;
import com.mona.rpg.view.Container;
import com.mona.rpg.view.IScene;

import java.util.ArrayList;
import java.util.List;

public class ControllerImpl implements IController {
    private IBaseMap gameMap;
    private static volatile IController controller;
    private List<IBaseEvent> eventList;
    private List<IBaseListener> listenerList;
    private List<IScene> sceneList;
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
        gameMap.getDrawable(200, 100);
        container.init();
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
