package com.mona.rpg.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Role implements IActor {
    private Image image;
    private Image cur;
    private Image direction;
    private int count;
    private int rate;
    private int type;
    private int x, y;

    public Role(Image image) {
        this.image = image;
        cur = new Image("file:///../res/方向指示器.png", false);
        direction = new Image("file:///../res/direction.png", false);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GraphicsContext onDraw(GraphicsContext mGraphics) {
        mGraphics.save();
        mGraphics.translate(x + 32, y + 90);
        mGraphics.scale(0.14, 0.07);
        mGraphics.rotate(rate);
        mGraphics.drawImage(cur, -cur.getWidth() / 2, -cur.getHeight() / 2);
        mGraphics.restore();
        mGraphics.drawImage(image, 32 * count, type, 32, 48, x + 16, y + 48, 32, 48);
        rate++;
        if (rate % 12 == 0) {
            if (count > 2)
                count = 0;
            else count++;
        }
        mGraphics.save();
        mGraphics.translate(x + 32, y + 90);
        mGraphics.rotate(rate);
        mGraphics.drawImage(direction, -direction.getWidth() / 2, -direction.getHeight() / 2);
        mGraphics.restore();
        return mGraphics;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }
}
