package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Spinner extends Servo1D implements TeamConstants {


    double currentAngleNormalized; //(0-360)
    double previousAngleNormalized;

    public Spinner(Servo servo, double initPos, double min, double max, boolean moveOnInit) {
        super(servo, initPos, min, max, moveOnInit);
        currentAngleNormalized = 0;
    }

    public void setCenteredPositionDegrees(double degrees) {
        if (((0.5 * SPINDEXER_RANGE + degrees) / SPINDEXER_RANGE) <= 1 && ((0.5 * SPINDEXER_RANGE + degrees) / SPINDEXER_RANGE) >= 0) {
            setPosition((0.5 * SPINDEXER_RANGE + degrees) / SPINDEXER_RANGE);
            currentAngleNormalized = degrees;
        }
        // 0 degrees is center position -900,900, center position is none over trapdoor with slots 0,1,2 clockwise
    }

    public double getCenteredPositionDegrees() {
        return currentAngleNormalized;
    }

    public void rotateBy(double degrees) {
        setCenteredPositionDegrees(getCenteredPositionDegrees()+degrees);
    }


}