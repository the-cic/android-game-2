package com.mush.weirdo2;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.mush.weirdo2.interfaces.GameContent;

/**
 * Created by mirko on 15/05/2016.
 */
public class MainGameContent implements GameContent {

    GroundTile green;
    GroundTile brown;
    double uOffset = 0;
    double vOffset = 0;
    double tempFi = 0;

    public MainGameContent(Resources resources) {
        green = new GroundTile(resources, R.drawable.tile_green);
        brown = new GroundTile(resources, R.drawable.tile_brown);
    }

    public void processInput(MotionEvent event, int panelWidth, int panelHeight) {
    }

    public void update(double secondsPerFrame) {
        tempFi += 0.5 * secondsPerFrame;
        uOffset = Math.sin(tempFi) * 6;
    }

    public void draw(Canvas canvas) {
//        green.draw(10, 10, canvas);
//        brown.draw(10+16, 10+8+1, canvas);
        boolean vEven = true;
        double screenX0 = (GamePanel.WIDTH / 2) - 16;
        double screenY0 = 0;
        int u0 = -((int)(uOffset/2))*2;
        screenX0 += (uOffset % 2) * 16;
        int horCount = (GamePanel.WIDTH / 32) / 2;
        int verCount = (GamePanel.HEIGHT / (16 / 2))-1;
        for (int v = 0; v < verCount; v++) {
            for (int ui = -horCount - (uOffset > 0 ? 1 : 0); ui <= horCount + (uOffset < 0 ? 1 : 0); ui++) {
                int u = u0 + (vEven ? ui * 2 : ui * 2 + 1);
                int x = (v + u) / 2;
                int y = (v - u) / 2;
                double screenX = screenX0 + (u-u0) * 16;
                double screenY = screenY0 + v * 9;
                if (x == 1 || y == 1 || (x == 0 && y == 0)) {
                    brown.draw(screenX, screenY, canvas);
                } else {
                    green.draw(screenX, screenY, canvas);
                }
            }
            vEven = !vEven;
        }
    }
}
