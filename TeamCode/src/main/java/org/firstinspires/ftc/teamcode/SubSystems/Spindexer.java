package org.firstinspires.ftc.teamcode.SubSystems;



import org.firstinspires.ftc.teamcode.Cogintilities.EricsEgregiousException;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;

import java.util.Arrays;

public class Spindexer implements TeamConstants {

    int activeSlotDrop; //
    int activeSlotSensor;// assumes color sensor is same as drop
    int activeSlotOldSensor;// assumes color sensor is opposite to drop
    double fractionalSlotDrop;
    double fractionalSlotSensor;
    double currentAngleNormalized; //(0-360)
    boolean isOpen;
    Spinner spinner;
    Dropper dropper;
    TimedTimer dropTimer;
    SpinStatesSingleton spinStates;
    ColorDetection colorDetection;


    public Spindexer(Spinner spinner, Dropper dropper, SpinStatesSingleton spinStates, ColorDetection colorDetection) {
        this.spinner = spinner;
        this.dropper = dropper;
        this.spinStates = spinStates;
        this.colorDetection = colorDetection;
        activeSlotDrop = -1;
        activeSlotSensor = -1;
        fractionalSlotDrop = -1;
        fractionalSlotSensor = -1;
        currentAngleNormalized = 0;
        isOpen = false;
        dropTimer = new TimedTimer();
        update();
    }

    public void setCenteredPositionDegrees(double degrees) {
        spinner.setCenteredPositionDegrees(degrees);
        update();
    }

    public void setCentered() {
        spinner.setCenteredPositionDegrees(0);
        update();
    }

    public double getCenteredPositionDegrees() {
        return spinner.getCenteredPositionDegrees();
    }

    public void rotateBy(double degrees) {
        spinner.rotateBy(degrees);
        update();
    }

    public void continueRotatingBy(double degrees) {
        spinner.continueRotatingBy(degrees);
        update();
    }

    public void rotateTowardsCenteredBy(double degrees) {
        double currentAngle = getCenteredPositionDegrees();
        if (currentAngle > 0)
            rotateBy(-degrees);
        else if (currentAngle < 0)
            rotateBy(degrees);
        else continueRotatingBy(degrees);
        update();
    }

    public double getPosition() {
        return spinner.servoPos();
    }
//
//    public void setDropperPosition(double pos) {
//        dropper.setPosition(pos);
//    }

    public void rotateSlotToDrop(int slot) {
        update();
        double targetAngle;
        switch (slot) {
            case 0:
                targetAngle = 300;
                break;
            case 1:
                targetAngle = 180;
                break;
            case 2:
                targetAngle = 60;
                break;
            default:
                targetAngle = spinner.getCenteredPositionDegrees(); // No change if invalid slot
        }
        // Calculate the shortest path to the target angle
        double delta = ((targetAngle - currentAngleNormalized + 540) % 360) - 180; // Calculate shortest path (-180 to 180)
        if (currentAngleNormalized + delta > SPINNER_RANGE / 2)
            delta -= 360;
        else if (currentAngleNormalized + delta < -SPINNER_RANGE / 2)
            delta += 360;
        rotateBy(delta);
        update();
    }


    public void rotateSlotToSensor(int slot) {
        rotateSlotToDrop(slot);
//        update();
//        double targetAngle;
//        switch (slot) {
//            case 0:
//                targetAngle = 120;
//                break;
//            case 1:
//                targetAngle = 0;
//                break;
//            case 2:
//                targetAngle = 240;
//                break;
//            default:
//                targetAngle = spinner.getCenteredPositionDegrees(); // No change if invalid slot
//        }
//        // Calculate the shortest path to the target angle
//        double delta = ((targetAngle - currentAngleNormalized + 540) % 360) - 180; // Calculate shortest path (-180 to 180)
//        if (currentAngleNormalized + delta > SPINNER_RANGE / 2)
//            delta -= 360;
//        else if (currentAngleNormalized + delta < -SPINNER_RANGE / 2)
//            delta += 360;
//        rotateBy(delta);
//        update();
    }

    public void rotateStateToDrop(State state, int... excludedIndexes) {
        update();
        int[] indexes = spinStates.getIndexesOfStateInStates(state, excludedIndexes);
        double[] distances = new double[indexes.length];
        boolean found = false;
        boolean secondFound = false;
        int foundIndex = -1;
        int secondFoundIndex = -1;
        int setIndex;
        double rotation;
        if (indexes.length > 0) {
            if (indexes.length == 1) {
                rotateSlotToDrop(indexes[0]);
            } else {
                for (int i = 0; i < indexes.length; i++) {
                    double targetSlotValue = indexes[i]; // The actual slot number (0, 1, or 2)

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
//                            rotation = 120;
                            setIndex = Math.floorMod(activeSlotDrop-1, 3);
                        } else {
//                            rotation = 60;
                            setIndex = (int) fractionalSlotDrop;
                        }
                    } else if (spinner.getPreviousRotation() < 0) {
                        if (activeSlotDrop != -1) {
//                            rotation = -120;
                            setIndex = Math.floorMod(activeSlotDrop+1, 3);
                        } else {
//                            rotation = -60;
                            setIndex = Math.floorMod((int) fractionalSlotDrop+1, 3);
                        }
                    } else {
                        throw new EricsEgregiousException("IDK, something went horribly wrong. Pls Fix");
                    }
//                    rotateBy(rotation);
                    rotateSlotToDrop(setIndex);
                    if (activeSlotDrop != foundIndex && activeSlotDrop != secondFoundIndex) {
                        throw new EricsEgregiousException("IDK, something went horribly wrong. Pls Fix");
                    }
                } else if (found) {
                    rotateSlotToDrop(foundIndex);
                } else {
                    throw new EricsEgregiousException("IDK, something went horribly wrong. Pls Fix");
                }

            }
        }

    }

    public void rotateStateToSensor(State state, int... excludedIndexes) { //Note: Optimized for centered (closest to normalized 0) positioning instead of continuous movement
        update();
        int[] indexes = spinStates.getIndexesOfStateInStates(state, excludedIndexes);
        double[] distances = new double[indexes.length];
        boolean found = false;
        boolean secondFound = false;
        int foundIndex = -1;
        int secondFoundIndex = -1;
        int setIndex;
        double rotation;
        if (indexes.length > 0) {
            if (indexes.length == 1) {
                rotateSlotToSensor(indexes[0]);
            } else {
                for (int i = 0; i < indexes.length; i++) {
                    double targetSlotValue = indexes[i]; // The actual slot number (0, 1, or 2)

                    double forwardSlotDist = (targetSlotValue - fractionalSlotSensor + 3.0) % 3.0;
                    // Ensure forwardSlotDist is always in [0, 3)
                    if (forwardSlotDist < 0) forwardSlotDist += 3.0;


                    double backwardSlotDist = (fractionalSlotSensor - targetSlotValue + 3.0) % 3.0;
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
                    if (currentAngleNormalized <= 0) {
                        if (activeSlotSensor != -1) {
//                            rotation = 120;
                            setIndex = Math.floorMod(activeSlotSensor-1, 3);
                        } else {
//                            rotation = 60;
                            setIndex = (int) fractionalSlotSensor;
                        }
                    } else if (currentAngleNormalized > 0) {
                        if (activeSlotSensor != -1) {
//                            rotation = -120;
                            setIndex = Math.floorMod(activeSlotSensor+1, 3);
                        } else {
//                            rotation = -60;
                            setIndex = Math.floorMod((int) fractionalSlotSensor+1, 3);
                        }
                    } else {
                        throw new EricsEgregiousException("currentAngleNormalized Not Valid: " + currentAngleNormalized);
                    }
//                    rotateBy(rotation);
                    rotateSlotToSensor(setIndex);
                    if (activeSlotSensor != foundIndex && activeSlotSensor != secondFoundIndex) {
                        throw new EricsEgregiousException("Moved to incorrect positon (Not one of the Founds)");
                    }
                } else if (found) {
                    rotateSlotToSensor(foundIndex);
                } else {
                    throw new EricsEgregiousException("None found (shouldn't be possible)");
                }

            }
        }

    }

    public void disableSpinner() {
        spinner.disable();
    }

    public void enableSpinner() {
        spinner.enable();
    }

    public boolean isSpinnerEnabled() {
        return spinner.isEnabled();
    }

    public void update() {
        currentAngleNormalized = ((spinner.getCenteredPositionDegrees() % 360 + 360) % 360);
        switch ((int) currentAngleNormalized) {
            case 300:
                activeSlotDrop = 0;
                fractionalSlotDrop = 0;
                fractionalSlotSensor = 1.5;
                break;
            case 180:
                activeSlotDrop = 1;
                fractionalSlotDrop = 1;
                fractionalSlotSensor = 2.5;
                break;
            case 60:
                activeSlotDrop = 2;
                fractionalSlotDrop = 2;
                fractionalSlotSensor = 0.5;
                break;
            default:
                activeSlotDrop = -1;
                break;
        }
        switch ((int) currentAngleNormalized) {
            case 120:
                activeSlotSensor = 0;
                fractionalSlotSensor = 0;
                fractionalSlotDrop = 1.5;
                break;
            case 0:
                activeSlotSensor = 1;
                fractionalSlotSensor = 1;
                fractionalSlotDrop = 2.5;
                break;
            case 240:
                activeSlotSensor = 2;
                fractionalSlotSensor = 2;
                fractionalSlotDrop = 0.5;
                break;
            default:
                activeSlotSensor = -1;
                break;

        }
        activeSlotOldSensor = activeSlotSensor;
        activeSlotSensor = activeSlotDrop;
        fractionalSlotSensor = fractionalSlotDrop;

        if (dropTimer.isDone()) {
            isOpen = false;
            dropper.close();
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

    public int getActiveSlotOldSensor() {
        update();
        return activeSlotOldSensor;
    }

    public double getFractionalSlotDrop() {
        update();
        return fractionalSlotDrop;
    }

    public void detectColor() { // TODO: Mount color sensor to spindexer & confirm positioning
        update();
        //Note: Current Sensor mounting places it at the same position as drop and not at "sensor"
        if (activeSlotSensor != -1 && spinner.isDone()) {
            State state = spinStates.getSlot(activeSlotSensor);
            State newState = colorDetection.getState();
            if ((state == State.NONE || state == State.UNKNOWN) || (newState == State.GREEN || newState == State.PURPLE)) {
                spinStates.setSlot(activeSlotSensor, newState);
            }
        }

        //Note: Old Sensor mounting placed it at "sensor" but was later changed
//        if (activeSlotSensor != -1) {
//            State state = spinStates.getSlot(activeSlotSensor);
//            State newState = colorDetection.getState();
//            if ((state == State.NONE || state == State.UNKNOWN) || (newState == State.GREEN || newState == State.PURPLE)) {
//                spinStates.setSlot(activeSlotSensor, newState);
//            }
//        }
    }

    public void dropTimed() { // assumes successful drop
        update();
        if (activeSlotDrop != -1) {
            spinStates.setSlot(activeSlotDrop, State.NONE);
            isOpen = true;
            dropper.open();
            dropTimer = new TimedTimer(DROP_TIMER); // how long until door closes
        }
    }

    public void drop() { // assumes successful drop
        update();
        if (activeSlotDrop != -1) {
            spinStates.setSlot(activeSlotDrop, State.NONE);
        }
        isOpen = true;
        dropper.open();
        dropTimer = new TimedTimer(0.1);
    }

    public void dropWithoutStateUpdate() {
        update();
        if (activeSlotDrop != -1) {
            isOpen = true;
            dropper.open();
            dropTimer = new TimedTimer(0.1);
        }
    }

    public void stateUpdateForDrop() {
        if (activeSlotDrop != -1) spinStates.setSlot(activeSlotDrop, State.NONE);
    }

    public void setWiggleOffset(double degrees) {
       spinner.setWiggleOffset(degrees);
    }

    public double getDropTimerRemainingTime() {
        return dropTimer.getRemainingTime();
    }

    public boolean isSpinnerDone() {
        return spinner.isDone();
    }

    public double getSpinnerRemainingTime() {
        return spinner.getRemainingTime();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public double getDropperPos() {
        return dropper.servoPos();
    }



    public void periodic() { // must be called during TeleOp for timer updates to function
        update();
    }

}