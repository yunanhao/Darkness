package com.mona.web;

import javax.swing.*;
import java.awt.*;

/**
 * 设置名称窗体
 *
 * @author zing
 */
public class NIOSetNameFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JTextField txtName;// 文本框
    private static JButton btnOK;// ok按钮
    private static JLabel label;// 标签

    public NIOSetNameFrame() {
        setLayout(null);
        Toolkit kit = Toolkit.getDefaultToolkit();
        int w = kit.getScreenSize().width;
        int h = kit.getScreenSize().height;
        this.setBounds(w / 2 - 230 / 2, h / 2 - 200 / 2, 230, 200);
        setTitle("设置名称");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        txtName = new JTextField(4);
        this.add(txtName);
        txtName.setBounds(10, 10, 100, 25);
        btnOK = new JButton("OK");
        this.add(btnOK);
        btnOK.setBounds(120, 10, 80, 25);
        label = new JLabel("[w:" + w + ",h:" + h + "]");
        this.add(label);
        label.setBounds(10, 40, 200, 100);
        label.setText("<html>在上面的文本框中输入名字<br/>显示器宽度：" + w + "<br/>显示器高度：" + h + "</html>");

        btnOK.addActionListener(e -> {
            String uname = txtName.getText();
            NIOClientService service = NIOClientService.getInstance();
            NIOChatFrame chatFrame = new NIOChatFrame(service, uname);
            chatFrame.show();
            setVisible(false);
        });
    }

    public static void main(String[] args) {
        NIOSetNameFrame setNameFrame = new NIOSetNameFrame();
        setNameFrame.setVisible(true);
    }

}
