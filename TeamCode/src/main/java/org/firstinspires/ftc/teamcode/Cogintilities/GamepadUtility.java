package org.firstinspires.ftc.teamcode.Cogintilities;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;

public final class GamepadUtility {

    private GamepadUtility() {}

    public static void setGamepadLED(Color color, Gamepad... gamepads) {
        if (color == null) color = Color.BLACK;
        Color.RgbValues rgbValues = color.getRgbValues();

        for (Gamepad gamepad: gamepads) {
            gamepad.setLedColor(rgbValues.r, rgbValues.g, rgbValues.b, Gamepad.LED_DURATION_CONTINUOUS);
        }
    }

    public static void setGamepadLED(RobotConfiguration.AllianceColor allianceColor, Gamepad... gamepads) {
        Color color = allianceColor != null ? allianceColor.getColor() : null;
        setGamepadLED(color, gamepads);

    }

}
