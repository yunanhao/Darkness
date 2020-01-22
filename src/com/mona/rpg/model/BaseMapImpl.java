package com.mona.rpg.model;

public class BaseMapImpl implements IBaseMap {
    private static volatile IBaseMap baseMap;
    private int[][] data = {
            {2, 1},
            {2, 1}
    };

    private BaseMapImpl() {

    }

    public static IBaseMap getInstance() {
        synchronized (IBaseMap.class) {
            if (baseMap == null) {
                synchronized (IBaseMap.class) {
                }
                {
                    baseMap = new BaseMapImpl();
                }
            }
        }
        return baseMap;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public IDrawable getDrawable(int width, int height) {
        return null;
    }
}
