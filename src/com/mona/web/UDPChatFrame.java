package com.mona.web;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPChatFrame extends JPanel {
    private static final long serialVersionUID = 1L;
    boolean flag;
    int port = 10086;
    String address = "192.168.33.52";
    DatagramSocket send;
    DatagramSocket receive;
    UDP_Send udpSend;
    JTextArea showTextArea;
    JTextArea inputTextArea;
    JPanel rightPanel;
    JScrollPane scrollPane1;
    JScrollPane scrollPane2;
    JSplitPane splitPane1;
    JSplitPane splitPane2;
    JButton sendButton;
    JButton closeButton;
    JPanel buttonpane;
    private int width = 450, height = 450;

    public UDPChatFrame() throws Exception {
        super(null, true);
        send = new DatagramSocket();
        receive = new DatagramSocket(port);
        udpSend = new UDP_Send(send, port);
        showTextArea = new JTextArea();
        showTextArea.setLineWrap(true);
        showTextArea.setEditable(false);
        showTextArea.setForeground(new Color(0xf90033));
        scrollPane1 = new JScrollPane(showTextArea);
        inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        scrollPane2 = new JScrollPane(inputTextArea);
        rightPanel = new JPanel();
        rightPanel.setBackground(new Color(0x0099ff));
        splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, scrollPane1, scrollPane2);
        splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, splitPane1, rightPanel);
        inputTextArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        udpSend.send(port);
                        inputTextArea.setText("");
                        break;
                    default:
                        break;
                }
            }
        });
        sendButton = new JButton("发送");
        sendButton.addActionListener(e -> {
            udpSend.send(port);
            inputTextArea.setText("");
        });
        closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> System.exit(0));
        buttonpane = new JPanel();
        buttonpane.add(closeButton);
        buttonpane.add(sendButton);
        splitPane1.setBounds(0, 0, width, height - 32);
        splitPane1.setDividerLocation(0.8);
        splitPane2.setBounds(0, 0, width, height - 32);
        splitPane2.setDividerLocation(0.8);
        buttonpane.setBounds(width - 250, height - 32, 140, 32);
        this.add(splitPane2);
        this.add(buttonpane);
        setPreferredSize(new Dimension(width, height));
        new Thread(new UDP_Receive(receive)).start();
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setContentPane(new UDPChatFrame());
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    class UDP_Send {
        DatagramSocket datagramSocket;
        int port;
        String text;

        public UDP_Send(DatagramSocket datagramSocket, int port) {
            this.datagramSocket = datagramSocket;
            this.port = port;
        }

        public void send(int port) {
            try {
                text = new String(inputTextArea.getText().getBytes(), StandardCharsets.UTF_8);
                byte[] buffered = text.getBytes();
                DatagramPacket database = new DatagramPacket(buffered, buffered.length, InetAddress.getByName(address), port);
                showTextArea.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 24));
                showTextArea.append("本机" + ":\n");
                showTextArea.setFont(new Font("仿宋", Font.CENTER_BASELINE, 16));
                showTextArea.append("  " + text + "\n");
                datagramSocket.send(database);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class UDP_Receive implements Runnable {
        DatagramSocket datagramSocket;

        public UDP_Receive(DatagramSocket datagramSocket) {
            this.datagramSocket = datagramSocket;
        }

        @Override
        public void run() {
            try {
                byte[] buff = new byte[4096];
                DatagramPacket database = new DatagramPacket(buff, buff.length);
                while (true) {
                    datagramSocket.receive(database);
                    String text = new String(database.getData(), 0, database.getLength(), StandardCharsets.UTF_8);
                    showTextArea.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 24));
                    showTextArea.append(database.getAddress() + ":\n");
                    showTextArea.setFont(new Font("仿宋", Font.CENTER_BASELINE, 16));
                    showTextArea.append("  " + text + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
