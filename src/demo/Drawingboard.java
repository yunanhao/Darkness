package demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class Drawingboard extends JPanel {
    private static final long serialVersionUID = -5037259752721755023L;
    LinkedList<ShapeData> linkedList;
    ArrayList<ShapeData> arrayList;
    ShapeData shapeData;
    int startX, startY, tempX, tempY;

    public Drawingboard(final int width, final int height) {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        setPreferredSize(new Dimension(width, height));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                System.out.println(linkedList.removeLast());
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
            }

            @Override
            public void mouseExited(final MouseEvent e) {
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                switch (e.getButton()) {
                    case 1:
                        startX = tempX = e.getX();
                        startY = tempY = e.getY();
                        shapeData = new ShapeData(e.getX(), e.getY());
                        linkedList.addLast(shapeData);
                        break;
                    default:
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                switch (e.getButton()) {
                    case 1:
                        shapeData.endX = e.getX();
                        shapeData.endY = e.getY();
                        break;
                    default:
                }
                startX = tempX = 0;
                startY = tempY = 0;
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(final MouseEvent e) {
                tempX = e.getX();
                tempY = e.getY();
                switch (e.getButton()) {
                    case 1:
                        shapeData.endX = e.getX();
                        shapeData.endY = e.getY();
                        break;
                    default:
                }
            }

            @Override
            public void mouseMoved(final MouseEvent e) {
            }
        });
    }

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        final Drawingboard contentPane = new Drawingboard(640, 640);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(3);
        while (true) {
            contentPane.repaint();
        }
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        ((Graphics2D) g).setStroke(new BasicStroke(8.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 3.0f, new float[]{
                5, 3, 4}, 0.0f));
        for (final ShapeData data : linkedList) {
            g.drawLine(data.startX, data.startY, data.endX, data.endY);
        }
    }
}

class ShapeData {
    int startX, startY, endX, endY;

    public ShapeData(final int x, final int y) {
        endX = startX = x;
        endY = startY = y;
    }

    @Override
    public String toString() {
        return "起点(" + startX + "," + startY + ")" + "终点(" + endX + "," + endY + ")";
    }
}