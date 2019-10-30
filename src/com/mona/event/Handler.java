package com.mona.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/***/
public class Handler {
    /**
     * 事件队列
     */
    private static final ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    /**
     * 标识和事件的映射
     */
    private static final Map<Object, Runner> runnerSet = new HashMap<>();
    /**
     * 标识和回调的映射
     */
    private static final Map<Object, List<Callback>> callbackSet = new HashMap<>();
    /**
     * 线程池
     */
    private static final ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(100);

    /**
     * 轮询消息队列并在队列不为空时派发事件
     */
    public static final void dispatchMessage() {
        // 遍历事件队列
        while (true) {
            if (messageQueue.isEmpty()) {
                return;
            }
            // 取消息
            Message message = messageQueue.poll();
            if (message == null) {
                continue;
            }
            // 获取消息的处理对象
            Runner runner = runnerSet.get(message.id);
            if (runner == null) {
                continue;
            }
            // 处理消息
            runner.setMessage(message);
            runner.setCallback(callbackSet.get(message.id));
            scheduled.submit(runner);
        }
    }

    /**
     * 绑定标识所对应的消息处理器
     *
     * @param id     标识
     * @param runner 消息处理器
     */
    public static final void bind(Object id, Runner runner) {
        runnerSet.put(id, runner);
    }

    /**
     * 移除标识所对应的消息处理器
     *
     * @param id     标识
     * @param runner 消息处理器
     */
    public static final boolean remove(Object id, Runner runner) {
        return runnerSet.remove(id, runner);
    }

    /**
     * 注册监听
     *
     * @param id       标识
     * @param callback 对应的回调函数
     */
    public static final boolean registerListener(Object id, Callback callback) {
        List<Callback> callbacks = callbackSet.get(id);
        if (callbacks == null) {
            callbacks = new LinkedList<Callback>();
            callbackSet.put(id, callbacks);
        }
        return callbacks.add(callback);
    }

    /**
     * 取消监听
     *
     * @param id       标识
     * @param callback 对应的回调函数
     */
    public static final boolean cancerListener(Object id, Callback callback) {
        List<Callback> callbacks = callbackSet.get(id);
        if (callbacks == null) {
            return false;
        }
        return callbacks.remove(callback);
    }

    /**
     * 发送一个消息
     */
    public static final boolean post(Object id, Object... params) {
        return messageQueue.offer(new Message(id, params));
    }

    public final boolean postAtTime(Object id, long uptimeMillis) {
        // TODO
        return false;
    }

    public final boolean postDelayed(Object id, long delayMillis) {
        // TODO
        return false;
    }

}