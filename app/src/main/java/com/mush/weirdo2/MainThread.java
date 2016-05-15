package com.mush.weirdo2;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by mirko on 15/05/2016.
 */
public class MainThread extends Thread {

    private static final long NANOS_PER_MILLISECOND = 1000000;
    private static final long NANOS_PER_SECOND = 1000 * NANOS_PER_MILLISECOND;

    public int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    private static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long currentTime;
        long elapsedTime;
        long targetTime;
        long waitTime;
        long remainingTime;
        long totalTime = 0;
        int frameCount = 0;

        long lastTime = System.nanoTime();

        System.out.println("thread run started");

        while (running) {
            currentTime = System.nanoTime();
            elapsedTime = currentTime - lastTime;

            targetTime = NANOS_PER_SECOND / FPS;

            updateAndDraw(elapsedTime);

            lastTime = currentTime;
            currentTime = System.nanoTime();
            remainingTime = targetTime - (currentTime - lastTime);

            if (remainingTime > 0) {
                waitTime = remainingTime / NANOS_PER_MILLISECOND;
            } else {
                waitTime = 1;
            }

            try {
                this.sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += elapsedTime;
            frameCount++;

            if (frameCount >= FPS) {
                averageFPS = NANOS_PER_SECOND / (totalTime / frameCount);
                frameCount = 0;
                totalTime = 0;
//                System.out.println("fps:"+averageFPS);
//                System.out.println("e:"+elapsedTime + "t:"+targetTime);
//                System.out.println("w:"+waitTime);
            }
        }

        System.out.println("thread run finished");
    }

    private void updateAndDraw(long elapsedTime) {
        canvas = null;
        double elapsedSeconds = (double) elapsedTime / NANOS_PER_SECOND;

        try {
            canvas = this.surfaceHolder.lockCanvas();
            synchronized (surfaceHolder) {
                this.gamePanel.update(elapsedSeconds);
                this.gamePanel.draw(canvas);
            }
        } catch (Exception e) {

        } finally {
            if (canvas != null) {
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void setRunning(boolean b) {
        running = b;
    }

    public double getAverageFPS() {
        return averageFPS;
    }
}
