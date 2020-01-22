package com.mona.rpg.control;

import com.mona.rpg.model.IRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventImpl implements IBaseEvent {
    private static final HashMap<String, IBaseEvent> local = new HashMap<>();

    private int identity;

    private int type;

    private String tag;

    private long time;

    private List<IBaseListener> listeners;

    private int x, y;

    public EventImpl() {
        tag = "";
        listeners = new ArrayList<>();
    }

    public static IBaseEvent create(String tag, int x, int y) {
        EventImpl event = new EventImpl();
        event.x = x;
        event.y = y;
        event.tag = tag;
        event.time = System.currentTimeMillis();
        event.identity = tag.hashCode();
        event.addListener(new IBaseListener() {
            @Override
            public void call(IBaseEvent event) {
                System.out.println(event);
            }
        });
        return event;
    }

    @Override
    public int getIdentity() {
        return identity;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public IRole getSource() {
        return null;
    }

    @Override
    public void addListener(IBaseListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean removeListener(IBaseListener listener) {
        return listeners.remove(listener);
    }

    @Override
    public void run() {
        listeners.forEach((IBaseListener listener) -> listener.call(this));
    }

    @Override
    public int hashCode() {
        return identity;
    }

    @Override
    public String toString() {
        return tag;
    }
}
