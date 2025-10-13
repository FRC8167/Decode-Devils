package org.firstinspires.ftc.teamcode.Cogintilities;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.SubSystems.SpinStatesSingleton;
import org.firstinspires.ftc.teamcode.SubSystems.Spindexer;

import java.util.ArrayList;
import java.util.List;

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
    private int[] scanAttempts = {0,0,0};
    private boolean[] excludedIndexesBoolean = new boolean[3];
    private int[] excludedIndexes = convertExcludedIndexes(excludedIndexesBoolean);
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
        scanAttempts = new int[]{0, 0, 0};
        excludedIndexesBoolean = new boolean[3];
        excludedIndexes = convertExcludedIndexes(excludedIndexesBoolean);
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
                        if (spinStates.isStateInStates(State.NONE, excludedIndexes) || spinStates.isStateInStates(State.UNKNOWN, excludedIndexes)) {
                            lastScanSlot = currentScanSlot;
                            currentScanSlot = spindexer.getActiveSlotSensor();
                            State activeState;
                            if (currentScanSlot != -1) activeState = spinStates.getSlot(currentScanSlot);
                            else activeState = null;

                            if ((activeState == State.NONE || activeState == State.UNKNOWN) && !wiggleActive && spinStates.isNotExcluded(currentScanSlot, excludedIndexes)) {
                                spindexer.detectColor();
                                State newActiveState = spinStates.getSlot(currentScanSlot);
                                if (newActiveState == State.NONE || newActiveState == State.UNKNOWN) {
                                    scanCycleAttempts ++;
                                    if (scanCycleAttempts >= SEQUENCER_SCAN_CYCLE_ATTEMPTS) {
                                        scanAttempts[currentScanSlot]++;
                                        for (int i = 0; i < scanAttempts.length; i++) {
                                            if (scanAttempts[i] >= SEQUENCER_SCAN_MAX_ATTEMPTS) {
                                                excludedIndexesBoolean[i] = true;
                                                excludedIndexes = convertExcludedIndexes(excludedIndexesBoolean);
                                            }
                                        }
                                         //TODO: Test if this works
                                        spindexer.rotateTowardsCenteredBy(150);
                                        //Note: Position is intentionally invalid (no indexes over drop and sensor) to throw error if code runs unexpectedly
                                        timer = new TimedTimer(SEQUENCER_TIMER_WIGGLE);
                                        wiggleActive = true;
                                    }
                                } else {
                                    scanCycleAttempts = 0;
                                }
                            } else if (wiggleActive) {
                                spindexer.rotateSlotToSensor(lastScanSlot);
                                timer = new TimedTimer(SEQUENCER_TIMER_WIGGLE_BACK);
                                wiggleActive = false;
                            } else {
                                if (spinStates.isStateInStates(State.NONE, excludedIndexes)) {
                                    spindexer.rotateStateToSensor(State.NONE, excludedIndexes);
                                } else {
                                    spindexer.rotateStateToSensor(State.UNKNOWN, excludedIndexes);
                                }
                                timer = new TimedTimer(SEQUENCER_TIMER);
                            }
                        } else {
                            done = true;
                            mode = null;
                        }

                    }
                }
                if (done && wiggleActive) spindexer.rotateSlotToSensor(lastScanSlot);
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

    public int[] convertExcludedIndexes(@NonNull boolean... excludedIndexesBoolean) {
        int length = excludedIndexesBoolean.length;
        List<Integer> excludedIndexesList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (excludedIndexesBoolean[i]) {
                excludedIndexesList.add(i);
            }
        }
        return excludedIndexesList.stream().mapToInt(i -> i).toArray();

    }

}
