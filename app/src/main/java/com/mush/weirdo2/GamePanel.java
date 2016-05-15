package com.mush.weirdo2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mush.weirdo2.interfaces.GameContent;

import java.text.MessageFormat;

/**
 * Created by mirko on 15/05/2016.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 180/2;
    public static final int HEIGHT = 100/2;
    private MainThread mainThread;
    private GameContent gameContent;
    private Paint fpsPaint;

    public GamePanel(Context context) {
        super(context);

        System.out.println("New game panel");
        getHolder().addCallback(this);

        Typeface fpsTypeface = Typeface.create("sans-serif", Typeface.BOLD);
        fpsPaint = new Paint();

        fpsPaint.setColor(Color.RED);
        fpsPaint.setStyle(Paint.Style.FILL);
        fpsPaint.setTextSize(20);
        fpsPaint.setTypeface(fpsTypeface);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("Surface Created");

        if (gameContent == null) {
            System.out.println("Create gameContent");
            gameContent = new MainGameContent(getResources());
        }

        if (mainThread == null) {
            System.out.println("Create main thread");
            mainThread = new MainThread(getHolder(), this);
            mainThread.setRunning(true);
            mainThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println(MessageFormat.format("Surface Changed f:{0} w:{1} h:{2}", format, width, height));
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("Surface Destroyed");
        boolean retry = true;
        while (retry) {
            try {
                System.out.println("set running false");
                mainThread.setRunning(false);
                System.out.println("join");
                mainThread.join();
                retry = false;
                System.out.println("running false and thread joined");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mainThread = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean b = super.onTouchEvent(event);

        processInput(event);

        // Follow move events on the right side, and only touch events on the left
        if (!(event.getX() < getWidth() * 0.1 && event.getY() < getHeight() * 0.1)) {
            return true;
        }
        return b;
    }

    private void processInput(MotionEvent event) {
        if (event.getX() < getWidth() * 0.1 && event.getY() < getHeight() * 0.1) {
            if (mainThread != null) {
                switch (mainThread.FPS) {
                    case 30:
                        mainThread.FPS = 60;
                        break;
                    case 60:
                        mainThread.FPS = 15;
                        break;
                    case 15:
                        mainThread.FPS = 30;
                        break;
                    default:
                        mainThread.FPS = 30;
                }
            }
        }

        gameContent.processInput(event, getWidth(), getHeight());
    }

    public void update(double secondsPerFrame) {
        gameContent.update(secondsPerFrame);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        render(canvas);
    }

    private void render(Canvas canvas) {
        final float scaleFactorX = (float) getWidth() / WIDTH;
        final float scaleFactorY = (float) getHeight() / HEIGHT;
        final float scaleFactor = Math.min(scaleFactorX, scaleFactorY);
        final float offsetX;
        final float offsetY;

        if (scaleFactorX > scaleFactorY) {
            offsetX = ((float) getWidth() - WIDTH * scaleFactor) / 2;
            offsetY = 0;
        } else {
            offsetX = 0;
            offsetY = ((float) getHeight() - HEIGHT * scaleFactor) / 2;
        }

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.translate(offsetX, offsetY);
            canvas.clipRect(0, 0, getWidth() - offsetX * 2, getHeight() - offsetY * 2);
            canvas.scale(scaleFactor, scaleFactor);
            gameContent.draw(canvas);
            canvas.restoreToCount(savedState);

            if (mainThread != null) {
                double fps = mainThread.getAverageFPS();
                double fpsDiff = mainThread.FPS - fps;

                if (fpsDiff < mainThread.FPS * 0.2) {
                    fpsPaint.setColor(Color.GREEN);
                } else if (fpsDiff < mainThread.FPS * 0.4) {
                    fpsPaint.setColor(Color.YELLOW);
                } else {
                    fpsPaint.setColor(Color.RED);
                }

                canvas.drawText("Fps: " + fps, 10, 20, fpsPaint);
            }
        }
    }

}