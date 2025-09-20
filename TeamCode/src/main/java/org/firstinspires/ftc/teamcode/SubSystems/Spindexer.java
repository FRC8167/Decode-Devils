package org.firstinspires.ftc.teamcode.SubSystems;


import static org.firstinspires.ftc.teamcode.RobotConfiguration.colorDetection;
import static org.firstinspires.ftc.teamcode.RobotConfiguration.spinStates;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Spindexer extends Servo1D implements TeamConstants {

    int activeSlotDrop; //
    int activeSlotSensor; // assumes color sensor is opposite to drop
    double currentAngleNormalized; //(0-360)

    public Spindexer(Servo servo, double initPos, double min, double max, boolean moveOnInit) {
        super(servo, initPos, min, max, moveOnInit);
        activeSlotDrop = -1;
        activeSlotSensor = -1;
        currentAngleNormalized = 0;
    }

    public void setCenteredPositionDegrees(double degrees) {
        setPosition((degrees+0.5*SPINDEXER_RANGE)/SPINDEXER_RANGE);
        update();
        // 0 degrees is center position -900,900, center position is none over trapdoor with slots 0,1,2 clockwise
    }

    public double getCenteredPositionDegrees() {
        return servoPos()*SPINDEXER_RANGE-0.5*SPINDEXER_RANGE;
    }

    public void rotateBy(double degrees) {
        setCenteredPositionDegrees(getCenteredPositionDegrees()+degrees);
    }

    public void moveToCenter() {
        setPosition(0.5);
    }

    public void rotateSlotToDrop(int slot) {
        update();
        double targetAngle;
        switch (slot) {
            case 0: targetAngle = 60; break;
            case 1: targetAngle = 180; break;
            case 2: targetAngle = 300; break;
            default: targetAngle = getCenteredPositionDegrees(); // No change if invalid slot
        }
        // Calculate the shortest path to the target angle
        double delta = ((targetAngle - currentAngleNormalized + 540) % 360) - 180; // Calculate shortest path (-180 to 180)
        if (currentAngleNormalized + delta > SPINDEXER_RANGE/2)
            delta -= 360;
        else if (currentAngleNormalized + delta < -SPINDEXER_RANGE/2)
            delta += 360;
        rotateBy(delta);
        update();
    }

    public void rotateSlotToSensor(int slot) {
        update();
        double targetAngle;
        switch (slot) {
            case 0: targetAngle = 240; break;
            case 1: targetAngle = 0; break;
            case 2: targetAngle = 120; break;
            default: targetAngle = getCenteredPositionDegrees(); // No change if invalid slot
        }
        // Calculate the shortest path to the target angle
        double delta = ((targetAngle - currentAngleNormalized + 540) % 360) - 180; // Calculate shortest path (-180 to 180)
        if (currentAngleNormalized + delta > SPINDEXER_RANGE/2)
            delta -= 360;
        else if (currentAngleNormalized + delta < -SPINDEXER_RANGE/2)
            delta += 360;
        rotateBy(delta);
        update();
    }

    public void update() {
        currentAngleNormalized = ((getCenteredPositionDegrees()%360+360)%360);
        switch ((int) currentAngleNormalized) {
            case 60: activeSlotDrop = 0; break;
            case 180: activeSlotDrop = 1; break;
            case 300: activeSlotDrop = 2; break;
            default: activeSlotDrop = -1; break;
        }
        switch ((int) currentAngleNormalized) {
            case 0: activeSlotSensor = 1; break;
            case 120: activeSlotSensor = 2; break;
            case 240: activeSlotSensor = 0; break;
            default: activeSlotSensor = -1; break;
        }

    }


    public void detectColor() { // TODO: Mount color sensor to spindexer & confirm positioning
        update();
        spinStates.setSlot(activeSlotSensor, colorDetection.getState());
    }

    public void drop() { // assumes successful drop TODO: Add actual drop function w/ linear servo
        update();
        spinStates.setSlot(activeSlotDrop, State.None);

    }




}