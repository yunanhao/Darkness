package com.mona.web;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatFrame {
    private Dimension windowSize;
    private Dimension roleSelectorSize;
    private Dimension titleSize;
    private Panel title;
    private JSplitPane body;
    private JSplitPane chat;
    private JPanel roleSelector;
    private JPanel displayPanel;
    private Panel note;
    private JSplitPane cantainer;
    private JTextPane showArea;
    private JEditorPane inputArea;
    private CardLayout card;
    private ActionListener roleSelectorListener;

    public ChatFrame() {
        windowSize = new Dimension(800, 600);
        roleSelectorSize = new Dimension(64, 600);
        titleSize = new Dimension(600, 64);
        card = new CardLayout();
        roleSelectorListener = e -> {
            card.show(displayPanel, e.getActionCommand());
        };
        roleSelector = new JPanel();
        roleSelector.setMinimumSize(roleSelectorSize);
        displayPanel = new JPanel(card);
        cantainer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, roleSelector, displayPanel);
        addCard(0);
        addCard(1);
        addCard(2);
        addCard(3);
        initPanel();
        cantainer.setPreferredSize(windowSize);
    }

    public static void main(String[] args) {
        // 界面显示风格
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        } catch (Exception e) {

            e.printStackTrace();
        }
        JFrame frame = new JFrame("卡片布局管理器");
        frame.setBackground(Color.yellow);
        // frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new ChatFrame().getContainer());
        frame.pack();
        // canvas.requestFocus();// 必须在pack()之后调用不然键盘事件无法监听
        frame.setVisible(true);
    }

    public void addCard(int index) {
        showArea = new JTextPane();
        showArea.setEditable(false);
        showArea.addHyperlinkListener(hle -> {
            // 覆写hyperlinkUpdate()方法，当超级链接事件触发时会进入这
            // 判断是否为超级链接运行操作。若操作为真，则将新的HTML文件放到JEditorPane中,
            // 操作为(thePane.setPage(hle.getURL());)
            if (hle.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                System.out.println(hle.getURL());
                try {
                    showArea.setPage(hle.getURL());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        showArea.setPreferredSize(new Dimension(600, 400));
        inputArea = new JEditorPane();
        note = new Panel();
        chat = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(showArea), inputArea);
        title = new Panel();
        title.setMinimumSize(titleSize);
        body = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, chat, note);
        displayPanel.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, title, body), "0");
        JButton button = new JButton("00000");
        button.setPreferredSize(new Dimension(roleSelectorSize.width, 64));
        button.setActionCommand(String.valueOf(index));
        button.addActionListener(roleSelectorListener);
        roleSelector.add(button);
        card.show(displayPanel, String.valueOf(index));
    }

    private void initPanel() {
        body.setDividerLocation(0.5);

        // 设置编辑器要处理的文档内容类型，有text/html,text/rtf.text/plain三种类型。
        showArea.setContentType("text/html");
        showArea.setText("<a href=\"http://www.baidu.com\">琉璃神社</a>");
        setBlue_Italic_Bold_22("O(∩_∩)O哈哈~ 你没啊");

        chat.setDividerLocation(0.8);
        chat.setBackground(new Color(255, 22, 33, 100));

    }

    // 这个方法最主要的用途是将字符串插入到JTextPane中。
    public void insert(String str, AttributeSet attrset) {
        Document docs = showArea.getDocument();// 利用getDocument()方法取得JTextPane的Document
        // instance.0
        str = str + "\n";
        try {
            docs.insertString(docs.getLength(), str, attrset);
        } catch (BadLocationException ble) {
            System.out.println("BadLocationException:" + ble);
        }
    }

    public void setYellow_Bold_20(String str) {
        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setForeground(attrset, Color.yellow);
        StyleConstants.setBold(attrset, true);
        insert(str, attrset);
    }

    public void setBlue_Italic_Bold_22(String str) {
        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setForeground(attrset, Color.blue);
        StyleConstants.setItalic(attrset, true);
        StyleConstants.setFontSize(attrset, 24);
        insert(str, attrset);
    }

    public void setRed_UnderLine_Italic_24(String str) {
        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setForeground(attrset, Color.red);
        StyleConstants.setUnderline(attrset, true);
        StyleConstants.setItalic(attrset, true);
        StyleConstants.setFontSize(attrset, 24);
        insert(str, attrset);
    }

    public Container getContainer() {
        return cantainer;
    }

    void a() {
        // 设置center面板对象为卡片布局
        CardLayout card = new CardLayout();
        // ActionListener actionListener = e -> {
        // switch (e.getActionCommand()) {
        // case "第 0 序章":
        // card.show(center, "card0");
        // break;
        // case "第 1 序章":
        // card.show(center, "card1");
        // break;
        // case "第 2 序章":
        // card.show(center, "card2");
        // break;
        // case "第 3 序章":
        // card.show(center, "card3");
        // break;
        // default:
        // break;
        // }
        // };
        // for (int i = 0; i < 4; i++) {
        // JButton button = new JButton("第 " + i + " 序章");
        // button.setPreferredSize(new Dimension(128, 32));
        // button.addActionListener(actionListener);
        // bottom.add(button);
        // Canvas canvas = new MyCanvas();
        // canvas.addMouseListener(new MouseAdapter() {
        // @Override
        // public void mousePressed(MouseEvent e) {
        // System.out.println("nmb");
        // }
        // });
        // canvas.addKeyListener(new KeyListener() {
        //
        // @Override
        // public void keyTyped(KeyEvent e) {
        // System.out.println(e.getKeyChar());
        // }
        //
        // @Override
        // public void keyReleased(KeyEvent e) {
        //
        // }
        //
        // @Override
        // public void keyPressed(KeyEvent e) {
        //
        // }
        // });
        // canvas.setPreferredSize(new Dimension(200, 500));
        // canvas.setBackground(Color.blue);
        // center.add("card" + i, canvas);
        // }
    }
}