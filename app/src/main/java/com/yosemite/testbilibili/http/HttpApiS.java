package com.yosemite.testbilibili.http;

import com.yosemite.testbilibili.basebean.GetBarrageBean;
import com.yosemite.testbilibili.basebean.HttpBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/9/14.
 */

public interface HttpApiS {
    /*获取弹幕的方法*/
    //GetBarrages
    @FormUrlEncoded
    @POST("/MyTestWChat/GetBarrages")
    Observable<GetBarrageBean> getBarrages(@FieldMap Map<String, String> params);
    /*发送弹幕的方法*/
    @FormUrlEncoded
    @POST("/MyTestWChat/myPost")
    Observable<HttpBean> sendBarrage(@FieldMap Map<String, String> params);
}
