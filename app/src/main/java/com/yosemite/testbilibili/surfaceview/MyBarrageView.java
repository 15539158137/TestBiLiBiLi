package com.yosemite.testbilibili.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yosemite.testbilibili.barragepackage.BarrageBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */

public class MyBarrageView extends SurfaceView {



    public SurfaceHolder surfaceHolder;
    public Canvas canvas;
    public boolean CanDraw;
    public MyBarrageView(Context context) {
        super(context);
    }
    public MyBarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(final SurfaceHolder surfaceHolder) {
                CanDraw = true;
                //当创建完成，就开始绘制
//开始绘制
                new Thread() {
                    @Override
                    public void run() {
                        //拿到画布
                        canvas = surfaceHolder.lockCanvas();
                        //清屏操作,
                        canvas.drawColor(Color.WHITE);
                        Paint paint = new Paint();
                        //抗锯齿
                        paint.setAntiAlias(true);
                        //FC3C39
                        paint.setColor(Color.parseColor("#00ffff"));
                        paint.setStrokeWidth((float) 20.0);
                        canvas.drawLine(0, 0, 100, 100, paint);
                        //提交所做的修改
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                CanDraw = false;
                Log.e("surfaceview被销毁了","=====");
            }
        });

    }

    public MyBarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyBarrageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}

