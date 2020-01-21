package com.mona.rpg.model;

/**
 * 事件对象
 */
public interface IBaseEvent {
    int getCode();

    IRole[] getRoles();
}
