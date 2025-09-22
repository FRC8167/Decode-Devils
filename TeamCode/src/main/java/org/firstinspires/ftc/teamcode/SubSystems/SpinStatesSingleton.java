package org.firstinspires.ftc.teamcode.SubSystems;


import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class SpinStatesSingleton implements TeamConstants {
    State slot0;
    State slot1;
    State slot2;

    private static SpinStatesSingleton single_instance = null;

    public SpinStatesSingleton() {
        slot0 = State.None;
        slot1 = State.None;
        slot2 = State.None;
    }

    public static synchronized SpinStatesSingleton getInstance()
    {
        if (single_instance == null)
            single_instance = new SpinStatesSingleton();
        return single_instance;
    }

    public void setSlot(int index, State state) {
        if (index == 0) {
            slot0 = state;
        } else if (index == 1) {
            slot1 = state;
        } else if (index == 2) {
            slot2 = state;
        } else {
            throw new IllegalArgumentException("Index must be between 0-2");
        }
    }

    public State getSlot(int index) {
        if (index == 0) {
            return slot0;
        } else if (index == 1) {
            return slot1;
        } else if (index == 2) {
            return slot2;
        } else {
            throw new IllegalArgumentException("Index must be between 0-2");
        }
    }

    public State[] getStates() {
        return new State[] {slot0, slot1, slot2};
    }

    public int getCountOfStateInStates(State state) {
        int count = 0;
        for (State s : getStates()) {
            if (s == state) count++;
        }
        return count;
    }

    public int[] getIndexesOfStateInStates(State state) {
        int[] indexes = new int[getCountOfStateInStates(state)];
        int currentIndex = 0;
        State[] states = getStates();
        for (int i = 0; i < states.length; i++) {
            if (states[i] == state) {
                indexes[currentIndex++] = i;
            }
        }
        return indexes;
    }
}
