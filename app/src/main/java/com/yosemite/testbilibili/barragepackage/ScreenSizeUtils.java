package com.yosemite.testbilibili.barragepackage;

import android.content.Context;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/4.
 */

public class ScreenSizeUtils {
    public static Map getScreenSize(Context mContext) {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Map map = new HashMap();
        map.put("width", width);
        map.put("height", height);
        return map;
    }

    public static int getScreenHeight(Context mContext) {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Map map = new HashMap();
        map.put("width", width);
        map.put("height", height);
        return height;
    }

    public static int getScreenWidth(Context mContext) {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Map map = new HashMap();
        map.put("width", width);
        map.put("height", height);
        return width;
    }
}
