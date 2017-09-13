package com.yosemite.testbilibili;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.yosemite.testbilibili.barragepackage.BarrageBean;
import com.yosemite.testbilibili.barragepackage.ScreenSizeUtils;
import com.yosemite.testbilibili.surfaceview.MyBarrageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ActivityThree extends Activity{
    //所有的弹幕内容
    List<BarrageBean> allContent;
    Canvas canvas;


    MyBarrageView myBarrageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_thress);
    myBarrageView=findViewById(R.id.mys);

        allContent=new ArrayList();

        BarrageBean barrageBean=new BarrageBean();
        barrageBean.setColor(0);
        barrageBean.setContent("第一条弹幕");
        barrageBean.setSpeend(0);
        allContent.add(barrageBean);

        BarrageBean barrageBean1=new BarrageBean();
        barrageBean1.setColor(1);
        barrageBean1.setContent("第二条弹幕");
        barrageBean1.setSpeend(1);
        allContent.add(barrageBean1);

        BarrageBean barrageBean2=new BarrageBean();
        barrageBean2.setColor(2);
        barrageBean2.setContent("第三条弹幕");
        barrageBean2.setSpeend(2);
        allContent.add(barrageBean2);

        BarrageBean barrageBean3=new BarrageBean();
        barrageBean3.setColor(0);
        barrageBean3.setContent("第四条弹幕");
        barrageBean3.setSpeend(0);
        allContent.add(barrageBean3);
        new Thread(){
            @Override
            public void run() {
                setAllContent1();
            }
        }.start();

    }

    /*这个是初始状态下的数据加载，开始动画，对于后续的就可以直接拿到数据，直接放置了*/
    public void setAllContent1() {
        if(allContent==null||allContent.size()==0){
            return;
        }
        Log.e("进入到循环","=====");
        for (final BarrageBean barrageBean : allContent) {
            //设置单个弹幕的显示
            final Paint paint = new Paint();
            paint.setTextSize((float) 50.0);
            //抗锯齿
            paint.setAntiAlias(true);
            if (barrageBean.getColor() == 0) {
                //表示是个白色的弹幕
                paint.setColor(Color.parseColor("#ffffff"));
            } else if (barrageBean.getColor() == 1) {
//0000CD
                paint.setColor(Color.parseColor("#0000CD"));
            } else {
//FF0000
                paint.setColor(Color.parseColor("#FF0000"));
            }
            //对于文字的初始Y的位置，使用随机数
            Random random = new Random();
            final int x = random.nextInt(10);
            //计算出这个随便的Y坐标
            int tempY = myBarrageView.getHeight() * x / 10;
            barrageBean.setY(tempY);

            Log.e("开始画单个","====="+tempY+"=="+myBarrageView.getWidth()+"==");
            //此时canvas没有被销毁，还可以绘制，当走到surfaceDestroyed时，surfaceViewHolder就销毁了
            myBarrageView.canvas = myBarrageView.surfaceHolder.lockCanvas();
            //清屏操作,
            myBarrageView.canvas.drawColor(Color.TRANSPARENT);


            myBarrageView.canvas.drawText(barrageBean.getContent(), myBarrageView.getWidth(), tempY, paint);
            //提交所做的修改
            myBarrageView.surfaceHolder.unlockCanvasAndPost(myBarrageView.canvas);

            //需要给文字设置动画
            ValueAnimator valueAnimator = ValueAnimator.ofInt(myBarrageView.getWidth(), 0);
            if (barrageBean.getSpeend() == 0) {
                valueAnimator.setDuration(3000);
            } else if (barrageBean.getSpeend() == 1) {
                valueAnimator.setDuration(2000);
            } else {
                valueAnimator.setDuration(1000);
            }
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int temp = (int) valueAnimator.getAnimatedValue();
                    Log.e("动画变化",temp+"");
                    barrageBean.setX(temp);
                    new Thread(){
                        @Override
                        public void run() {
                            refreshAllContentPosition();
                        }
                    }.start();

                }
            });
            valueAnimator.start();
        }
        //全部设置完毕

    }
    //在动画的过程中，动态的绘制这些个数据
    private void refreshAllContentPosition() {
        for (final BarrageBean barrageBean : allContent) {
            //设置单个弹幕的显示
            final Paint paint = new Paint();
            //抗锯齿
            paint.setAntiAlias(true);
            if (barrageBean.getColor() == 0) {
                //表示是个白色的弹幕
                paint.setColor(Color.parseColor("#ffffff"));
            } else if (barrageBean.getColor() == 1) {
//0000CD
                paint.setColor(Color.parseColor("#0000CD"));
            } else {
//FF0000
                paint.setColor(Color.parseColor("#FF0000"));
            }
            //此时canvas没有被销毁，还可以绘制，当走到surfaceDestroyed时，surfaceViewHolder就销毁了
            myBarrageView.canvas = myBarrageView.surfaceHolder.lockCanvas();
            //清屏操作,
            myBarrageView.canvas.drawColor(Color.TRANSPARENT);


            myBarrageView.canvas.drawText(barrageBean.getContent(), barrageBean.getX(), barrageBean.getY(), paint);
            //提交所做的修改
            myBarrageView.surfaceHolder.unlockCanvasAndPost(myBarrageView.canvas);
        }
    }
}
