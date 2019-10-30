package demo;

import java.awt.*;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This program try to demonstrate double-buffer usage in drawing move your
 * mouse to observe screen flicker in the two canvas
 *
 * @author wangdq
 */
public class PaintDemo4 {
    public static void main(String[] args) {
        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        Frame frame = new Frame("Double-buffer in AWT demo");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.add(new CanvasEx(false, "Not Using Double-Buffer", Color.BLUE), BorderLayout.CENTER);
        frame.add(new CanvasEx(true, "Using Double-Buffer", Color.ORANGE), BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

/**
 * the canvas to show content here we try to simplify code ,the code style does
 * not encouraged
 */
class CanvasEx extends Canvas {
    private static final long serialVersionUID = 1L;
    private Point pos;
    private Image bufferImage;// off-screen drawable image
    private int bufferWidth;
    private int bufferHeight;
    private Graphics bufferGraphics;// off-screen graphics
    private boolean doubleBufferEnabled;
    private String text;

    public CanvasEx(boolean dBufEnabled, String text, Color bgClr) {
        doubleBufferEnabled = dBufEnabled;
        this.text = text;
        setBackground(bgClr);
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                pos = (Point) e.getPoint().clone();
                repaint();
            }
        });
        pos = new Point();
    }

    @Override
    /**
     * override it to avoid clearing the background by default
     */
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if (doubleBufferEnabled) {
            // checks the buffersize with the current panelsize
            // or initialises the image with the first paint
            if (bufferWidth != getSize().width || bufferHeight != getSize().height || bufferImage == null
                    || bufferGraphics == null) {
                resetBuffer();
            }
            drawContent(bufferGraphics);
            g.drawImage(bufferImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            drawContent(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 200);
    }

    // we pass the off-screen graphic to it using double buffer
    // switch to the normal paint by passing the screen graphics
    private void drawContent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setPaint(Color.RED);
        g2d.drawString(text, 50, 50);
        g2d.setPaint(Color.DARK_GRAY);
        g2d.fillOval(pos.x, pos.y, 50, 50);
    }

    private void resetBuffer() {
        // always keep track of the image size
        bufferWidth = getSize().width;
        bufferHeight = getSize().height;

        // clean up the previous image
        if (bufferGraphics != null) {
            bufferGraphics.dispose();
            bufferGraphics = null;
        }
        if (bufferImage != null) {
            bufferImage.flush();
            bufferImage = null;
        }
        System.gc();
        // create the new image with the size of the panel
        bufferImage = createImage(bufferWidth, bufferHeight);
        bufferGraphics = bufferImage.getGraphics();
    }
}