package org.firstinspires.ftc.teamcode.Cogintilities;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

public enum Color implements TeamConstants{
    RED   (0.279),
    ORANGE(0.333),
    YELLOW(0.388),
    SAGE  (0.444),
    GREEN (0.500),
    AZURE (0.555),
    BLUE  (0.611),
    INDIGO(0.666),
    VIOLET(0.722),
    WHITE (1.000);

    private final double value;

    Color(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Nullable
    @Contract(pure = true)
    public static Color fromState(State state) {
        switch (state) {
            case PURPLE:
                return VIOLET;
            case GREEN:
                return GREEN;
            case UNKNOWN:
                return RED;
            default:
                return null;
        }
    }

}
