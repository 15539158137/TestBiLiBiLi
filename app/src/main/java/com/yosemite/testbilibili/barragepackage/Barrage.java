package com.yosemite.testbilibili.barragepackage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/13.
 */

public class Barrage extends View {
    //所有的弹幕内容
    List<BarrageBean> allContent;
    Canvas canvas;
    //首先，由于弹幕可以选择不同的颜色，所以Paint要与Message建立一个关联，如果找个message有颜色，那么就是设置的颜色，没有颜色就是黑色
    public void setAllContent(List<BarrageBean> list) {
        this.allContent=list;
        Log.e("进入到set方法","=====");
     invalidate();
    }

    //在动画的过程中，动态的绘制这些个数据
    private void refreshAllContentPosition() {
        if(allContent==null||allContent.size()==0){
            return;
        }
        for (int i=0;i<allContent.size();i++) {
            Log.e("现在所在的数据",allContent.get(i).getContent());
            final BarrageBean barrageBean=allContent.get(i);
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
            paint.setTextSize(60);

            //对于文字的初始Y的位置，使用随机数
            Random random = new Random();
            final int x = random.nextInt(10);
            //计算出这个随便的Y坐标
            int tempY = getHeight() * x / 10;
            Log.e("这条数据是否已经设置过了",allContent.get(i).hadSet+"=");
            //对于弹幕显示的位置，初始是在屏幕的最右边，所在的Y是个随机数；如果他是初始值，呢么需要给他加个动画，让他X轴的数据进行变动，如果他不是初始值，那么就只需要直接设置上去就可以了。
            if(barrageBean.isHadSet()){
                Log.e("这条数据直接上去，不许动画","============");
                //如果设置了，就使用设置的，没有就初始化
                canvas.drawText(barrageBean.getContent(), barrageBean.getX(), barrageBean.getY(), paint);
                //如果已经设置了，呢么就直接去进行相减，如果没有设置，就先设置开始的坐标
                if (barrageBean.getSpeend() == 0) {
                    canvas.drawText(barrageBean.getContent(), barrageBean.getX()-getWidth()/6, barrageBean.getY(), paint);
                } else if (barrageBean.getSpeend() == 1) {
                    canvas.drawText(barrageBean.getContent(), barrageBean.getX()-getWidth()/4, barrageBean.getY(), paint);
                } else {
                    canvas.drawText(barrageBean.getContent(), barrageBean.getX()-getWidth()/2, barrageBean.getY(), paint);
                }
            }else {
                Log.e("这条数据还需要动画","=====");
                canvas.drawText(barrageBean.getContent(), getWidth(), tempY, paint);
                allContent.get(i).setX(getWidth());
                allContent.get(i).setY(tempY);
                //设置这个已经是设置过的了
                allContent.get(i).setHadSet(true);
                //需要给文字设置动画
                Log.e("这条数据有没有设置上去",allContent.get(i).hadSet+"=");
                final ValueAnimator valueAnimator = ValueAnimator.ofInt(getWidth(), 0);
                if (barrageBean.getSpeend() == 0) {
                    valueAnimator.setDuration(3000);
                } else if (barrageBean.getSpeend() == 1) {
                    valueAnimator.setDuration(2000);
                } else {
                    valueAnimator.setDuration(1000);
                }
                Log.e("获取到的时长",valueAnimator.getDuration()+"");
                valueAnimator.start();
                final int finalI = i;
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int temp = (int) valueAnimator.getAnimatedValue();
                        Log.e("动画变化",temp+"");
                        allContent.get(finalI).setX(temp);
                        invalidate();

                    }
                });

            }

        }
    }

    public Barrage(Context context) {
        super(context);
    }

    public Barrage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public Barrage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Barrage(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        final Paint paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        paint.setTextSize((float) 50.0);
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawText("弹幕开始了",1000,150,paint);
        refreshAllContentPosition();
    }
}
