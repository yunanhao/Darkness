package com.mona.rpg.model.Interface;

/**
 * 基础地图
 */
public interface IBaseMap {

    int getWidth();

    int getHeight();

    void init();

    void update();

    IDrawable getDrawable();
}
