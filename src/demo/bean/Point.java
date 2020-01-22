package demo.bean;

public class Point implements Comparable<Point> {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public int compareTo(Point o) {
        if (x < o.x) {
            return -1;
        }
        if (x > o.x) {
            return 1;
        }
        if (y < o.y) {
            return 1;
        }
        if (y > o.y) {
            return -1;
        }
        return 0;
    }
}
