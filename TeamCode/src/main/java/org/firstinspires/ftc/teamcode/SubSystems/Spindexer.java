package org.firstinspires.ftc.teamcode.SubSystems;



import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

import java.util.Arrays;

public class Spindexer implements TeamConstants {

    int activeSlotDrop; //
    int activeSlotSensor;// assumes color sensor is opposite to drop
    double fractionalSlotDrop;
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
        fractionalSlotDrop = -1;
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
        update();
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

    public void rotateStateToDrop(State state) {
        update();
        int[] indexes = spinStates.getIndexesOfStateInStates(state);
        double[] distances = new double[indexes.length];
        boolean found = false;
        boolean secondFound = false;
        int foundIndex = -1;
        int secondFoundIndex = -1;
        double rotation;
        if (indexes.length > 0) {
            if (indexes.length == 1) {
                rotateSlotToDrop(indexes[0]);
            }
            else {
                for (int i = 0; i < indexes.length; i++) {
                    double targetSlotValue = (double)indexes[i]; // The actual slot number (0, 1, or 2)

                    double forwardSlotDist = (targetSlotValue - fractionalSlotDrop + 3.0) % 3.0;
                    // Ensure forwardSlotDist is always in [0, 3)
                    if (forwardSlotDist < 0) forwardSlotDist += 3.0;


                    double backwardSlotDist = (fractionalSlotDrop - targetSlotValue + 3.0) % 3.0;
                    // Ensure backwardSlotDist is always in [0, 3)
                    if (backwardSlotDist < 0) backwardSlotDist += 3.0;

                    distances[i] = Math.min(forwardSlotDist, backwardSlotDist);
                }
                double min = Arrays.stream(distances).min().getAsDouble();
                for (int i = 0; i < indexes.length; i++) {
                    if (!found && distances[i] == min) {
                        found = true;
                        foundIndex = indexes[i];
                    }
                    if (found && distances[i] == min && indexes[i] != foundIndex) {
                        secondFound = true;
                        secondFoundIndex = indexes[i];
                    }
                }
                if (found && secondFound) {
                    if (spinner.getPreviousRotation() >= 0) {
                        if (activeSlotDrop != -1) {
                            rotation = 120;
//                            setIndex = (activeSlotDrop-1+3)%3;
                        }
                        else {
                            rotation = 60;
//                            setIndex = Math.toIntExact(Math.round(fractionalSlotDrop - 0.5));
                        }
                    }
                    else if (spinner.getPreviousRotation() < 0) {
                        if (activeSlotDrop != -1) {
                            rotation = -120;
//                            setIndex = (activeSlotDrop+1)%3;
                        }
                        else {
                            rotation = -60;
//                            setIndex = Math.toIntExact(Math.round(fractionalSlotDrop + 0.5))%3;
                        }
                    }
                    else {
                        throw new IllegalStateException("IDK, something went horribly wrong. Pls Fix");
                    }
                    rotateBy(rotation);
                    update();
                    if (activeSlotDrop != foundIndex && activeSlotDrop != secondFoundIndex) {
                        throw new IllegalStateException("IDK, something went horribly wrong. Pls Fix");
                    }
                }
                else if (found) {
                    rotateSlotToDrop(foundIndex);
                }

                else {
                    throw new IllegalStateException("IDK, something went horribly wrong. Pls Fix");
                }

            }
        }

    }

    public void update() {
        currentAngleNormalized = ((spinner.getCenteredPositionDegrees()%360+360)%360);
        switch ((int) currentAngleNormalized) {
            case 300: activeSlotDrop = 0; fractionalSlotDrop = 0; break;
            case 180: activeSlotDrop = 1; fractionalSlotDrop = 1; break;
            case 60: activeSlotDrop = 2; fractionalSlotDrop = 2; break;
            default: activeSlotDrop = -1; break;
        }
        switch ((int) currentAngleNormalized) {
            case 120: activeSlotSensor = 0; fractionalSlotDrop = 1.5; break;
            case 0: activeSlotSensor = 1; fractionalSlotDrop = 2.5; break;
            case 240: activeSlotSensor = 2; fractionalSlotDrop = 0.5; break;
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

    public double getFractionalSlotDrop() {
        update();
        return fractionalSlotDrop;
    }

    public void detectColor() { // TODO: Mount color sensor to spindexer & confirm positioning
        update();
        if (activeSlotSensor != -1)
            spinStates.setSlot(activeSlotSensor, colorDetection.getState());
    }

    public void drop() { // assumes successful drop TODO: Add actual drop function w/ linear servo
        update();
        if (activeSlotDrop != -1)
            spinStates.setSlot(activeSlotDrop, State.None);
    }





}