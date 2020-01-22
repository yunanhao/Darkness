package com.mona.rpg.view;


public class PaintImpl implements IPaint {
    private int color;

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int setColor(int color) {
        this.color ^= color;
        color ^= this.color;
        this.color ^= color;
        return color;
    }
}
