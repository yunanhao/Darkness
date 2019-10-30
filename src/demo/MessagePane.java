package demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public class MessagePane<T> extends JComponent {
    private static final long serialVersionUID = -4373757487136917004L;
    /**
     * 允许最大数据行数
     */
    private static final int MAXLINE = 10000;
    public static String show = "1234";
    /**
     * 数据队列
     */
    private LinkedList<T> datas = new LinkedList<T>();
    /**
     * 本次绘制，需要绘制的行数，正数向下偏移，负数向上偏移
     */
    private int add = 0;
    /**
     * 每行数据的绘制高度
     */
    private int lineH = 1;
    /**
     * 滚动条
     */
    private JScrollBar bar = new JScrollBar(JScrollBar.VERTICAL);
    /**
     * 滚动条当前值
     */
    private int barValue = 0;
    /**
     * 绘制任务
     */
    private Runnable paintRunnable = () -> {
        add = 1;
        paintImmediately(0, 0, getWidth(), getHeight());
        add = 0;
    };
    /**
     * 滚动条更新任务
     */
    private Runnable barRunnable = () -> bar.setValues(barValue, getHeight() / lineH, 0,
            datas.size() + 1);

    /**
     * 构造一个新的 MessagePane
     */
    public MessagePane() {
        initialize();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            final MessagePane<String> messagePane = new MessagePane<String>();
            frame.setContentPane(messagePane);
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            Runnable runnable = () -> {
                while (true) {
                    messagePane.addData(show + Math.random());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnable).start();
            new Thread(runnable).start();
            new Thread(runnable).start();
            new Thread(runnable).start();
        });

    }

    /**
     * 初始化
     */
    private void initialize() {
        setOpaque(true);
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        add(bar, BorderLayout.EAST);
        bar.addAdjustmentListener(e -> {
            int newValue = bar.getValue();
            if (newValue != barValue) {
                add = barValue - newValue;
                barValue = newValue;
                MessagePane.this.paintImmediately(0, 0,
                        MessagePane.this.getWidth(),
                        MessagePane.this.getHeight());
                add = 0;
            }
        });
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                //控件大小变化时，更新滚动条
                updateBar();
            }
        });
        setFont(UIManager.getFont("Panel.font"));
        lineH = getFontMetrics(getFont()).getHeight() + 2;
        updateBar();
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (font != null) {
            lineH = getFontMetrics(font).getHeight() + 2;
        }
    }

    /**
     * 根据当前数据量，更新滚动条属性
     */
    private void updateBar() {
        if (SwingUtilities.isEventDispatchThread()) {
            barRunnable.run();
        } else {
            SwingUtilities.invokeLater(barRunnable);
        }
    }

    /**
     * 标准的添加数据接口，在事件指派线程中调用该方法，可以获得最大的性能优化
     *
     * @param data - 数据
     */
    public synchronized void addData(T data) {
        datas.addFirst(data);
        if (datas.size() > MAXLINE) {
            //数据超出限定范围时，移除最旧的一条数据
            datas.pollLast();
        } else {
            //数据长度变化时，更新滚动条
            updateBar();
        }
        if (SwingUtilities.isEventDispatchThread()) {
            paintRunnable.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(paintRunnable);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 标准的清除数据接口
     */
    public synchronized void clearDate() {
        datas.clear();
        //清除数据后，滚动条值归零
        barValue = 0;
        repaint();
        //清除数据后，更新滚动条
        updateBar();
    }

    /**
     * 获得真实的绘制宽度（减去滚动条宽度）
     *
     * @return
     */
    private int getPaintWidth() {
        return getWidth() - bar.getWidth();
    }

    @Override
    protected void paintComponent(Graphics g) {
        //当调用 repaint 进入该方法时，add 为 0 ，会将整个控件完整绘制一遍。
        //而当设置了 add 然后调用 paintImmediately 进入该方法时，
        //会进行 copyArea，然后只绘制 copy 后的脏区域。
        int w = getPaintWidth();
        int h = getHeight();
        if (add > 0) {
            int move = lineH * add;
            //add 大于 0 表示需要向下整体位移，因此向下 copy
            //重新设置 clip，限制需要绘制的区域
            g.copyArea(0, 0, w, h - move, 0, move);
            g.setClip(0, 0, w, move);
        } else if (add < 0) {
            int move = -lineH * add;
            //add 小于 0 表示需要向上整体位移，因此向上 copy
            //重新设置 clip，限制需要绘制的区域
            g.copyArea(0, move, w, h - move, 0, -move);
            g.setClip(0, h - move, w, move);
        }
        Rectangle clip = g.getClipBounds();
        int clipY = clip.y;
        int clipX = clip.x;
        int clipH = clip.height;
        int y = 0;
        g.setColor(getBackground());
        g.fillRect(clipX, clipY, w, clipH);
        for (int i = barValue; i < datas.size(); i++) {
            y += lineH;
            if (y < clipY) {
                //小于绘制区域上沿时，不需要绘制该条数据
                continue;
            }
            if (y - lineH > clipY + clipH) {
                //由于是从上往下绘制，因此大于绘制区域下沿时，终止绘制
                break;
            }
            g.setColor(getForeground());
            g.drawString(datas.get(i).toString(), 0, y);
        }
    }

}
