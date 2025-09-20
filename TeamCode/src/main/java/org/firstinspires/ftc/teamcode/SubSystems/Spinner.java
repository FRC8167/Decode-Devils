package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Spinner extends Servo1D implements TeamConstants {

    int activeSlotDrop; //
    int activeSlotSensor; // assumes color sensor is opposite to drop
    double currentAngleNormalized; //(0-360)

    public Spinner(Servo servo, double initPos, double min, double max, boolean moveOnInit) {
        super(servo, initPos, min, max, moveOnInit);
        activeSlotDrop = -1;
        activeSlotSensor = -1;
        currentAngleNormalized = 0;
    }

    protected void setCenteredPositionDegrees(double degrees) {
        setPosition((degrees+0.5*SPINDEXER_RANGE)/SPINDEXER_RANGE);
        // 0 degrees is center position -900,900, center position is none over trapdoor with slots 0,1,2 clockwise
    }

    public double getCenteredPositionDegrees() {
        return servoPos()*SPINDEXER_RANGE-0.5*SPINDEXER_RANGE;
    }

    public void rotateBy(double degrees) {
        setCenteredPositionDegrees(getCenteredPositionDegrees()+degrees);
    }
}