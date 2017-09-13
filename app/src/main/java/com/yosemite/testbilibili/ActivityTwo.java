package com.yosemite.testbilibili;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yosemite.testbilibili.barragepackage.Barrage;
import com.yosemite.testbilibili.barragepackage.BarrageBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ActivityTwo extends Activity{
  Barrage barrage;
  List<BarrageBean> allContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
       barrage= findViewById(R.id.danmu);
      if(barrage==null){
        Log.e("在oncreat中就是个空的","=======");
      }
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

      barrage.setAllContent(allContent);



    }


}
