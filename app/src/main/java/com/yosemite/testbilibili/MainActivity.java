package com.yosemite.testbilibili;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.bean.VideoijkBean;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.gyf.barlibrary.ImmersionBar;
import com.yosemite.testbilibili.barragepackage.BarrageBean;
import com.yosemite.testbilibili.barragepackage.BarrageView;
import com.yosemite.testbilibili.barragepackage.ScreenSizeUtils;
import com.yosemite.testbilibili.basebean.GetBarrageBean;
import com.yosemite.testbilibili.basebean.HttpBean;
import com.yosemite.testbilibili.http.HttpApiS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends AppCompatActivity {
    //发送的弹幕需要保存起来
    //首先，需要获取视频当前播放的位置，这个需要开定时器去轮训，这个50毫秒一次；
    //对于视频的弹幕信息也需要持续去拿，也是50毫秒一次
    //对于拿到弹幕的使用，根据视频播放的当前进度，去查看拿到的弹幕信息，如果有，就发弹幕，没有，就不发
    //对于这里的弹幕处理，是使用list遍历的方法，为了减少时间，对于已经播放的弹幕就需哟啊根据当前的播放进度，进行相应的剔除操作，
    //对于弹幕的信息：弹幕的内容、颜色、速度、显示的视频播放位置
    int colorModel;//0白色1蓝2红
    int speedModel;
    @BindView(R.id.write)
    CheckBox write;
    @BindView(R.id.blue)
    CheckBox blue;
    @BindView(R.id.red)
    CheckBox red;
    @BindView(R.id.low_speed)
    CheckBox low_speed;
    @BindView(R.id.medium_speed)
    CheckBox medium_speed;
    @BindView(R.id.height_speed)
    CheckBox height_speed;
    @BindView(R.id.editext)
    EditText editText;

    @OnClick(R.id.send)
    void sendMessage() {
//发送一条弹幕
        BarrageBean barrageBean = new BarrageBean();
        barrageBean.setColor(colorModel);
        barrageBean.setSpeend(speedModel);
        if (editText.getText().toString() == null || editText.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        barrageBean.setContent(editText.getText().toString());
        barrageBean.setPosition(player.getCurrentPosition());
        allContent.add(barrageBean);

        Log.e("重新触发了动画", "===");
        startBarrage();
        sendBarrage(barrageBean);
    }

    BarrageView barrage;
    List<BarrageBean> allContent;
    PlayerView player;


    List<MyAnimator> allAnimators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.myplayview);
        ButterKnife.bind(MainActivity.this);
        startPlayerByCode();
        //有关弹幕的信息
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
        initView();

    }

    private void initView() {
        //设置checkbox的显示
        write.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    blue.setChecked(false);
                    red.setChecked(false);
                    colorModel = 0;
                }
            }
        });
        blue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    write.setChecked(false);
                    red.setChecked(false);
                    colorModel = 1;
                }
            }
        });
        red.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    write.setChecked(false);
                    blue.setChecked(false);
                    colorModel = 2;
                }
            }
        });

        low_speed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    medium_speed.setChecked(false);
                    height_speed.setChecked(false);
                    speedModel = 0;
                }
            }
        });
        medium_speed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    low_speed.setChecked(false);
                    height_speed.setChecked(false);
                    speedModel = 1;
                }
            }
        });
        height_speed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    medium_speed.setChecked(false);
                    low_speed.setChecked(false);
                    speedModel = 2;
                }
            }
        });
        //设置状态栏的颜色
        ImmersionBar.with(this)
                //这个的目的是规避状态栏和布局重叠
                .statusBarView(R.id.top_view)
                .statusBarColor(R.color.cl_error)
                //设置状态栏的字体为深色，因为设置颜色后状态栏的文字看不清楚
                .statusBarDarkFont(true)
                //设置字体颜色，特定机型上不能设置的解决
                .flymeOSStatusBarFontColor(R.color.colorAccent)
                .init();
    }

    /*开始弹幕的方法*/
    private void startBarrage() {
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
                //du对于这里的宽和高，默认是300高，屏幕一样的宽度，对于实际情况需要实际处理。
                //根据实际的尺寸去设置宽和高
                int tempY = dip2px(MainActivity.this, 300) * x / 10;
                allContent.get(i).setX(ScreenSizeUtils.getScreenWidth(MainActivity.this));
                allContent.get(i).setY(tempY);
                //设置这个已经是设置过的了
                allContent.get(i).setHadSet(true);
                //根据文字的长度，动态设定最后的位置信息
                final int finalPosition = allContent.get(i).getContent().length() * 70;
                //对于不同的手机型号进行判断

                if (android.os.Build.MANUFACTURER.equals("Letv")) {
                    //乐视手机，就不能使用动画，而是使用定时器来做相关的操纵
//每隔两秒调用一次onNext();
                    final Disposable[] disposable = new Disposable[allContent.size() + 1];
                    Observable observable = Observable.interval(50, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                    final int finalI = i;
                    Observer observer = new Observer() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            disposable[finalI] = d;
                        }

                        @Override
                        public void onNext(@NonNull Object o) {
                            Log.e("数据传输过来", "数据传输");
                            int oneRecodeX;
                            if (allContent.get(finalI).getSpeend() == 0) {
                                //每半秒更新一次，最慢是6秒
                                oneRecodeX = (ScreenSizeUtils.getScreenWidth(MainActivity.this) + finalPosition) / 12 / 10;
                            } else if (allContent.get(finalI).getSpeend() == 1) {
                                oneRecodeX = (ScreenSizeUtils.getScreenWidth(MainActivity.this) + finalPosition) / 8 / 10;
                            } else {
                                oneRecodeX = (ScreenSizeUtils.getScreenWidth(MainActivity.this) + finalPosition) / 6 / 10;
                            }
                            if (allContent.get(finalI).getX() - oneRecodeX < -finalPosition) {
                                disposable[finalI].dispose();
                            } else {
                                allContent.get(finalI).setX(allContent.get(finalI).getX() - oneRecodeX);
                                barrage.setAllContent(allContent);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            Log.e("RXjava调用了完成的方法", "=======");
                        }
                    };
                    observable.subscribe(observer);

                } else {
                    //需要给文字设置动画
                    final ValueAnimator valueAnimator = ValueAnimator.ofInt(ScreenSizeUtils.getScreenWidth(MainActivity.this), -finalPosition);
                    Log.e("动画开始时候的信息", barrage.getWidth() + "==");
                    if (barrageBean.getSpeend() == 0) {
                        valueAnimator.setDuration(6000);
                    } else if (barrageBean.getSpeend() == 1) {
                        valueAnimator.setDuration(4000);
                    } else {
                        valueAnimator.setDuration(3000);
                    }
                    final int finalI1 = i;
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int temp = (int) valueAnimator.getAnimatedValue();
                            Log.e("动画变化", temp + "");
                            allContent.get(finalI1).setX(temp);
                            barrage.setAllContent(allContent);
                        }
                    });
                    valueAnimator.start();
                }
            }


        }


    }

    class MyAnimator {
        ValueAnimator valueAnimator;
        int position;

        public ValueAnimator getValueAnimator() {
            return valueAnimator;
        }

        public void setValueAnimator(ValueAnimator valueAnimator) {
            this.valueAnimator = valueAnimator;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    private void setAllAnimators() {
        for (int i = 0; i < allAnimators.size(); i++) {
            final MyAnimator myAnimator = allAnimators.get(i);
            myAnimator.getValueAnimator().addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int temp = (int) valueAnimator.getAnimatedValue();
                    Log.e("动画变化", temp + "");
                    allContent.get(myAnimator.getPosition()).setX(temp);
                    barrage.setAllContent(allContent);
                }
            });
            myAnimator.getValueAnimator().start();
        }
    }

    private void simpleDemo() {
        String url = "http://9890.vod.myqcloud.com/9890_9c1fa3e2aea011e59fc841df10c92278.f20.mp4";
        player = new PlayerView(MainActivity.this)
                .setTitle("我的测试视频")
                .setScaleType(PlayStateParams.fitparent)
                .hideMenu(true)
                .forbidTouch(false)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        Glide.with(MainActivity.this)
                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
                                .into(ivThumbnail);
                    }
                })
                .setPlaySource(url)
                .startPlay();

        Log.e("视频的时长",player.getDuration()+"");

    }
    private void startPlayerByCode() {
        /**播放资源*/
        List<VideoijkBean> list = new ArrayList<VideoijkBean>();
        String url1 = "http://video.jiecao.fm/11/23/xin/%E5%81%87%E4%BA%BA.mp4";
        String url2 = "http://video.jiecao.fm/11/23/xin/%E5%81%87%E4%BA%BA.mp4";
        VideoijkBean m1 = new VideoijkBean();
        m1.setStream("标清");
        m1.setUrl(url1);
        VideoijkBean m2 = new VideoijkBean();
        m2.setStream("高清");
        m2.setUrl(url2);
        list.add(m1);
        list.add(m2);
/**播放器*/
//对于播放器的裁剪模式
//         PlayStateParams.fitParent:可能会剪裁,保持原视频的大小，显示在中心,当原视频的大小超过 view 的大小超过部分裁剪处理
//         PlayStateParams.fillParent:可能会剪裁,等比例放大视频，直到填满 View 为止,超过 View 的部分作裁剪处理
//         PlayStateParams.wrapcontent:将视频的内容完整居中显示，如果视频大于 view,则按比例缩视频直到完全显示在 view 中
//         PlayStateParams.fitXY:不剪裁,非等比例拉伸画面填满整个 View
//         PlayStateParams.f16_9:不剪裁,非等比例拉伸画面到 16:9,并完全显示在 View 中
//         PlayStateParams.f4_3:不剪裁,非等比例拉伸画面到 4:3,并完全显示在 View 中
        player = new PlayerView(MainActivity.this)
                .setTitle("我的测试视频")
                .setScaleType(PlayStateParams.wrapcontent)
                .hideMenu(true)
                .forbidTouch(false)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        /**加载前显示的缩略图*/
                        Glide.with(MainActivity.this)
                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
                                .into(ivThumbnail);
                    }
                })
                .setPlaySource(list).startPlay()
        ;
        //100ms获取一次播发的进度，这个进度需要和用在弹幕的地方
        //保存一个数值，如果找个数值和新的数值相同，呢么表示视频处于暂停状态
        final int[] tempPosition = new int[1];
        Observable observable = Observable.interval(50, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull Object o) {
                if(player==null){

                }else {
                    if(tempPosition[0]==player.getCurrentPosition()){
                        //表示视频处于暂停状态
                    }else {
                        tempPosition[0] =player.getCurrentPosition();
                        Log.e("视频的长度和当前的进度",player.getDuration()+"="+player.getCurrentPosition());
                        getBarrages(tempPosition[0]);
                    }


                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.e("RXjava调用了完成的方法", "=======");
            }
        };
        observable.subscribe(observer);



    }
    //发送弹幕的方法
    private void sendBarrage(BarrageBean barrageBean){
        Map<String, String> map = new HashMap<>();
        map.put("content", barrageBean.getContent());
        map.put("color", barrageBean.getColor()+"");
        map.put("speed", barrageBean.getSpeend()+"");
        map.put("position", barrageBean.getPosition()+"");
        //116.196.94.144
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://116.196.94.144")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        HttpApiS httpApiS = retrofit.create(HttpApiS.class);
        httpApiS.sendBarrage(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull HttpBean httpBean) {
                        Log.e("获取到的信息",httpBean.getResult()+"===");
                        editText.setText("发送成功了");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("获取弹幕失败", e.getMessage() + "'");
                        editText.setText("发送失败"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });}
    //获取弹幕的方法
    private void getBarrages(final int position){
        Map<String, String> map = new HashMap<>();
        map.put("position",position+"");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://116.196.94.144")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        HttpApiS httpApiS = retrofit.create(HttpApiS.class);
        httpApiS.getBarrages(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetBarrageBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull GetBarrageBean getBarrageBean) {
                        if(getBarrageBean.getReturnDatas()==null||getBarrageBean.getReturnDatas().size()==0){

                        }else {
                            List<BarrageBean> tempBarrages=new ArrayList();
                            for(BarrageBean barrageBean1:getBarrageBean.getReturnDatas()){
                                if(position+100>barrageBean1.getPosition()&&barrageBean1.getPosition()>position){
                                        allContent.add(barrageBean1);
                                    startBarrage();
                                }
                            }
                            Log.e("获取弹幕成功","====="+getBarrageBean.getReturnDatas().get(0).getContent());
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("获取弹幕失败", e.getMessage() + "'");

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    Handler handler;

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        String carrier = android.os.Build.MANUFACTURER;
        Log.e("手机信息", carrier);
        //"Letv"
        startBarrage();


    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
