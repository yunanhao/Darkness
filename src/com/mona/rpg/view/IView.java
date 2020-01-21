package com.mona.rpg.view;

import com.mona.rpg.model.IBaseMap;

/**
 * 视图对象
 */
public interface IView {
    void loadMap(IBaseMap map);

    void delMap(IBaseMap map);

    void addMap(IBaseMap map);
}
