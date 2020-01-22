package demo.util;

public final class Shapes {
    /**
     * 圆形碰撞
     *
     * @param x1 圆形1 的圆心X 坐标
     * @param y1 圆形2 的圆心X 坐标
     * @param x2 圆形1 的圆心Y 坐标
     * @param y2 圆形2 的圆心Y 坐标
     * @param r1 圆形1 的半径
     * @param r2 圆形2 的半径
     * @return
     */
    public static boolean isCollisionWithCircle(int x1, int y1, int x2, int y2,
                                                int r1, int r2) {
        return Math.hypot(x1 - x2, y1 - y2) <= r1 + r2;// 如果两圆的圆心距小于或等于两圆半径则认为发生碰撞
    }

    /**
     * 检测两个矩形区域是否发生了碰撞
     *
     * @param x1 第一个矩形的X
     * @param y1 第一个矩形的Y
     * @param w1 第一个矩形的宽
     * @param h1 第一个矩形的高
     * @param x2 第二个矩形的X
     * @param y2 第二个矩形的Y
     * @param w2 第二个矩形的宽
     * @param h2 第二个矩形的高
     */
    public static boolean isCrashOutside(int x1, int y1, int w1, int h1,
                                         int x2, int y2, int w2, int h2) {
        // java自身提供的方法
        // return x1 < w2 + x2 && x2 < w1 + x1 && y1 < h2 + y2 && y2 < h1 + y1;
        return (x1 > x2 ? x1 <= w2 + x2 : x2 <= w1 + x1)
                && (y1 > y2 ? y1 <= h2 + y2 : y2 <= h1 + y1);
    }

    /**
     * 检测两个矩形区域是否即将发生碰撞
     *
     * @param x1  第一个矩形的X
     * @param y1  第一个矩形的Y
     * @param w1  第一个矩形的宽
     * @param h1  第一个矩形的高
     * @param vx1 第一个矩形的水平速度
     * @param vy1 第一个矩形的垂直速度
     * @param x2  第二个矩形的X
     * @param y2  第二个矩形的Y
     * @param w2  第二个矩形的宽
     * @param h2  第二个矩形的高
     * @param vx2 第二个矩形的水平速度
     * @param vy2 第二个矩形的垂直速度
     */
    public static boolean isCrashOutside(int x1, int y1, int w1, int h1,
                                         int vx1, int vy1, int x2, int y2, int w2, int h2, int vx2, int vy2) {
        return isCrashOutside(x1 + vx1, y1 + vy1, w1, h1, x2 + vx2, y2 + vy2,
                w2, h2);
    }

    /**
     * 检测两个矩形区域是否包含
     *
     * @param x1 第一个矩形的X
     * @param y1 第一个矩形的Y
     * @param w1 第一个矩形的宽
     * @param h1 第一个矩形的高
     * @param x2 第二个矩形的X
     * @param y2 第二个矩形的Y
     * @param w2 第二个矩形的宽
     * @param h2 第二个矩形的高
     */
    public static boolean isInside(int x1, int y1, int w1, int h1, int x2,
                                   int y2, int w2, int h2) {
        return x1 > x2 && x1 + w1 < x2 + w2 && y1 > y2 && y1 + h1 < y2 + h2
                || x2 > x1 && x2 + w2 < x1 + w1 && y2 > y1 && y2 + h2 < y1 + h1;
    }

    /**
     * 检测两个矩形区域是否相交
     *
     * @param x1 第一个矩形的X
     * @param y1 第一个矩形的Y
     * @param w1 第一个矩形的宽
     * @param h1 第一个矩形的高
     * @param x2 第二个矩形的X
     * @param y2 第二个矩形的Y
     * @param w2 第二个矩形的宽
     * @param h2 第二个矩形的高
     */
    public static boolean isIntersected(int x1, int y1, int w1, int h1, int x2,
                                        int y2, int w2, int h2) {
        return !isOutside(x1, y1, w1, h1, x2, y2, w2, h2)
                && !isInside(x1, y1, w1, h1, x2, y2, w2, h2);
    }

    /**
     * 检测两个矩形区域是否相离
     *
     * @param x1 第一个矩形的X 坐标
     * @param y1 第一个矩形的Y 坐标
     * @param w1 第一个矩形的宽
     * @param h1 第一个矩形的高
     * @param x2 第二个矩形的X 坐标
     * @param y2 第二个矩形的Y 坐标
     * @param w2 第二个矩形的宽
     * @param h2 第二个矩形的高
     * @return
     */
    public static boolean isOutside(int x1, int y1, int w1, int h1, int x2,
                                    int y2, int w2, int h2) {
        return x1 >= x2 + w2 || x2 >= x1 + w1 || y1 >= y2 + h2 || y2 >= y1 + h1;
    }

    //	public static void moveInside(int x, int y, int width, int height) {
    //		if (this.x + vx <= x) {
    //			this.x = x;
    //			vx = -vx;
    //		} else if (this.x + this.width + vx >= x + width) {
    //			this.x = x + width - this.width;
    //			vx = -vx;
    //		}
    //		if (this.y + vy <= y) {
    //			this.y = y;
    //			vy = -vy;
    //		} else if (this.y + this.height + vy > y + height) {
    //			this.y = y + height - this.height;
    //			vy = -vy;
    //		}
    //		move();
    //	}

}
