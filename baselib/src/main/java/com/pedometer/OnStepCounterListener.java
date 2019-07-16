package com.pedometer;

public interface OnStepCounterListener {

    /**
     * 用于显示步数
     * @param step
     */
    void onChangeStepCounter(int step);
}
