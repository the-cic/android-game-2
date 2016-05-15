package com.mush.weirdo2.interfaces;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by mirko on 15/05/2016.
 */
public interface GameContent {

    public void processInput(MotionEvent event, int panelWidth, int panelHeight);

    public void update(double secondsPerFrame);

    public void draw(Canvas canvas);
}
