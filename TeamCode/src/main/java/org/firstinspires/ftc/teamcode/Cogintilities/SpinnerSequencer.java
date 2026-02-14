package org.firstinspires.ftc.teamcode.Cogintilities;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.SubSystems.Shooter;
import org.firstinspires.ftc.teamcode.SubSystems.SpinStatesSingleton;
import org.firstinspires.ftc.teamcode.SubSystems.Spindexer;

import java.util.ArrayList;
import java.util.List;

public class SpinnerSequencer implements TeamConstants {
    public enum Mode {DROP, SCAN, NONE}
    public enum DualMode {INITIAL, RECENTER, FINAL, NONE}

    private final Spindexer spindexer;
    private final Shooter shooter;
    private final SpinStatesSingleton spinStates;
    private State[] states;
    private State[] dualModeStates;
    private int numStates;
    private int nextState = 0;
    private boolean done = true;
    private boolean wiggleActive = false;
    private boolean waitUntilNextIsActive = false;

    private int scanCycleAttempts = 0;
    private int[] scanAttempts = {0,0,0};
    private boolean[] excludedIndexesBoolean = new boolean[3];
    private int[] excludedIndexes = convertExcludedIndexes(excludedIndexesBoolean);
    private int currentScanSlot = -1;
    private int lastScanSlot = -1;

    private double shootVel = ConfigurableConstants.SHOOTER_VELOCITY_FAR;

    private Mode mode;
    private DualMode dualMode;

    private TimedTimer timer;


    public SpinnerSequencer(Spindexer spindexer, Shooter shooter, SpinStatesSingleton spinStates) {
        this.spindexer = spindexer;
        this.shooter = shooter;
        this.spinStates = spinStates;
        timer = new TimedTimer();
        mode = Mode.NONE;
        dualMode = DualMode.NONE;
    }

    private void runStatesToDropInternal(@NonNull State... states) {
        mode = Mode.DROP;
        this.states = states;
        numStates = states.length;
        nextState = 0;
        done = false;
        wiggleActive = false;
        testStatesValidity();
        testStatesArePresent();
        update(false);
    }

    public void runStatesToDrop(State... states) {
        if (states == null || states.length == 0) return;
        stop();
        runStatesToDropInternal(states);
    }

    public void runBestStatesToDrop(State... states) {
        stop();
        runBestStatesToDropInternal(states);
    }

    private void runBestStatesToDropInternal(State... states) {

        runStatesToDropInternal(toBestStatesAvailable(states));
    }

    public State[] toBestStatesAvailable(State... states) {
        List<State> stateList = new ArrayList<>();
        int purples = spinStates.getCountOfStateInStates(State.PURPLE);
        int greens = spinStates.getCountOfStateInStates(State.GREEN);
        int unknowns = spinStates.getCountOfStateInStates(State.UNKNOWN);
        for (int i = 0; i < 3; i++) {
            if (states != null) {
                if (states[i] == State.PURPLE && purples > 0) {
                    purples--;
                    stateList.add(states[i]);
                } else if (states[i] == State.GREEN && greens > 0) {
                    greens--;
                    stateList.add(states[i]);
                } else {
                    if (unknowns > 0) {
                        unknowns--;
                        stateList.add(State.UNKNOWN);
                    } else if (greens > 0) {
                        greens--;
                        stateList.add(State.GREEN);
                    } else if (purples > 0) {
                        purples--;
                        stateList.add(State.PURPLE);
                    }
                }
            } else {
                if (unknowns > 0) {
                    unknowns--;
                    stateList.add(State.UNKNOWN);
                } else if (greens > 0) {
                    greens--;
                    stateList.add(State.GREEN);
                } else if (purples > 0) {
                    purples--;
                    stateList.add(State.PURPLE);
                }
            }
        }
        return stateList.toArray(new State[0]);
    }

    public void runDual(State... states) {
        if (states == null || states.length == 0) return;
        dualModeStates = states;
        this.states = dualModeStates;
        dualMode = DualMode.INITIAL;
        done = true;
        testStatesValidity();
        update(false);

    }

    private void runScanAllInternal() { //Assumes artifacts in all slots
        mode = Mode.SCAN;
        done = false;
        wiggleActive = false;
        waitUntilNextIsActive = false;
        scanCycleAttempts = 0;
        scanAttempts = new int[]{0, 0, 0};
        excludedIndexesBoolean = new boolean[3];
        excludedIndexes = convertExcludedIndexes(excludedIndexesBoolean);
        currentScanSlot = -1;
        lastScanSlot = -1;
        update(false);
    }

    public void runScanAll() {
        stop();
        runScanAllInternal();
    }

    public void testStatesArePresent() {
        if (statesAreNotPresent(states)) stop();
    }


    public void testStatesValidity() {
        if (states == null || states.length == 0) stop();
    }

    public boolean statesAreNotPresent(@NonNull State[] states) {
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
                return true;
            }

        }
        return countPurple > spinStates.getCountOfStateInStates(State.PURPLE)
                || countGreen > spinStates.getCountOfStateInStates(State.GREEN)
                || countNone > spinStates.getCountOfStateInStates(State.NONE)
                || countUnknown > spinStates.getCountOfStateInStates(State.UNKNOWN);
    }

    public void update(boolean isValid) {
        if (done && timer.isDone()) {
            if (dualMode == DualMode.INITIAL) {
                if (statesAreNotPresent(states)) {
                    runScanAllInternal();
                } else {
                    dualMode = DualMode.RECENTER;
                    spindexer.setCentered();
//                    timer = new TimedTimer(SEQUENCER_TIMER_RECENTER);
                    timer = new TimedTimer(spindexer.getSpinnerRemainingTime()); //TODO: Test, Optimize, and, if working, implement for others
                }
            } else if (dualMode == DualMode.RECENTER) {
                dualMode = DualMode.FINAL;
                runStatesToDropInternal(dualModeStates);
            } else if (dualMode == DualMode.FINAL) {
                dualMode = DualMode.NONE;
            }
        }
        switch (mode) {
            case DROP:
                shooter.setVelocityRPM(shootVel);
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
                            if (isValid) {
                                if (nextState != 0) spindexer.stateUpdateForDrop();

                                spindexer.rotateStateToDrop(states[nextState]);
                                nextState++;

                                if (nextState == 1) timer = new TimedTimer(SEQUENCER_TIMER_INITIAL);
                                else timer = new TimedTimer(SEQUENCER_TIMER);
                            }

                        }
                    }


                }

                if (wiggleActive && timer.isDone()) {
                    spindexer.continueRotatingBy(-SEQUENCER_WIGGLE_DEGREES);
                    wiggleActive = false;
                    mode = Mode.NONE;
                    shooter.setVelocityRPM(0);
                    spindexer.setCentered();
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

                            if ((activeState == State.NONE || activeState == State.UNKNOWN) && !wiggleActive && !waitUntilNextIsActive && spinStates.isNotExcluded(currentScanSlot, excludedIndexes)) {
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
                            } else if (waitUntilNextIsActive) {
                                waitUntilNextIsActive = false;
                                if (spinStates.isStateInStates(State.NONE, excludedIndexes)) {
                                    spindexer.rotateStateToSensor(State.NONE, excludedIndexes);
                                } else {
                                    spindexer.rotateStateToSensor(State.UNKNOWN, excludedIndexes);
                                }
                                timer = new TimedTimer(SEQUENCER_TIMER);
                            } else {
                                waitUntilNextIsActive = true;
                                timer = new TimedTimer(SEQUENCER_TIMER_WAIT);
                            }
                        } else {
                            done = true;
                            mode = Mode.NONE;
                            timer = new TimedTimer(SEQUENCER_TIMER_WAIT);
                        }

                    }
                }
                if (done && wiggleActive) spindexer.rotateSlotToSensor(lastScanSlot);
                break;
//            default:
//                shooter.setVelocityRPM(0);
//            Note: Disabled because it caused issues w/ manual set, can be altered later if need be
        }


    }

    public void stop() {
        if (!done && mode == Mode.DROP) shooter.setVelocityRPM(0);
        done = true;
        mode = Mode.NONE;
        dualMode = DualMode.NONE;
    }

    public void adjustShootVel(double velocity) {
        shootVel = Double.isNaN(velocity) ? ConfigurableConstants.SHOOTER_VELOCITY_FAR : velocity;
    }

    public boolean isDone() {
        return done;
    }

    public Mode getMode() {
        return mode;
    }
    public DualMode getDualMode() {
        return dualMode;
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
