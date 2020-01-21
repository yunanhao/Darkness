package com.mona.rpg.control;

import com.mona.rpg.model.IBaseEvent;
import com.mona.rpg.model.IBaseMap;
import com.mona.rpg.model.IDrawable;
import com.mona.rpg.model.IRole;
import com.mona.rpg.view.ICanvas;
import com.mona.rpg.view.IView;

public abstract class ControllerImpl implements IController {
    private IBaseMap gameMap;
    private IView view;
    private ICanvas canvas;

    @Override
    public IRole getRole() {
        return null;
    }

    @Override
    public void start() {
        IDrawable drawable = gameMap.getDrawable(200, 100);
        view.addMap(gameMap);
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

    }
}
