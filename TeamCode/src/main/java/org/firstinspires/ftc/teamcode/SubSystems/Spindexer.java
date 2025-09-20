package org.firstinspires.ftc.teamcode.SubSystems;


import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Spindexer implements TeamConstants {

    int activeSlotDrop; //
    int activeSlotSensor; // assumes color sensor is opposite to drop
    double currentAngleNormalized; //(0-360)
    Spinner spinner;
    SpinStatesSingleton spinStates;
    ColorDetection colorDetection;


    public Spindexer(Spinner spinner, SpinStatesSingleton spinStates, ColorDetection colorDetection) {
        this.spinner = spinner;
        this.spinStates = spinStates;
        this.colorDetection = colorDetection;
        activeSlotDrop = -1;
        activeSlotSensor = -1;
        currentAngleNormalized = 0;
        update();
    }

    public void setCenteredPositionDegrees(double degrees) {
        spinner.setCenteredPositionDegrees(degrees);
        update();
    }

    public double getCenteredPositionDegrees() {
        return spinner.getCenteredPositionDegrees();
    }

    public void rotateBy(double degrees) {
        spinner.rotateBy(degrees);
    }

    public double getPosition() {
        return spinner.servoPos();
    }
//
//    public void setPosition(double pos) {
//        spinner.setPosition(pos);
//    }


    public void rotateSlotToDrop(int slot) {
        update();
        double targetAngle;
        switch (slot) {
            case 0: targetAngle = 300; break;
            case 1: targetAngle = 180; break;
            case 2: targetAngle = 60; break;
            default: targetAngle = spinner.getCenteredPositionDegrees(); // No change if invalid slot
        }
        // Calculate the shortest path to the target angle
        double delta = ((targetAngle - currentAngleNormalized + 540) % 360) - 180; // Calculate shortest path (-180 to 180)
        if (currentAngleNormalized + delta > SPINDEXER_RANGE/2)
            delta -= 360;
        else if (currentAngleNormalized + delta < -SPINDEXER_RANGE/2)
            delta += 360;
        spinner.rotateBy(delta);
        update();
    }

    public void rotateSlotToSensor(int slot) {
        update();
        double targetAngle;
        switch (slot) {
            case 0: targetAngle = 120; break;
            case 1: targetAngle = 0; break;
            case 2: targetAngle = 240; break;
            default: targetAngle = spinner.getCenteredPositionDegrees(); // No change if invalid slot
        }
        // Calculate the shortest path to the target angle
        double delta = ((targetAngle - currentAngleNormalized + 540) % 360) - 180; // Calculate shortest path (-180 to 180)
        if (currentAngleNormalized + delta > SPINDEXER_RANGE/2)
            delta -= 360;
        else if (currentAngleNormalized + delta < -SPINDEXER_RANGE/2)
            delta += 360;
        spinner.rotateBy(delta);
        update();
    }

    public void update() {
        currentAngleNormalized = ((spinner.getCenteredPositionDegrees()%360+360)%360);
        switch ((int) currentAngleNormalized) {
            case 300: activeSlotDrop = 0; break;
            case 180: activeSlotDrop = 1; break;
            case 60: activeSlotDrop = 2; break;
            default: activeSlotDrop = -1; break;
        }
        switch ((int) currentAngleNormalized) {
            case 120: activeSlotSensor = 0; break;
            case 0: activeSlotSensor = 1; break;
            case 240: activeSlotSensor = 2; break;
            default: activeSlotSensor = -1; break;
        }

    }

    public int getActiveSlotDrop() {
        update();
        return activeSlotDrop;
    }

    public int getActiveSlotSensor() {
        update();
        return activeSlotSensor;
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