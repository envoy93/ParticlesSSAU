package com.shashov.particles.model;

import android.graphics.Color;
import com.shashov.particles.MainCanvas;
import com.shashov.particles.MainFragment;
import com.shashov.particles.SettingsFragment;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by kirill on 30.12.2015.
 */
public class MainThread extends Thread {

    private final MainCanvas canvas;
    public boolean isExit = false;
    private static ArrayList<Body> queue = new ArrayList<>();
    private ArrayList<Worker> workers = new ArrayList<>();
    private CyclicBarrier cyclicBarrier;
    private CyclicBarrier cyclicBarrierStart;
    private Space space;

    public static boolean switcher = true;
    public static boolean switcherDelete = false;
    private boolean running;
    private long fps = -1;

    public void setRunning(boolean running) {
        this.running = running;
    }


    public MainThread(MainCanvas canvas) {
        this.canvas = canvas;
        space = new Space();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loop();
    }

    private void loop() {

        int countOfThreads = 3;
        cyclicBarrierStart = new CyclicBarrier(countOfThreads + 1);
        cyclicBarrier = new CyclicBarrier(countOfThreads + 1);
        for (int i = 0; i < countOfThreads; i++) {
            Worker worker = new Worker(i, space, cyclicBarrier, cyclicBarrierStart, countOfThreads, this);
            synchronized (workers) {
                workers.add(worker);
            }
            worker.start();
        }


        long time3 = 0;
        long time = 0;
        long time2 = 0;
        while (running) {
            time = System.currentTimeMillis();
            if (switcher) {
                space.resetAll();
                // singleThread();
                process();
                space.update();
                canvas.postInvalidate();
            }



            if (switcherDelete) {
                space.delAll();
                switcherDelete = false;
            }

            if (!queue.isEmpty()) {
                synchronized (queue) {
                    space.addAll(queue);
                    queue.clear();
                }
            }

            time2 = System.currentTimeMillis() - time;

            if (time2 < 50) {
                try {
                    Thread.sleep(50 - time2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            time3 = System.currentTimeMillis() - time;
            if (time3 == 0) {
                time3 = 1;
            }
            fps = 1000 / time3;

        }
    }

    private void process() {
        try {
            cyclicBarrierStart.await();
            cyclicBarrierStart.reset();
            cyclicBarrier.await();
            cyclicBarrier.reset();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            if (isExit) {
                return;
            }
            e.printStackTrace();
        }

    }

    public long getFPS(){
        return fps;
    }

    public Space getSpace(){
        return space;
    }

    public static void handleAddButton() {
        Random random = new Random();
        synchronized (MainFragment.weight) {
            if (MainFragment.weight <= 0){
                return;
            }
        }
        synchronized (queue) {
            for (int i = 0; i < SettingsFragment.count; i++) {
                double x = random.nextInt(MainCanvas.width - 2 * Body.calcRadius(MainFragment.weight * 1e6)) + Body.calcRadius(MainFragment.weight * 1e6);
                double y = random.nextInt(MainCanvas.height - 2 * Body.calcRadius(MainFragment.weight * 1e6)) + Body.calcRadius(MainFragment.weight * 1e6);
                Body body = new Body(x, y, MainFragment.weight * 1e6, 0, 0, Color.rgb(random.nextInt(200), random.nextInt(200), random.nextInt(200)));

                queue.add(body);

            }
        }
    }

    public static void handleTouchClick(float x, float y) {
        Random random = new Random();
        synchronized (MainFragment.weight) {
            if (MainFragment.weight <= 0){
                return;
            }
        }
        Body body = new Body(x, y, MainFragment.weight * 1e6, 0, 0, Color.rgb(random.nextInt(200), random.nextInt(200), random.nextInt(200)));
        synchronized (queue) {
            queue.add(body);
        }
    }

    public static void handlePlayButton() {
        switcher = !switcher;
    }

    public static void handleDeleteButton() {
        switcherDelete = true;
    }
}
