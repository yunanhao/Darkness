package com.mona.rpg.model;

/**
 * 事件对象
 */
public interface IBaseEvent {

    int getIdentity();

    String getType();

    long getTime();

    IRole getSource();

    String toString();

    int hashCode();
}
