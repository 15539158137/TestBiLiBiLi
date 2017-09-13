package com.yosemite.testbilibili.barragepackage;

import android.animation.ValueAnimator;

/**
 * Created by Administrator on 2017/9/13.
 */

public class BarrageBean {
    int color;//三种颜色，白，蓝，红
    String content;//聊天的内容
    int speend;//弹幕的速度
    int ID;//这条弹幕的ID，这个ID来建立起动画和消息的关联
    //弹幕的横纵坐标，这个是移动的
    int X;
    int Y;
    //是否已经设置了xy，设置了就只能拿去用，没有设置就初始化
    boolean hadSet;

    public BarrageBean() {
        this.hadSet=false;
    }

    public boolean isHadSet() {
        return hadSet;
    }

    public void setHadSet(boolean hadSet) {
        this.hadSet = hadSet;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSpeend() {
        return speend;
    }

    public void setSpeend(int speend) {
        this.speend = speend;
    }
}
