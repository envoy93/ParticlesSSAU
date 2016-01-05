package com.shashov.particles.model;


import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import com.shashov.particles.MainCanvas;

/**
 * Created by kirill on 19.12.2015.
 */
public class Body {

    private double x, y, weight, xVelocity, yVelocity, xForce, yForce;

    private int color;
    private int radius;

    public Body(double xPos, double yPos, double mass, double xVel, double yVel, int c) {
        this.x = xPos;
        this.y = yPos;
        this.weight = mass;
        this.xVelocity = xVel;
        this.yVelocity = yVel;
        this.color = c;
        this.radius = this.calcRadius(this.weight);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void render(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(color);
        canvas.drawCircle((float) x - radius, (float) y - radius, radius, p);
    }

    public void update() {
        xVelocity += Constants.VELOCITY_FACTOR * xForce / weight;
        yVelocity += Constants.VELOCITY_FACTOR * yForce / weight;

        x -= Constants.VELOCITY_FACTOR * xVelocity;
        y -= Constants.VELOCITY_FACTOR * yVelocity;

        if (x < radius) {
            x = radius;
            xVelocity = 0;
        }
        if (x > (MainCanvas.width-radius)) {
            x = MainCanvas.width-radius;
            xVelocity = 0;
        }
        if (y < radius) {
            y = radius;
            yVelocity = 0;
        }
        if (y > MainCanvas.height - radius) {
            y = MainCanvas.height - radius;
            yVelocity = 0;
        }
    }

    public void resetForce() {
        xForce = 0;
        yForce = 0;
    }

    public synchronized void addForce(Body otherBody) {
        double hack = 150;
        double dx = x - otherBody.getX();
        double dy = y - otherBody.getY();

        double dist = Math.sqrt((dx * dx) + (dy * dy));
        double force = (Constants.GRAVITY * this.weight * otherBody.getWeight()) / (dist * dist + hack * hack);
        if (dist == 0) {
            dist = 1e-10;
        }
        xForce += force * dx / dist;
        yForce += force * dy / dist;

        otherBody.addXForce(-force * dx / dist);
        otherBody.addYForce(-force * dy / dist);
    }

    private synchronized void addXForce(double force) {
        xForce += force;
    }

    private synchronized void addYForce(double force) {
        yForce += force;
    }

    public static final int calcRadius(double weight) {
        return (int) (26 * weight / 10000) / 1999 + 7970 / 1999;
    }

    //###########################################################################################################################

    public double getArea() {
        return Math.PI * radius * radius;
    }

    public double getAngle() {
        double result = ((Math.atan2(yVelocity, xVelocity)) / (Math.PI / 180));

        if (result < 0) {
            return 360 + result;
        }
        return result;
    }

    public double distTo(Body otherBody) {
        double dx = x - otherBody.getX();
        double dy = y - otherBody.getY();
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    public void moveTo(double x, double y) {
        xForce = x - this.x;
        yForce = y - this.y;
    }

    public boolean isOut() {
        if (x > MainCanvas.width || x < 0 || y > MainCanvas.height || y < 0) {
            return true;
        }
        return false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWeight() {
        return weight;
    }

}
