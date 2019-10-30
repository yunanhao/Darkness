package demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * 三角形
 */
public class Triangle {
    private int x1, y1;
    private int x2, y2;
    private int x3, y3;

    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        super();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }

    public static void main(String... args) {
        Triangle triangle = new Triangle(100, 200, 150, 500, 350, 350);
        Triangle triangle1 = new Triangle(250, 200, 50, 400, 300, 500);
        Line line = new Line(0, 50, 400, 50);
        JFrame frame = new JFrame();
        frame.setContentPane(triangle.new MyPanel(line, triangle, triangle1));
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println(triangle.crash(triangle1));
        System.out.println(triangle1.crash(triangle));
    }

    public static boolean isInside(Triangle triangle, int x, int y) {
        int area = 0;
        return ((area = (triangle.x2 - triangle.x1) * (triangle.y3 - triangle.y1) - (triangle.x3 - triangle.x1) * (triangle.y2 - triangle.y1)) < 0 ? -area : area)
                == ((area = (triangle.x1 - x) * (triangle.y2 - y) - (triangle.x2 - x) * (triangle.y1 - y)) < 0 ? -area : area)
                + ((area = (triangle.x2 - x) * (triangle.y3 - y) - (triangle.x3 - x) * (triangle.y2 - y)) < 0 ? -area : area)
                + ((area = (triangle.x3 - x) * (triangle.y1 - y) - (triangle.x1 - x) * (triangle.y3 - y)) < 0 ? -area : area);
    }

    public boolean crash(Triangle triangle) {
        return
                isInside(this, triangle.x1, triangle.y1) ||
                        isInside(this, triangle.x2, triangle.y2) ||
                        isInside(this, triangle.x3, triangle.y3)
                ;
    }

    public void paint(Graphics2D g) {
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2, y2, x3, y3);
        g.drawLine(x3, y3, x1, y1);
    }

    class MyPanel extends JPanel implements MouseMotionListener, MouseListener {
        private static final long serialVersionUID = 1L;
        private final Triangle triangle[];
        private final Line line;
        private int x, y;

        public MyPanel(Line line, Triangle... triangle) {
            this.triangle = triangle;
            this.line = line;
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            boolean flag = Triangle.isInside(triangle[0], x, y);
            if (flag) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.black);
            }
            for (int i = 0; i < triangle.length; i++) {
                triangle[i].paint((Graphics2D) g);
            }
            line.paint((Graphics2D) g);
            g.drawOval(x - 4, y - 4, 8, 8);
            g.drawString("方法1测试鼠标是否在三角形上：" + flag, 0, 32);
            g.drawString("" + line.getK(x, y), 0, 96);
            g.drawString("" + line.getLocation(x, y), 0, 128);
            g.setColor(Color.blue);
            g.drawLine(x, y, triangle[0].x1, triangle[0].y1);
            g.drawLine(x, y, triangle[0].x2, triangle[0].y2);
            g.drawLine(x, y, triangle[0].x3, triangle[0].y3);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            line.setEnd(e.getX(), e.getY());
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            x = e.getX();
            y = e.getY();
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            line.setStart(e.getX(), e.getY());
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }
    }
}

class Line {
    int x1, y1;
    int x2, y2;

    public Line(int x1, int y1, int x2, int y2) {
        super();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setStart(int x, int y) {
        this.x1 = x;
        this.y1 = y;
    }

    public void setEnd(int x, int y) {
        this.x2 = x;
        this.y2 = y;
    }

    public void paint(Graphics2D g) {
        g.fillOval(x1 - 2, y1 - 2, 4, 4);
        g.drawLine(x1, y1, x2, y2);
    }

    public int getK(int x, int y) {
        // 1. 线段的斜率 (x2 - x1) / (y2 - y1)
        // 2. 点到线段起点得到的线段的斜率 (x - x1) / (y - y1)
        // 3. 1和2的值相等则点在线上
        // 4. 避免浮点数的比较 所以要避免除法
        // 5. a/b = c/d 可以变换成 a*d = b*c, 例如1/2 = 2/4 变成 1*4 = 2*2
        // 6. 所以变成(x2 - x1) * (y - y1) == (y2 - y1) * (x - x1), 如果为true则点在线上
        // System.out.println(1.0*(y - y1)/(x - x1));
        // System.out.println(1.0*(y2 - y1)/(x2 - x1));
        return (x - x1) * (y2 - y1) - (y - y1) * (x2 - x1);
    }

    public String getLocation(int x, int y) {
        if (getK(x, y) > 0) {
            return "右上";
        } else if (getK(x, y) < 0) {
            return "左下";
        } else {
            if (x < x1 || y < y1) {
                return "线段的起点之外";
            } else if (x > x2 || y > y2) {
                return "线段的终点之外";
            } else {
                return "线段上";
            }
        }
    }
}
