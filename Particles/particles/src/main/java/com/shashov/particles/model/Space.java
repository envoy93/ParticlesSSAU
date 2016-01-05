package com.shashov.particles.model;


import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by kirill on 19.12.2015.
 */
public class Space {
    private List<Body> safeBodies;

    public Space() {
        safeBodies = new ArrayList<Body>();//Collections.synchronizedList(new ArrayList<Body>());
        safeBodies.clear();
    }

    public void render(Canvas canvas) {
        if (!this.safeBodies.isEmpty()) {
            synchronized (safeBodies) {
                Iterator<Body> it = safeBodies.iterator();
                while (it.hasNext()) {
                    Body body = it.next();
                    body.render(canvas);
                }
            }
        }
    }

    public void addBody(int x, int y, double weight) {
        Random rand = new Random();
        synchronized (safeBodies) {
            safeBodies.add(new Body(x, y, weight, 0, 0, Color.argb(0, rand.nextInt(), rand.nextInt(), rand.nextInt())));
        }
    }

    public void addBody(Body body) {
        synchronized (safeBodies) {
            safeBodies.add(body);
        }
    }

    public boolean notEmpty() {
        if (safeBodies.isEmpty()) {
            return false;
        }
        return true;
    }

    public void update() {
        if (!this.safeBodies.isEmpty()) {
            Iterator<Body> it = safeBodies.iterator();
            while (it.hasNext()) {
                Body body = it.next();
                body.update();
            }
        }
    }

    public void addForces() {
        for (int i = 0; i < safeBodies.size(); i++) {
            Body bodyOne = safeBodies.get(i);
            for (int j = i+1; j < safeBodies.size(); j++) {
                Body bodyTwo = safeBodies.get(j);
                bodyOne.addForce(bodyTwo);
            }

        }
    }

    public int getSize() {
        return this.safeBodies.size();
    }

    public void delAll() {
        synchronized (safeBodies) {
            safeBodies.clear();
        }
    }

    public void resetAll() {
        for (Object a : safeBodies) {
            Body bodyOne = (Body) a;
            bodyOne.resetForce();
        }
    }

    public Body getBody(int i) {
        return safeBodies.get(i);
    }

    public void addAll(List bodies) {
        synchronized (safeBodies) {
            safeBodies.addAll(bodies);
        }
    }
}
