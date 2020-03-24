package com.mona.rpg.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Role {
    private Image image;
    private int count;
    private int rate;
    private int type;
    private int x, y;

    public Role(Image image) {
        this.image = image;
    }

    public GraphicsContext onDraw(GraphicsContext mGraphics) {
        mGraphics.save();
        mGraphics.drawImage(image, 32 * count, type, 32, 48, x, y, 64, 96);
        System.out.println(count);
        rate++;
        if (rate % 8 == 0) {
            if (count > 2)
                count = 0;
            else count++;
        }
        mGraphics.restore();
        return mGraphics;
    }
}
