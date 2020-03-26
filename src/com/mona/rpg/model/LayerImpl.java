package com.mona.rpg.model;

import java.util.List;

public class LayerImpl implements ILayer {
    List<IActor> actorList;

    public void add(IActor actor) {
        actorList.add(actor);
    }
}
