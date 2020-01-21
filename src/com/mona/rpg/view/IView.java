package com.mona.rpg.view;

import com.mona.rpg.model.IBaseMap;
import com.mona.rpg.model.IDrawable;

/**
 * 视图对象
 */
public interface IView extends IDrawable {
    void loadMap(IBaseMap map);

    void delMap(IBaseMap map);

    void addMap(IBaseMap map);
}
