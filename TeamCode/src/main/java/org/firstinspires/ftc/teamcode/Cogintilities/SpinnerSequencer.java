package org.firstinspires.ftc.teamcode.Cogintilities;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.SubSystems.SpinStatesSingleton;
import org.firstinspires.ftc.teamcode.SubSystems.Spindexer;

public class SpinnerSequencer implements TeamConstants {
    public enum Mode{DROP, SCAN}

    private final Spindexer spindexer;
    private final SpinStatesSingleton spinStates;
    private State[] states;
    private int numStates;
    private int nextState = 0;
    private boolean done = true;
    private boolean wiggleActive = false;

    private int scanCycleAttempts = 0;
    private int scanAttemptsSlot0 = 0;
    private int scanAttemptsSlot1 = 0;
    private int scanAttemptsSlot2 = 0;
    private int currentScanSlot = -1;
    private int lastScanSlot = -1;

    private Mode mode;

    private TimedTimer timer;


    public SpinnerSequencer(Spindexer spindexer, SpinStatesSingleton spinStates) {
        this.spindexer = spindexer;
        this.spinStates = spinStates;
        timer = new TimedTimer();
    }

    public void runStatesToDrop(@NonNull State... states) {
        mode = Mode.DROP;
        this.states = states;
        numStates = states.length;
        nextState = 0;
        done = false;
        wiggleActive = false;
        testStates();
        update();
    }

    public void scanAll() { //Assumes artifacts in all slots
        mode = Mode.SCAN;
        done = false;
        wiggleActive = false;
        scanCycleAttempts = 0;
        scanAttemptsSlot0 = 0;
        scanAttemptsSlot1 = 0;
        scanAttemptsSlot2 = 0;
        currentScanSlot = -1;
        lastScanSlot = -1;
        update();
    }

    public void testStates() {
        int countPurple = 0;
        int countGreen = 0;
        int countNone = 0;
        int countUnknown = 0;
        for (State state : states) {
            if (state == State.PURPLE) countPurple++;
            if (state == State.GREEN) countGreen++;
            if (state == State.NONE) countNone++;
            if (state == State.UNKNOWN) countUnknown++;

            if (!spinStates.isStateInStates(state)) {
                stop();
            }
            if (countPurple > spinStates.getCountOfStateInStates(State.PURPLE)
            || countGreen   > spinStates.getCountOfStateInStates(State.GREEN)
            || countNone    > spinStates.getCountOfStateInStates(State.NONE)
            || countUnknown > spinStates.getCountOfStateInStates(State.UNKNOWN)) {
                stop();
            }
        }
    }

    public void update() {
        switch (mode) {
            case DROP:
                if (!done) {
                    spindexer.dropWithoutStateUpdate();
                    if (timer.isDone()) {
                        if (nextState >= numStates) {
                            spindexer.stateUpdateForDrop();
                            done = true;
                            wiggleActive = true;
                            spindexer.continueRotatingBy(SEQUENCER_WIGGLE_DEGREES);
                            timer = new TimedTimer(SEQUENCER_TIMER_WIGGLE);


                        } else {
                            if (nextState != 0) spindexer.stateUpdateForDrop();

                            spindexer.rotateStateToDrop(states[nextState]);
                            nextState++;

                            if (nextState == 1) timer = new TimedTimer(SEQUENCER_TIMER_INITIAL);
                            else timer = new TimedTimer(SEQUENCER_TIMER);


                        }
                    }


                }

                if (wiggleActive && timer.isDone()) {
                    spindexer.continueRotatingBy(-SEQUENCER_WIGGLE_DEGREES);
                    wiggleActive = false;
                    mode = null;
                }
                break;

            case SCAN:
                if (!done) {
                    if (timer.isDone()) {
                        if (spinStates.isStateInStates(State.NONE) || spinStates.isStateInStates(State.UNKNOWN)) {
                            lastScanSlot = currentScanSlot;
                            currentScanSlot = spindexer.getActiveSlotSensor();
                            State activeState;
                            if (currentScanSlot != -1) activeState = spinStates.getSlot(currentScanSlot);
                            else activeState = null;

                            if ((activeState == State.NONE || activeState == State.UNKNOWN) && !wiggleActive) {
                                spindexer.detectColor();
                                State newActiveState = spinStates.getSlot(currentScanSlot);
                                if (newActiveState == State.NONE || newActiveState == State.UNKNOWN) {
                                    scanCycleAttempts ++;
                                    if (scanCycleAttempts >= SEQUENCER_SCAN_CYCLE_ATTEMPTS) {
                                        switch (spindexer.getActiveSlotSensor()) {
                                            case (0): scanAttemptsSlot0 ++;
                                            case (1): scanAttemptsSlot1 ++;
                                            case (2): scanAttemptsSlot2 ++;
                                        }
                                        if (scanAttemptsSlot0 >= SEQUENCER_SCAN_MAX_ATTEMPTS
                                        || scanAttemptsSlot1 >= SEQUENCER_SCAN_MAX_ATTEMPTS
                                        || scanAttemptsSlot2 >= SEQUENCER_SCAN_MAX_ATTEMPTS) {
                                            done = true; //Exits if any exceed limits
                                            mode = null;
                                        }
                                        else {
                                            spindexer.setCentered();
                                            timer = new TimedTimer(SEQUENCER_TIMER_WIGGLE);
                                            wiggleActive = true;
                                        }
                                    }
                                } else {
                                    scanCycleAttempts = 0;
                                }
                            } else if (wiggleActive) {
                                spindexer.rotateSlotToSensor(lastScanSlot);
                                timer = new TimedTimer(SEQUENCER_TIMER_WIGGLE_BACK);
                                wiggleActive = false;
                            } else {
                                if (spinStates.isStateInStates(State.NONE)) {
                                    spindexer.rotateStateToSensor(State.NONE);
                                } else {
                                    spindexer.rotateStateToSensor(State.UNKNOWN);
                                }
                                timer = new TimedTimer(SEQUENCER_TIMER);
                            }
                        } else {
                            done = true;
                            mode = null;
                        }

                    }
                }
                break;

            }

    }

    public void stop() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    public Mode getMode() {
        return mode;
    }

}
