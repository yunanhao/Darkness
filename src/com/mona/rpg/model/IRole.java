package com.mona.rpg.model;

import com.mona.rpg.view.ICanvas;

/**
 * 具备生命周期的角色
 */
public interface IRole {
    void onCreate();

    void onDestory();

    void onStart();

    void onStop();

    void onResume();

    void onPause();

    void onActive();

    void onDraw(ICanvas canvas);
}
