package com.mona.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MessageHandler implements Runnable {
    private static final String END_MARK = "quit"; // 退出聊天室标识
    private static final String VIEW_USER = "viewuser"; // 查看在线成员列表

    private List<String> userList;
    private List<MessageHandler> threadList; // 服务器已启用线程集合
    private BlockingQueue<String> msgQueue;

    private Socket socket;

    private BufferedReader buff;

    private Writer writer;

    private String userName; // 成员名称

    /**
     * 构造函数<br>
     * 处理客户端的消息，加入到在线成员列表中
     *
     * @throws Exception
     */
    public MessageHandler(Socket socket, List<String> userList, List<MessageHandler> threadList,
                          BlockingQueue<String> msgQueue) {
        this.socket = socket;
        this.userList = userList;
        this.threadList = threadList;
        this.msgQueue = msgQueue;
        userName = String.valueOf(socket.getPort());
        try {
            buff = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        userList.add(userName);
        threadList.add(this);
        pushMsg("【" + userName + "进入了聊天室】");
        System.out.println("Form Cliect[port:" + socket.getPort() + "] " + userName + "进入了聊天室");
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = buff.readLine();

                if (VIEW_USER.equals(msg)) { // 查看聊天室在线成员
                    sendMsg(onlineUsers());
                } else if (END_MARK.equals(msg)) { // 遇到退出标识时就结束让客户端退出
                    sendMsg(END_MARK);
                    break;
                } else {
                    pushMsg(String.format("%1$s说：%2$s", userName, msg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally { // 关闭资源，聊天室移除成员
            try {
                writer.close();
                buff.close();
                socket.close();
            } catch (Exception e) {

            }
            userList.remove(userName);
            threadList.remove(this);
            pushMsg("【" + userName + "退出了聊天室】");
            System.out.println("Form Cliect[port:" + socket.getPort() + "] " + userName + "退出了聊天室");
        }
    }

    /**
     * 准备发送的消息存入队列
     *
     * @param msg
     */
    public void pushMsg(String msg) {
        try {
            msgQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        try {
            writer.write(msg);
            writer.write("\015\012");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 聊天室在线成员列表
     *
     * @return
     */
    public String onlineUsers() {
        StringBuffer sbf = new StringBuffer();
        sbf.append("======== 在线成员列表(").append(userList.size()).append(") ========\015\012");
        for (int i = 0; i < userList.size(); i++) {
            sbf.append("[" + userList.get(i) + "]\015\012");
        }
        sbf.append("===============================");
        return sbf.toString();
    }

}
