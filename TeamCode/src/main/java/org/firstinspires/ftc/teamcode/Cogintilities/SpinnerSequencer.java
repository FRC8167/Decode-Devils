package org.firstinspires.ftc.teamcode.Cogintilities;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.SubSystems.SpinStatesSingleton;
import org.firstinspires.ftc.teamcode.SubSystems.Spindexer;

public class SpinnerSequencer implements TeamConstants {
    private final Spindexer spindexer;
    private State[] states;
    private int numStates;
    private int nextState = 0;
    private boolean done = true;
    private boolean wiggleActive = false;

    TimedTimer timer;
    SpinStatesSingleton spinStates;

    public SpinnerSequencer(Spindexer spindexer, SpinStatesSingleton spinStates) {
        this.spindexer = spindexer;
        this.spinStates = spinStates;
        timer = new TimedTimer();
    }

    public void runStates(@NonNull State... states) {
        this.states = states;
        numStates = states.length;
        nextState = 0;
        done = false;
        wiggleActive = false;
        testStates();
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
        if (!done) {
            spindexer.dropWithoutStateUpdate();
            if (timer.isDone()) {
                if (nextState >= numStates) {
                    spindexer.stateUpdateForDrop();
                    done = true;
                    wiggleActive = true;
                    spindexer.continueRotatingBy(SEQUENCER_WIGGLE_DEGREES);
                    timer = new TimedTimer(SEQUENCER_TIMER_WIGGLE);


                }
                else {
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
        }

    }

    public void stop() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }

}
