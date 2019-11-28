package demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class RaceMap extends JPanel implements Runnable {
    private static final long serialVersionUID = -8937422683923758349L;
    JButton bt; // 控制的小方块
    Thread th; // 线程，获得此线程，方便对象调用此线程
    private int x = 0, y = 0; // bt的位置，也即控制的小方块的位置
    private int w = 15, h = 15; // bt的宽，高
    private int direction = 0; // 方向
    private boolean tag = true; // 结束标示符
    private double speed = 1; // 速度
    private Color color = Color.BLACK; // 中间框的颜色

    // Icon icon;
    RaceMap() {
        setLayout(null);// 要将布局管理器设为空，才能对里面的控件地方做设置
        setBackground(Color.WHITE);
        bt = new JButton();
        add(bt);
        bt.setBackground(Color.RED); // 设置背景
        bt.setBounds(0, 0, w, h); // 设置初始地方
        // 添加键盘监听，控制方向
        bt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) // 上
                {
                    if (direction == 0) {
                        speed += 0.1;
                    } else if (direction == 1) // 本身为向下时，减速
                    {
                        speed -= 0.1;
                        if (speed <= 1) {
                            direction = 0;
                        }
                    } else {
                        // 本身为向左或向右时，仅改变方向
                        direction = 0;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    direction = 1;
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    direction = 2;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    direction = 3;
                }
            }
        });
        th = new Thread(this);// 获得此线程，方便下面对象直接启动此线程
    } // 要有继承Runnable或Thread才能用此，也即要有自己的线程

    public void makeRun() {
        tag = true;
        final Graphics g = getGraphics();
        g.setColor(Color.MAGENTA);// 这样画会容易被擦掉
        g.fillOval(10, 10, 50, 50);
        bt.requestFocusInWindow(); // 自动获得焦点，不用按TAB再获得
        g.dispose();
    }

    public void setSpeed(double speed) // 设置速度
    {
        this.speed = speed;
        bt.requestFocusInWindow(); // 自动获得焦点，不用按TAB再获得
    }

    public void setFillRectColor(String st) // 设置中间矩形的颜色
    {
        if (st.equals("蓝色")) {
            color = Color.BLUE;
        } else if (st.equals("绿色")) {
            color = Color.GREEN;
        } else {
            color = Color.BLACK;
        }
        repaint();
        bt.requestFocusInWindow(); // 自动获得焦点，不用按TAB再获得
    }

    public void setButtonWidthHeigth(int w, int h) {
        this.w = w;
        this.h = h;
        bt.setSize(w, h);
        bt.requestFocusInWindow(); // 自动获得焦点，不用按TAB再获得
    }

    @Override
    public void run() {
        bt.requestFocusInWindow(); // 自动获得焦点，不用按TAB再获得
        while (tag) {
            if (direction == 0) // 向上 跟的第2句是出边框时的操作
            { // 比如出上边框了就到最下面去
                y -= speed;
                if (y + h <= 1) {
                    y = getHeight();
                }
            } else if (direction == 1) // 向下
            {
                y += speed;
                if (y >= getHeight()) {
                    y = 0;
                }
            } else if (direction == 2) // 向左
            {
                x -= speed;
                if (x + w <= 1) {
                    x = getWidth();
                }
            } else // 向右
            {
                x += speed;
                if (x >= getWidth()) {
                    x = 0;
                }
            }
            bt.setLocation(x, y);
            try {
                Thread.sleep(10); // 控制速度
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            if (x + w > 130 && x < 330 && y + h > 130 && y < 330) // 判断是否与中间矩形相碰
            {
                tag = false;
                repaint();
                x = 0;
                y = 0;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);// 如果不写此，加的控件刚开始时不会显现
        if (tag == true) {
            g.setColor(color); // 画笔
        } else {
            g.setColor(Color.CYAN);
        }
        g.fillRect(130, 130, 200, 200); // 填充矩形
        if (tag == false) {
            g.setColor(Color.RED);
            g.drawString("游戏结束", getWidth() / 2 - 30, getWidth() / 2);
        }

    }
}

class UpPanel extends Panel {
    private static final long serialVersionUID = 1L;
    TextField tf; // 速度
    JButton bt1; // 提交速度
    JButton bt2; // 开始
    Choice ch; // 指示中间框的颜色
    Checkbox cb1;
    Checkbox cb2;
    Checkbox cb3;
    CheckboxGroup cbg;

    public UpPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        tf = new TextField(10);
        bt1 = new JButton("提交速度");
        bt2 = new JButton("点我开始");
        ch = new Choice();
        ch.add("黑色");
        ch.add("绿色");
        ch.add("蓝色");
        cbg = new CheckboxGroup();
        cb1 = new Checkbox("小", cbg, false);
        cb2 = new Checkbox("中", cbg, true);
        cb3 = new Checkbox("大", cbg, false);
        add(tf);
        add(bt1);
        add(bt2);
        add(ch);
        add(cb1);
        add(cb2);
        add(cb3);
    }
}

public class CarRace extends JFrame {
    private static final long serialVersionUID = -56155760140857009L;
    RaceMap rm; // 主面板 下面（实际是在Frame中间）
    // Button bt; //按钮 “点我开始”
    UpPanel upp; // 面板2 上面

    // Thread th;
    public CarRace() {
        super("RaceMapTest");
        rm = new RaceMap();
        // th=new Thread(rm);
        upp = new UpPanel();
        upp.bt2.addActionListener(new myActionPerformed(rm));
        upp.bt1.addActionListener(new myActionPerformed(upp.tf, rm));
        upp.ch.addItemListener(new myItemListener(rm, upp.ch));
        upp.cb1.addItemListener(new myItemListener(rm, upp.cb1));
        upp.cb2.addItemListener(new myItemListener(rm, upp.cb2));
        upp.cb3.addItemListener(new myItemListener(rm, upp.cb3));
        // bt=new Button("点我开始");
        upp.setBackground(Color.CYAN);
        // upp.add(bt);
        // bt.addActionListener(this);

        setBounds(100, 100, 500, 530);
        setLayout(new BorderLayout());
        add(upp, BorderLayout.NORTH);
        add(rm, BorderLayout.CENTER);
        validate();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }

        });
        setVisible(true);
    }

    /*
     * public void actionPerformed(ActionEvent e) { if(e.getSource()==bt) { try
     * //点击一次按钮后再点会出现IllegalThreadStateException异常 { rm.th.start();
     * rm.validate(); } catch(IllegalThreadStateException ee) { rm.makeRun(); }
     * } }
     */
    public static void main(String[] args) {
        new CarRace();
    }
}

class myActionPerformed implements ActionListener {
    TextField tf;
    RaceMap rm;

    myActionPerformed(TextField tf, RaceMap rm) // 此必须加上参数RaceMap
    { // 要不会抛出异常 NullPointerException
        this.tf = tf;
        this.rm = rm;
    }

    myActionPerformed(RaceMap rm) {
        this.rm = rm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JButton bt = (JButton) e.getSource();
        if (bt.getText().equals("提交速度"))// 判断
        {
            final String speed = tf.getText();
            if (!speed.equals("")) {
                try {
                    rm.setSpeed(Double.valueOf(speed));
                } catch (final NumberFormatException ee) {
                    JOptionPane.showMessageDialog(rm, "输入的速度值非数字!");
                }
            } else {
                JOptionPane.showMessageDialog(rm, "没有输入速度值!");
            }
        } else if (bt.getText().equals("点我开始"))// 判断
        {
            try// 点击一次按钮后再点会出现IllegalThreadStateException异常
            {
                rm.th.start();
                rm.validate();
            } catch (final IllegalThreadStateException ee) {
                rm.makeRun();
            }
        }
    }
}

class myItemListener implements ItemListener {
    RaceMap rm;
    Choice ch;
    Checkbox cb;

    public myItemListener(RaceMap rm, Choice ch) {
        this.rm = rm;
        this.ch = ch;
    }

    public myItemListener(RaceMap rm, Checkbox cb) {
        this.rm = rm;
        this.cb = cb;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getItemSelectable().equals(ch))// 判断事件源是否为JComboBox
        {
            rm.setFillRectColor(e.getItem().toString());
            // rm.setFillRectColor(ch.getSelectedItem());//这样也行
        } else if (e.getItemSelectable().equals(cb))// 判断事件源是否为
        { // Checkbox
            if (e.getItem().toString().equals("小")) {
                rm.setButtonWidthHeigth(10, 10);
            } else if (e.getItem().toString().equals("大")) {
                rm.setButtonWidthHeigth(20, 20);
            } else if (e.getItem().toString().equals("中")) {
                rm.setButtonWidthHeigth(15, 15);
            }
        }
    }
}