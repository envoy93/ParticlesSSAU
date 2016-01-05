package com.shashov.particles.model;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by kirill on 22.12.2015.
 */
public class Worker extends Thread {
    private final CyclicBarrier barrier;
    private final CyclicBarrier barrierStart;
    private final MainThread frame;
    private int index;
    private int count;
    private Space space;
    public boolean isExit = false;

    public Worker(int index, Space space, CyclicBarrier barrier, CyclicBarrier barrierStart, int count, MainThread frame) {
        this.index = index;
        this.space = space;
        this.barrier = barrier;
        this.barrierStart = barrierStart;
        this.count = count;
        this.frame = frame;
    }

    @Override
    public void run() {
        while (true) {


            try {
                barrierStart.await();
            } catch (Exception e) {
                if (frame.isExit) {
                    System.out.println("exit");
                    return;
                }
                e.printStackTrace();
            }

            int i = index;
            int k = 0;
            while (i < space.getSize()) {
                Body body = space.getBody(i);
                for (int j = i + 1; j < space.getSize(); j++) {
                    body.addForce(space.getBody(j));
                }

                //get new i
                if (k % 2 != 0) {
                    i = k * count + (count - 1 - index);
                } else {
                    i = index + k * count;
                }
                k++;
            }

            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }


        }
    }
}
