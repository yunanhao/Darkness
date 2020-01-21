package com.mona.rpg.model;

import com.mona.rpg.view.ICanvas;

/**
 * 具备生命周期的角色
 * 1.建筑单位(无法移动 )
 * 2.补给品(药品 装备等)
 * 3.可动单位(子弹 技能等)
 * 4.具有行为能力的角色
 * 抽取三者的共同特性:
 * -属性:
 * 是否存在
 * 单位类型
 * 绘制深度(以地面0为界 大于0在地上反之在地下)
 * -方法:
 * 自身行为 触发其他物体或被其他物体触发
 *
 * 速度水平或者垂直方向时*1 斜向则*0.7
 *
 * 物体移动方向(以X正方向为基准线顺时针的偏角为正 弧度为单位 没有方向时为NaN)
 */
public interface IRole {
    void onCreate();

    void onDestroy();

    void onStart();

    void onStop();

    void onResume();

    void onPause();

    void onActive();

    void onDraw(ICanvas canvas);
}
