package demo;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ChatView1 extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    int begin = 0;
    // 聊天对象的信息
    // 当前登录用户的昵称
    private String yourname = "你的昵称";
    private javax.swing.JButton imageButton;
    private javax.swing.JButton sendButton;
    private JComboBox fontStyleSelect;
    private JComboBox fontSizeSelect;
    private JComboBox foregroundSelect;
    private javax.swing.JLabel headLabel;
    private javax.swing.JLabel friendMarkLabel;
    private javax.swing.JLabel fontStyleLabel;
    private javax.swing.JLabel fontSizeLabel;
    private javax.swing.JLabel foregroundLabel;
    private javax.swing.JPanel topPane;
    private javax.swing.JPanel toolPane;
    private javax.swing.JScrollPane showScrollPane;
    private javax.swing.JScrollPane sendScrollPane;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextPane showTextPane;
    private javax.swing.JTextPane sendTextPane;
    private SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();

    public ChatView1() {
        initComponents();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatView1().setVisible(true);
            }
        });
    }

    private void initComponents() {
        //界面显示风格
        try {
            javax.swing.UIManager
                    .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        } catch (Exception e) {

            e.printStackTrace();
        }

        topPane = new javax.swing.JPanel();
        headLabel = new javax.swing.JLabel();
        friendMarkLabel = new javax.swing.JLabel();
        showScrollPane = new javax.swing.JScrollPane();
        showTextPane = new javax.swing.JTextPane();
        jSeparator1 = new javax.swing.JSeparator();
        toolPane = new javax.swing.JPanel();
        fontStyleLabel = new javax.swing.JLabel();
        fontStyleSelect = new JComboBox();
        fontSizeLabel = new javax.swing.JLabel();
        fontSizeSelect = new JComboBox();
        foregroundLabel = new javax.swing.JLabel();
        foregroundSelect = new JComboBox();
        imageButton = new javax.swing.JButton();
        sendScrollPane = new javax.swing.JScrollPane();
        sendTextPane = new javax.swing.JTextPane();
        sendButton = new javax.swing.JButton();

        showTextPane.setEditable(false);
        // 默认字体颜色
        StyleConstants.setForeground(simpleAttributeSet, Color.red);
        // 默认字体大小
        StyleConstants.setFontSize(simpleAttributeSet, 13);
        sendTextPane.setCharacterAttributes(simpleAttributeSet, false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setTitle("\u804a\u5929\u4e2d...");

        headLabel.setIcon(new ImageIcon(new ImageIcon("1.jpg").getImage().getScaledInstance(78, 49,
                Image.SCALE_DEFAULT)));
        headLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        headLabel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1,
                1, 1, new Color(255, 255, 255)));

        friendMarkLabel.setText("对方昵称" + "("
                + 123456789 + ")");


        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
                topPane);
        topPane.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                jPanel1Layout.createSequentialGroup().addComponent(headLabel,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 54,
                        javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18,
                        18).addComponent(friendMarkLabel,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 210,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(129, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                friendMarkLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 49,
                Short.MAX_VALUE).addComponent(headLabel,
                javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE));

        showScrollPane.setViewportView(showTextPane);

        jSeparator1.setForeground(new Color(204, 204, 0));

        toolPane.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1,
                1, new Color(204, 204, 0)));

        fontStyleLabel.setText("\u5b57\u4f53:");

        fontStyleSelect.setModel(new javax.swing.DefaultComboBoxModel(
                new String[]{"宋体", "黑体", "Dialog", "Gulim"}));
        fontStyleSelect.setBorder(null);
        // 字体设置
        fontStyleSelect.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox jcb = (JComboBox) e.getSource();

                    String fam = (String) jcb.getSelectedItem();
                    System.out.println(fam);
                    StyleConstants.setFontFamily(simpleAttributeSet, fam);

                    sendTextPane.setCharacterAttributes(simpleAttributeSet,
                            false);
                    sendTextPane.getStyledDocument().setCharacterAttributes(0,
                            sendTextPane.getText().length(),
                            simpleAttributeSet, false);
                } else {

                }
            }
        });

        fontSizeLabel.setText("\u5b57\u53f7:");

        fontSizeSelect.setModel(new javax.swing.DefaultComboBoxModel(
                new String[]{"13", "14", "15", "16", "17", "18", "18", "20",
                        "21", "22"}));
        fontSizeSelect.setBorder(null);

        // 字号处理
        fontSizeSelect.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox jcb = (JComboBox) e.getSource();

                    System.out.println((String) (jcb.getSelectedItem()));
                    System.out.println(jcb.getSelectedIndex());
                    StyleConstants
                            .setForeground(simpleAttributeSet, Color.blue);

                    int size = Integer.valueOf((String) jcb.getSelectedItem());
                    StyleConstants.setFontSize(simpleAttributeSet, size);

                    sendTextPane.setCharacterAttributes(simpleAttributeSet,
                            false);
                    sendTextPane.getStyledDocument().setCharacterAttributes(0,
                            sendTextPane.getText().length(),
                            simpleAttributeSet, false);
                } else {

                }

            }
        });

        foregroundLabel.setText("\u989c\u8272:");

        foregroundSelect.setModel(new javax.swing.DefaultComboBoxModel(
                new String[]{"红色", "蓝色", "绿色", "黄色", "黑色"}));
        foregroundSelect.setBorder(null);

        // 字体颜色处理
        foregroundSelect.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox jcb = (JComboBox) e.getSource();
                    System.out.println((String) (jcb.getSelectedItem()));
                    System.out.println(jcb.getSelectedIndex());
                    String colorName = (String) (jcb.getSelectedItem());

                    if (colorName.equals("黑色")) {
                        StyleConstants.setForeground(simpleAttributeSet,
                                Color.black);
                    } else if (colorName.equals("蓝色")) {
                        StyleConstants.setForeground(simpleAttributeSet,
                                Color.blue);
                    } else if (colorName.equals("绿色")) {
                        StyleConstants.setForeground(simpleAttributeSet,
                                Color.green);
                    } else if (colorName.equals("黄色")) {
                        StyleConstants.setForeground(simpleAttributeSet,
                                Color.yellow);
                    } else if (colorName.equals("红色")) {
                        StyleConstants.setForeground(simpleAttributeSet,
                                Color.red);
                    }

                    sendTextPane.setCharacterAttributes(simpleAttributeSet,
                            false);
                    sendTextPane.getStyledDocument().setCharacterAttributes(0,
                            sendTextPane.getText().length(),
                            simpleAttributeSet, false);
                } else {

                }

            }
        });

        //为了保证插入图片后,发送格式不变
        sendTextPane.setText("<p></p>");
        sendTextPane.setText("");
        imageButton.setText("\u56fe\u7247");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
                toolPane);
        toolPane.setLayout(jPanel2Layout);
        jPanel2Layout
                .setHorizontalGroup(jPanel2Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel2Layout
                                        .createSequentialGroup()
                                        .addComponent(fontStyleLabel)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                fontStyleSelect,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                49,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fontSizeLabel)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                fontSizeSelect,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                48,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(
                                                foregroundLabel,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                27,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                foregroundSelect,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                45,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(
                                                imageButton,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                50, Short.MAX_VALUE).addGap(
                                        101, 101, 101)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel2Layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fontStyleLabel,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 29,
                                Short.MAX_VALUE).addComponent(fontStyleSelect,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 26,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fontSizeLabel,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 29,
                                Short.MAX_VALUE).addComponent(fontSizeSelect,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 26,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(foregroundLabel,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 29,
                                Short.MAX_VALUE).addComponent(foregroundSelect,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 26,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(imageButton,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                javax.swing.GroupLayout.PREFERRED_SIZE)));

        sendScrollPane.setViewportView(sendTextPane);

        sendButton.setText("发送");

        imageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(new File("Face"));

                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.showOpenDialog(ChatView1.this);
                if (chooser.getSelectedFile() != null) {
                    sendTextPane.insertIcon(new ImageIcon(chooser
                            .getSelectedFile().toString()));
                    sendTextPane.setCharacterAttributes(simpleAttributeSet,
                            false);
                    // 下面这段加的原因是因为,避免因"添加图片后,输入的文本内容的格式变为系统默认"
                    sendTextPane.setCharacterAttributes(simpleAttributeSet,
                            false);
                }
            }
        });


        // 点发送的处理
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 入过发送的内容为空,返回不发送
                sendClicked(e);

            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
                getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                layout.createSequentialGroup().addComponent(sendScrollPane,
                        javax.swing.GroupLayout.DEFAULT_SIZE, 327,
                        Short.MAX_VALUE).addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                        .addComponent(sendButton,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 78,
                                javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(
                jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 411,
                Short.MAX_VALUE).addComponent(topPane,
                javax.swing.GroupLayout.DEFAULT_SIZE,
                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(toolPane, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(showScrollPane,
                        javax.swing.GroupLayout.DEFAULT_SIZE, 411,
                        Short.MAX_VALUE));
        layout
                .setVerticalGroup(layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                layout
                                        .createSequentialGroup()
                                        .addComponent(
                                                topPane,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(9, 9, 9)
                                        .addComponent(
                                                jSeparator1,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                showScrollPane,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                225,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                toolPane,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                                layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(
                                                                sendScrollPane,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                102,
                                                                Short.MAX_VALUE)
                                                        .addComponent(
                                                                sendButton,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                102,
                                                                Short.MAX_VALUE))));

        pack();

    }

    /*
     * 发送按钮监听
     */
    private void sendClicked(ActionEvent e) {
        Map<Integer, Icon> icons = getAllIcons(sendTextPane.getStyledDocument()
                .getRootElements());
        String content = sendTextPane.getText();
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        String remark = yourname + " " + hour + ":" + minute + ":" + second
                + "\n";
        updateShowTextPane(remark, content, icons, simpleAttributeSet);

        sendTextPane.setText("");
    }

    /**
     * 更新显示pane
     *
     * @param remark
     * @param content
     * @param icons
     * @param simpleAttributeSet
     */
    public void updateShowTextPane(String remark, String content,
                                   Map<Integer, Icon> icons, SimpleAttributeSet simpleAttributeSet) {

        StringBuffer b = new StringBuffer();
        b.append(content);
        int begin = showTextPane.getDocument().getEndPosition().getOffset() - 1;

        try {

            SimpleAttributeSet s1 = new SimpleAttributeSet();
            StyleConstants.setForeground(s1, Color.blue);
            showTextPane.getStyledDocument().insertString(begin, remark, s1);
            begin = showTextPane.getStyledDocument().getEndPosition()
                    .getOffset() - 1;
            showTextPane.getStyledDocument().insertString(begin, b.toString(),
                    simpleAttributeSet);

            for (int site : icons.keySet()) {
                System.out.println("插入位置:" + site + begin);
                showTextPane.getDocument().remove(site + begin, 1);
                showTextPane.setCaretPosition(site + begin);

                showTextPane.insertIcon(icons.get(site));
            }

            try {
                showTextPane.getStyledDocument()
                        .insertString(
                                showTextPane.getDocument().getEndPosition()
                                        .getOffset() - 1, "\n",
                                simpleAttributeSet);
                showTextPane.setCaretPosition(showTextPane.getDocument().getEndPosition().getOffset() - 1);
            } catch (BadLocationException e2) {
                e2.printStackTrace();
            }
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }

    }

    /**
     * 获得JTextPane中的所有图片的所在位置和图片对象
     *
     * @param roots
     * @return
     */
    public Map<Integer, Icon> getAllIcons(Element[] roots) {
        Map<Integer, Icon> icons = new HashMap<Integer, Icon>();

        for (int a = 0; a < roots.length; a++) {
            for (int c = 0; c < roots[a].getElementCount(); c++) {
                Element element = roots[a].getElement(c);
                Icon icon = StyleConstants.getIcon(element.getAttributes());

                if (icon != null) {
                    icons.put(element.getStartOffset(), icon);
                } else
                    icons.putAll(getAllIcons(new Element[]{element}));
            }
        }

        return icons;
    }

    public javax.swing.JTextPane getShowTextPane() {
        return showTextPane;
    }

    public void setShowTextPane(javax.swing.JTextPane showTextPane) {
        this.showTextPane = showTextPane;
    }
}
