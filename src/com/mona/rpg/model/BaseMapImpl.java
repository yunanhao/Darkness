package com.mona.rpg.model;

import com.mona.rpg.view.ICanvas;

public class BaseMapImpl implements IBaseMap {
    private static volatile IBaseMap baseMap;

    private int[][] data = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
            {0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
    };

    private IDrawable drawable;

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
        return data.length;
    }

    @Override
    public int getHeight() {
        return data[0].length;
    }

    @Override
    public void init() {
        drawable = new IDrawable() {
            @Override
            public void draw(ICanvas canvas) {
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[i].length; j++) {
                        switch (data[i][j]) {
                            case 0:
                                canvas.getPaint().setColor(0xffff0000);
                                break;
                            case 1:
                                canvas.getPaint().setColor(0xff00ff00);
                                break;
                            case 2:
                                canvas.getPaint().setColor(0xff0000ff);
                                break;
                            default:
                        }
                        canvas.drawRect(j * 32, i * 32, (j + 1) * 32 - 1, (i + 1) * 32 - 1);
                    }
                }
            }
        };
    }

    @Override
    public void update() {

    }

    @Override
    public IDrawable getDrawable() {
        return drawable;
    }
}
