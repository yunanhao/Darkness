package com.mona.web;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Socket客户端<br>
 * 功能说明：信息共享<br/>
 * 模拟聊天室，实现多人聊天
 *
 * @author 大智若愚的小懂
 * @version 1.0
 * @Date 2016年8月31日
 */
public class ChatroomClient implements Runnable {
    Writer writer;
    private InetSocketAddress targetAddress;
    private Socket client;
    private OutputStream out;
    private InputStream in;

    public ChatroomClient(String host, int port) throws IOException {
        targetAddress = new InetSocketAddress(port);
        client = new Socket();
        client.connect(targetAddress);
        out = client.getOutputStream();
        in = client.getInputStream();
        writer = new OutputStreamWriter(out, "UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        // 启动监听收取消息
        new Thread() {
            @Override
            public void run() {
                try {
                    while (!client.isClosed()) {
                        String result = reader.readLine();
                        if ("quit".equals(result)) { // 遇到退出标识时表示服务端返回确认退出
                            System.out.println("Cliect[port:" + client.getLocalPort() + "] 您已退出聊天室");
                            break;
                        } else { // 输出服务端回复的消息
                            System.out.println(result);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        client.close();
                        // 关闭连接
                        writer.close();
                        client.close();
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
        // 启动循环发送消息
        new Thread(this).start();
    }

    public static void main(String[] args) {
        try {
            // 启动客户端
            ChatroomClient client = new ChatroomClient("127.0.0.1", 8899);
            System.out.println("Cliect[port:" + client.client.getLocalPort() + "] 您已进入聊天室");
            InetSocketAddress.createUnresolved(InetAddress.getLocalHost().getHostName(), 8899);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            // 循环可以不停的输入消息，将消息发送出去
            while (!client.isClosed()) {
                String inputMsg = scanner.nextLine();
                writer.write(inputMsg);
                writer.write("\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

}
