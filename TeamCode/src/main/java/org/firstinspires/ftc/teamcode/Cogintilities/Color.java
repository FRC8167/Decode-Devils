package org.firstinspires.ftc.teamcode.Cogintilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

public enum Color implements TeamConstants{
    RED   (0.279, "#FF0000"),
    ORANGE(0.333, "#FFA500"),
    YELLOW(0.388, "#FFA500"),
    SAGE  (0.444, "#BCB88A"),
    GREEN (0.500, "#008000"),
    AZURE (0.555, "#007FFF"),
    BLUE  (0.611, "#0000FF"),
    INDIGO(0.666, "#4B0082"),
    VIOLET(0.722, "#7F00FF"),
    WHITE (1.000, "#FFFFFF");

    private final double servoValue;
    private final String hexValue;

    Color(double servoValue, String hexValue) {
        this.servoValue = servoValue;
        this.hexValue = hexValue;
    }

    public double getServoValue() {
        return servoValue;
    }

    public String getHexValue() {
        return hexValue;
    }

    @Nullable
    @Contract(pure = true)
    public static Color fromState(@NonNull State state) {
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
