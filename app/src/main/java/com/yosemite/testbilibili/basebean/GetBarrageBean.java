package com.yosemite.testbilibili.basebean;

import com.yosemite.testbilibili.barragepackage.BarrageBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */

public class GetBarrageBean {
    List<BarrageBean> returnDatas;
    String errorMessage;
    String result;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<BarrageBean> getReturnDatas() {
        return returnDatas;
    }

    public void setReturnDatas(List<BarrageBean> returnDatas) {
        this.returnDatas = returnDatas;
    }
}
