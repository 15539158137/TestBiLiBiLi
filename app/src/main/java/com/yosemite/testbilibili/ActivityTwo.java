package com.yosemite.testbilibili;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yosemite.testbilibili.barragepackage.BarrageView;
import com.yosemite.testbilibili.barragepackage.BarrageBean;
import com.yosemite.testbilibili.barragepackage.ScreenSizeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ActivityTwo extends Activity {
    BarrageView barrage;
    List<BarrageBean> allContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barrage = (BarrageView) findViewById(R.id.danmu);
        allContent = new ArrayList();

        BarrageBean barrageBean = new BarrageBean();
        barrageBean.setColor(0);
        barrageBean.setContent("第一条弹幕");
        barrageBean.setSpeend(0);
        allContent.add(barrageBean);

        BarrageBean barrageBean1 = new BarrageBean();
        barrageBean1.setColor(1);
        barrageBean1.setContent("第二条弹幕");
        barrageBean1.setSpeend(1);
        allContent.add(barrageBean1);

        BarrageBean barrageBean2 = new BarrageBean();
        barrageBean2.setColor(2);
        barrageBean2.setContent("第三条弹幕");
        barrageBean2.setSpeend(2);
        allContent.add(barrageBean2);

        BarrageBean barrageBean3 = new BarrageBean();
        barrageBean3.setColor(0);
        barrageBean3.setContent("第四条弹幕");
        barrageBean3.setSpeend(0);
        allContent.add(barrageBean3);

        BarrageBean barrageBean4 = new BarrageBean();
        barrageBean4.setColor(2);
        barrageBean4.setContent("第无条弹幕");
        barrageBean4.setSpeend(2);
        allContent.add(barrageBean4);


    }


    @Override
    protected void onResume() {
        super.onResume();
        startDanMu();
    }

    private void startDanMu() {
        for (int i = 0; i < allContent.size(); i++) {

            BarrageBean barrageBean = allContent.get(i);
            if (barrageBean.isHadSet()) {
                barrage.setAllContent(allContent);
            } else {
                //对于文字的初始Y的位置，使用随机数
                Random random = new Random();
                final int x = random.nextInt(10);
                //计算出这个随便的Y坐标
                int width = barrage.getRight() - barrage.getLeft();
                int height = barrage.getBottom() - barrage.getTop();
                Log.e("宽和搞", width + "=" + height + "=" + barrage.getRight());
                int tempY = ScreenSizeUtils.getScreenHeight(ActivityTwo.this) * x / 10;
                allContent.get(i).setX(barrage.getWidth());
                allContent.get(i).setY(tempY);
                //设置这个已经是设置过的了
                allContent.get(i).setHadSet(true);
                //根据文字的长度，动态设定最后的位置信息
                int finalPosition=allContent.get(i).getContent().length()*60;
                //需要给文字设置动画
                final ValueAnimator valueAnimator = ValueAnimator.ofInt(ScreenSizeUtils.getScreenWidth(ActivityTwo.this), -finalPosition);
                Log.e("动画开始时候的信息", barrage.getWidth() + "==");
                if (barrageBean.getSpeend() == 0) {
                    valueAnimator.setDuration(6000);
                } else if (barrageBean.getSpeend() == 1) {
                    valueAnimator.setDuration(4000);
                } else {
                    valueAnimator.setDuration(3000);
                }
                Log.e("获取到的时长", valueAnimator.getDuration() + "");
                final int finalI = i;
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int temp = (int) valueAnimator.getAnimatedValue();
                        Log.e("动画变化", temp + "");
                        allContent.get(finalI).setX(temp);
                        barrage.setAllContent(allContent);
                    }
                });
                valueAnimator.start();
            }
        }


    }

}
