package com.mona.event;

import java.util.ArrayDeque;

public class Message {
    /**
     * 唯一标识
     */
    public final Object id;
    /**
     * 传入参数
     */
    protected Object[] params;
    /**
     * 结果集
     */
    protected ArrayDeque<Object> results;

    public Message(Object id, Object... params) {
        this.id = id;
        this.params = params;
        results = new ArrayDeque<Object>();
    }

    public Object[] getParams() {
        return params;
    }

    public Object getParamAtIndex(int index) {
        if (params != null && params.length > index) {
            return params[index];
        }
        return null;
    }

    public void addResult(Object obj) {
        results.add(obj);
    }

    public ArrayDeque<Object> getResult() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("id=");
        sb.append(id);
        sb.append("{");
        for (Object obj : params) {
            if (obj != null) {
                sb.append(obj.toString()).append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }

}
