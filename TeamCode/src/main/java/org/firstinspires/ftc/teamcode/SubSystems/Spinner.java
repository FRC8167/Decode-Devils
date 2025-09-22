package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Spinner extends Servo1D implements TeamConstants {


    double currentAngleNormalized; //(0-360)
    double previousAngleNormalized;
    double previousRotation;

    public Spinner(Servo servo, double initPos, double min, double max, boolean moveOnInit) {
        super(servo, initPos, min, max, moveOnInit);
        currentAngleNormalized = 0;
    }

    public void setCenteredPositionDegrees(double degrees) {
        previousAngleNormalized = currentAngleNormalized;
        if (((0.5 * SPINDEXER_RANGE + degrees) / SPINDEXER_RANGE) <= 1 && ((0.5 * SPINDEXER_RANGE + degrees) / SPINDEXER_RANGE) >= 0) {
            setPosition((0.5 * SPINDEXER_RANGE + degrees) / SPINDEXER_RANGE);
            currentAngleNormalized = degrees;
        }
        update();
        // 0 degrees is center position -900,900, center position is none over trapdoor with slots 0,1,2 clockwise
    }

    public double getCenteredPositionDegrees() {
        return currentAngleNormalized;
    }

    public void rotateBy(double degrees) {
        setCenteredPositionDegrees(getCenteredPositionDegrees()+degrees);
    }

    public void update() {
        previousRotation = currentAngleNormalized - previousAngleNormalized;
    }

    public double getPreviousRotation() {
        return previousRotation;
    }


}