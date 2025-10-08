package org.firstinspires.ftc.teamcode.Cogintilities;

import org.firstinspires.ftc.teamcode.SubSystems.SpinStatesSingleton;
import org.firstinspires.ftc.teamcode.SubSystems.Spindexer;

public class SpinnerSequencer implements TeamConstants {
    private final Spindexer spindexer;
    private State[] states;
    private int numStates;
    private int nextState = 0;
    private boolean done = false;

    TimedTimer timer;
    SpinStatesSingleton spinStates;

    public SpinnerSequencer(Spindexer spindexer, SpinStatesSingleton spinStates) {
        this.spindexer = spindexer;
        this.spinStates = spinStates;
        timer = new TimedTimer(SEQUENCER_TIMER);
    }

    public void runStates(State... states) {
        this.states = states;
        numStates = states.length;
        timer.reset();
        nextState = 0;
        done = false;
        testStates();
        update();
    }

    public void testStates() {
        for (State state : states) {
            if (!spinStates.isStateInStates(state)) {
                stop();
            }
        }
    }

    public void update() {
        if (!done) {
            spindexer.drop();
            if (timer.isDone()) {
                spindexer.rotateStateToDrop(states[nextState]);
                nextState++;
                timer.reset();
            }

            if (nextState >= numStates) {
                done = true;
            }
        }

    }

    public void stop() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }

}
