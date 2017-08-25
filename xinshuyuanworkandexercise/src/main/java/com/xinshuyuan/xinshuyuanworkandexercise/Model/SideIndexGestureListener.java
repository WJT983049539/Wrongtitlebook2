package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/7/14.
 */

public class SideIndexGestureListener implements GestureDetector.OnGestureListener {

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

}
