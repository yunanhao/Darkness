package demo;

import java.awt.*;
import java.util.Random;

public class ShapeMoveable implements Runnable {
    public static final int SHAPE_OVAL = 0;
    public static final int SHAPE_RECT = 4;
    public static final int SHAPE_ROUNDRECT = 9;
    private final double mass;
    private final double gravity;
    public int x, y;
    public int width, height, arcWidth, arcHeight, shape;
    Color color;
    private int vx, vy;

    public ShapeMoveable(final int x, final int y, final int r, final int vx, final int vy, final double mass,
                         final double gravity) {
        this(x, y, r, r, 0, 0, SHAPE_OVAL, vx, vy, mass, gravity);
    }

    public ShapeMoveable(final int x, final int y, final int width, final int height, final int vx, final int vy,
                         final double mass, final double gravity) {
        this(x, y, width, height, 0, 0, SHAPE_RECT, vx, vy, mass, gravity);
    }

    /**
     * @param x         左上角横坐标
     * @param y         左上角纵坐标
     * @param width     宽度
     * @param height    高度
     * @param arcWidth  边角的水平弧度
     * @param arcHeight 边角的垂直弧度
     * @param shape     形状
     * @param vx        水平方向速度
     * @param vy        垂直方向速度
     * @param mass      质量
     * @param gravity   重力
     */
    public ShapeMoveable(final int x, final int y, final int width, final int height, final int arcWidth,
                         final int arcHeight, final int shape, final int vx, final int vy, final double mass, final double gravity) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.shape = shape;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.gravity = gravity;
        color = new Color(new Random().nextInt(0xffffff));
    }

    public ShapeMoveable(final int x, final int y, final int r, final int vx, final int vy) {
        this(x, y, r, r, 0, 0, SHAPE_OVAL, vx, vy, 1, 0);
    }

    public void crashOutside(final ShapeMoveable shape) {
        //TODO
//	double temp_speed = 0;
//	if (ShapeUtil.isCollision(x + vx, y + vy, width, height, shape.x + shape.vx, shape.y + shape.vy, shape.width,
//	    shape.height)) {
//	  if (x + width <= shape.x || x >= shape.x + shape.width) {
//		if (y + height > shape.y && y + height < shape.y + shape.height) {
//		  temp_speed = vx;
//		  vx = (int) (((mass - shape.mass) * temp_speed + 2 * shape.mass * shape.vx) / (mass + shape.mass));
//		  shape.vx = (int) (((shape.mass - mass) * shape.vx + 2 * mass * temp_speed) / (mass + shape.mass));
//		} else {
//		  temp_speed = vx;
//		  vx = (int) (((mass - shape.mass) * temp_speed + 2 * shape.mass * shape.vx) / (mass + shape.mass));
//		  shape.vx = (int) (((shape.mass - mass) * shape.vx + 2 * mass * temp_speed) / (mass + shape.mass));
//		  temp_speed = vy;
//		  vy = (int) (((mass - shape.mass) * temp_speed + 2 * shape.mass * shape.vy) / (mass + shape.mass));
//		  shape.vy = (int) (((shape.mass - mass) * shape.vy + 2 * mass * temp_speed) / (mass + shape.mass));
//		}
//	  } else if (y + height <= shape.y || y >= shape.y + shape.height) {
//		temp_speed = vy;
//		vy = (int) (((mass - shape.mass) * temp_speed + 2 * shape.mass * shape.vy) / (mass + shape.mass));
//		shape.vy = (int) (((shape.mass - mass) * shape.vy + 2 * mass * temp_speed) / (mass + shape.mass));
//	  }
//	}

    }

    public void draw(final Graphics g) {
        g.setColor(color);
        switch (shape) {
            case SHAPE_OVAL:
                g.fillOval(x, y, width, height);
                break;
            case SHAPE_RECT:
                g.fillRect(x, y, width, height);
                break;
            case SHAPE_ROUNDRECT:
                g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
                break;
            default:
                g.draw3DRect(x, y, width, height, true);
        }
    }

    private void move() {
        vy += gravity;
        x += vx;
        y += vy;
    }

    public void stop() {
        vx = 0;
        vy = 0;
    }

    public void moveInside(final int x, final int y, final int width, final int height) {
        if (this.x + vx <= x) {
            this.x = x;
            vx = -vx;
        } else if (this.x + this.width + vx >= x + width) {
            this.x = x + width - this.width;
            vx = -vx;
        }
        if (this.y + vy <= y) {
            this.y = y;
            vy = -vy;
        } else if (this.y + this.height + vy > y + height) {
            this.y = y + height - this.height;
            vy = -vy;
        }
        move();
    }

    @Override
    public void run() {

    }

}
