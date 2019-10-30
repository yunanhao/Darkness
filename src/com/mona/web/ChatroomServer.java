package com.mona.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Socket服务端<br>
 * 功能说明：信息共享<br/>
 * 模拟聊天室，实现多人聊天
 *
 * @author 大智若愚的小懂
 * @version 1.0
 * @Date 2016年8月31日
 */
public class ChatroomServer implements Runnable {

    private static final int SERVER_PORT = 8899; // 服务端端口
    /**
     * 线程池
     */
    public ScheduledExecutorService schedu;
    public ServerSocket serverSocket;
    private List<String> userList = new CopyOnWriteArrayList<String>();
    private List<MessageHandler> threadList = new ArrayList<MessageHandler>(); // 服务器已启用线程集合
    private BlockingQueue<String> msgQueue = new ArrayBlockingQueue<String>(20); // 存放消息的队列

    public ChatroomServer() throws IOException {
        serverSocket = new ServerSocket(SERVER_PORT);
        schedu = Executors.newScheduledThreadPool(8);
        schedu.execute(this);
        // 开启向客户端发送消息的线程
        schedu.execute(() -> {
            while (true) {
                /** 从消息队列中取消息，再发送给聊天室所有成员 */
                String msg = null;
                try {
                    msg = msgQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (msg != null) {
                    for (MessageHandler thread : threadList) {
                        thread.sendMsg(msg);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            ChatroomServer server = new ChatroomServer(); // 启动服务端
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
                Socket socket = serverSocket.accept();
                /**
                 * 我们的服务端处理客户端的连接请求是同步进行的， 每次接收到来自客户端的连接请求后，
                 * 都要先跟当前的客户端通信完之后才能再处理下一个连接请求。 这在并发比较多的情况下会严重影响程序的性能，
                 * 为此，我们可以把它改为如下这种异步处理与客户端通信的方式 每接收到一个Socket就建立一个新的线程来处理它
                 */
                schedu.execute(new MessageHandler(socket, userList, threadList, msgQueue));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
