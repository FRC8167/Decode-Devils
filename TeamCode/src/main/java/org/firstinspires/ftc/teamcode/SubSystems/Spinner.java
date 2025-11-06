package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;

public class Spinner extends Servo1D implements TeamConstants {


    double currentAngleNormalized; //(0-360)
    double previousAngleNormalized;
    double previousRotation;
    TimedTimer timer;


    public Spinner(Servo servo, double initPos, double min, double max, boolean moveOnInit) {
        super(servo, initPos, min, max, moveOnInit);
        currentAngleNormalized = 0;
        previousAngleNormalized = 0;
        previousRotation = 0;
        timer = new TimedTimer();
        update();
    }

    public void setCenteredPositionDegrees(double degrees) {
        previousAngleNormalized = currentAngleNormalized;
        if (((0.5 * SPINNER_RANGE + degrees) / SPINNER_RANGE) <= 1 && ((0.5 * SPINNER_RANGE + degrees) / SPINNER_RANGE) >= 0) {
            setPosition((0.5 * SPINNER_RANGE + degrees) / SPINNER_RANGE);
            currentAngleNormalized = degrees;
        }
        update();
        // 0 degrees is center position  ~-900,~900, center position is none over trapdoor with slots 0,1,2 clockwise
    }

    public double getCenteredPositionDegrees() {
        return currentAngleNormalized;
    }

    public void rotateBy(double degrees) {
        setCenteredPositionDegrees(getCenteredPositionDegrees()+degrees);
    }

    public void continueRotatingBy(double degrees) {
        if (previousRotation >= 0) rotateBy(degrees);
        else rotateBy(-degrees);
    }

    public boolean isDone() {
        return timer.isDone();
    }

    public double getRemainingTime() {
        return timer.getRemainingTime();
    }

    public void update() {
        previousRotation = currentAngleNormalized - previousAngleNormalized;
        timer = new TimedTimer(Math.abs(previousRotation) / SPINNER_SPEED + SPINNER_GRACE_TIME);
    }

    public double getPreviousRotation() {
        return previousRotation;
    }


}