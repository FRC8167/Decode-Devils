package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.SpinnerCorrector;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;

public class Spinner extends Servo1D implements TeamConstants {


    private double currentAngleNormalized; //(0-360)
    private double previousAngleNormalized;
    private double previousRotation;
    private double approxActualAngleStored; //Only used if movement is interrupted
    private double approxPreviousRotation;

    private double wiggleOffset;
    private TimedTimer timer;


    public Spinner(Servo servo, double initPos, double min, double max, boolean moveOnInit) {
        super(servo, initPos, min, max, moveOnInit);
        currentAngleNormalized = 0;
        previousAngleNormalized = 0;
        previousRotation = 0;
        wiggleOffset = 0;
        timer = new TimedTimer();
        update();
    }

    public void setCenteredPositionDegrees(double degrees) {
        double offsetDegrees = degrees + wiggleOffset;
        approxActualAngleStored = (!timer.isDone() ? getApproxActualAngle() : Double.NaN);

        previousAngleNormalized = currentAngleNormalized;

        if (((0.5 * SPINNER_RANGE + offsetDegrees) / SPINNER_RANGE) <= 1 && ((0.5 * SPINNER_RANGE + offsetDegrees) / SPINNER_RANGE) >= 0) {
//            double adjustedDegrees = SpinnerCorrector.convertActualToSet(degrees);
//            double adjustedPos = (0.5 * SPINNER_RANGE + adjustedDegrees) / SPINNER_RANGE + SPINNER_OFFSET;
//            setPosition(adjustedPos);
            double pos = (0.5 * SPINNER_RANGE + offsetDegrees) / SPINNER_RANGE + SPINNER_OFFSET;
            setPosition(pos);
            currentAngleNormalized = degrees;
        }
        update();
        // 0 degrees is center position  ~-900,~900, (approx. limits -810,810 w/o corrections, -790,790 w/ corrections)
        // center position is none over trapdoor with slots 0,1,2 clockwise
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

    public void setWiggleOffset(double degrees) {
        wiggleOffset = degrees;
        double pos = (0.5 * SPINNER_RANGE + wiggleOffset + currentAngleNormalized) / SPINNER_RANGE + SPINNER_OFFSET;
        setPosition(pos);
    }

    public boolean isDone() {
        return timer.isDone();
    }

    public double getRemainingTime() {
        return timer.getRemainingTime();
    }

    public double getApproxActualAngle() {
        return (
                Double.isNaN(approxActualAngleStored) ? previousAngleNormalized : approxActualAngleStored
        ) + (
                Double.isNaN(approxPreviousRotation) ? previousRotation : approxPreviousRotation
        ) * timer.getProportionCompleted();
    }

    public void update() { //TODO: Test and decide if grace time should be removed
        previousRotation = currentAngleNormalized - previousAngleNormalized;
        approxPreviousRotation = (!Double.isNaN(approxActualAngleStored)) ? (currentAngleNormalized - approxActualAngleStored) : Double.NaN;
        timer = new TimedTimer(Math.abs(Double.isNaN(approxPreviousRotation) ? previousRotation : approxPreviousRotation) / SPINNER_SPEED + SPINNER_GRACE_TIME);
    }

    public double getPreviousRotation() {
        return previousRotation;
    }



}