package com.mush.weirdo2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by mirko on 15/05/2016.
 */
public class GroundTile {
    private Bitmap image;
    private int width;
    private int height;
    private Point pivot;

    public GroundTile(Resources resources, int resourceId) {
        this.image = BitmapFactory.decodeResource(resources, resourceId);
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public void draw(double x, double y, Canvas canvas) {
        canvas.drawBitmap(image, (float) (x), (float) (y), null);
    }

}
