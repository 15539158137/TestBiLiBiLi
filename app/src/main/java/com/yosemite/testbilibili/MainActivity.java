package com.yosemite.testbilibili;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.bean.VideoijkBean;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    PlayerView player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myplayview);
        startPlayerByCode();

    }
    private void setClicks(){
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
player.startPlay();
            }
        });
        findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
player.pausePlay();
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.stopPlay();
            }
        });
    }
    private void simpleDemo(){
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
    }
    private void startPlayerByCode(){
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
                .setPlaySource(list)
             ;
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo 的内容，恢复系统其它媒体的状态*/
        //MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }

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
        /**demo 的内容，恢复设备亮度状态*/
        //if (wakeLock != null) {
        //    wakeLock.release();
        //}
    }
}
