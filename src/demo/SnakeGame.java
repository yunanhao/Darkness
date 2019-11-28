package demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SnakeGame extends JFrame {
    public static final int WIDTH = 800, HEIGHT = 600, SLEEPTIME = 200, L = 1, R = 2, U = 3, D = 4;
    private static final long serialVersionUID = 5165144048881842633L;
    BufferedImage offersetImage = new BufferedImage(SnakeGame.WIDTH, SnakeGame.HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
    Rectangle rect = new Rectangle(20, 40, 15 * 50, 15 * 35);
    Snake snake;
    MyNode node;

    public SnakeGame() {
        snake = new Snake(this);
        createNode();
        this.setBounds(100, 100, SnakeGame.WIDTH, SnakeGame.HEIGHT);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent arg0) {
                System.out.println(arg0.getKeyCode());
                switch (arg0.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        snake.dir = SnakeGame.L;
                        break;
                    case KeyEvent.VK_RIGHT:
                        snake.dir = SnakeGame.R;
                        break;
                    case KeyEvent.VK_UP:
                        snake.dir = SnakeGame.U;
                        break;
                    case KeyEvent.VK_DOWN:
                        snake.dir = SnakeGame.D;
                }
            }
        });
        setTitle("贪吃蛇 0.1   By ： Easy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        new Thread(new ThreadUpadte()).start();
    }

    public static void main(final String[] args) {
        new SnakeGame();
    }

    @Override
    public void paint(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) offersetImage.getGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, SnakeGame.WIDTH, SnakeGame.HEIGHT);
        g2d.setColor(Color.black);
        g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
        if (snake.hit(node)) {
            createNode();
        }
        snake.draw(g2d);
        node.draw(g2d);
        g.drawImage(offersetImage, 0, 0, null);
    }

    public void createNode() {
        final int x = (int) (Math.random() * 650) + 50, y = (int) (Math.random() * 500) + 50;
        final Color color = Color.blue;
        node = new MyNode(x, y, color);
    }

    class ThreadUpadte implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(SnakeGame.SLEEPTIME);
                    repaint();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class MyNode {
    int x, y, width = 15, height = 15;
    Color color;

    public MyNode(final int x, final int y, final Color color) {
        this(x, y);
        this.color = color;
    }

    public MyNode(final int x, final int y) {
        this.x = x;
        this.y = y;
        color = Color.black;
    }

    public void draw(final Graphics2D g2d) {
        g2d.setColor(color);
        g2d.drawRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}

class Snake {
    public List<MyNode> nodes = new ArrayList<MyNode>();
    SnakeGame interFace;
    int dir = SnakeGame.R;

    public Snake(final SnakeGame interFace) {
        this.interFace = interFace;
        nodes.add(new MyNode(20 + 150, 40 + 150));
        addNode();
    }

    public boolean hit(final MyNode node) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getRect().intersects(node.getRect())) {
                addNode();
                return true;
            }
        }
        return false;
    }

    public void draw(final Graphics2D g2d) {
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).draw(g2d);
        }
        move();
    }

    public void move() {
        nodes.remove(nodes.size() - 1);
        addNode();
    }

    public synchronized void addNode() {
        MyNode nodeTempNode = nodes.get(0);
        switch (dir) {
            case SnakeGame.L:
                if (nodeTempNode.x <= 20) {
                    nodeTempNode = new MyNode(20 + 15 * 50, nodeTempNode.y);
                }
                nodes.add(0, new MyNode(nodeTempNode.x - nodeTempNode.width, nodeTempNode.y));
                break;
            case SnakeGame.R:
                if (nodeTempNode.x >= 20 + 15 * 50 - nodeTempNode.width) {
                    nodeTempNode = new MyNode(20 - nodeTempNode.width, nodeTempNode.y);
                }
                nodes.add(0, new MyNode(nodeTempNode.x + nodeTempNode.width, nodeTempNode.y));
                break;
            case SnakeGame.U:
                if (nodeTempNode.y <= 40) {
                    nodeTempNode = new MyNode(nodeTempNode.x, 40 + 15 * 35);
                }
                nodes.add(0, new MyNode(nodeTempNode.x, nodeTempNode.y - nodeTempNode.height));
                break;
            case SnakeGame.D:
                if (nodeTempNode.y >= 40 + 15 * 35 - nodeTempNode.height) {
                    nodeTempNode = new MyNode(nodeTempNode.x, 40 - nodeTempNode.height);
                }
                nodes.add(0, new MyNode(nodeTempNode.x, nodeTempNode.y + nodeTempNode.height));
                break;
        }
    }
}