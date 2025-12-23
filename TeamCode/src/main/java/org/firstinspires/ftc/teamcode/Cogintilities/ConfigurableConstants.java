package org.firstinspires.ftc.teamcode.Cogintilities;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Config
public class ConfigurableConstants {
    private ConfigurableConstants() {}

//    public static double KP = 26;
//    public static double KI = 0;
//    public static double KD = 14;
//    public static double FF = 12.7;
//    public static double VEL_LOW_RPM = 1000;
//    public static double VEL_HIGH_RPM = 5000;
//    public static double ALTERNATION_PERIOD = 10;
    public static double SHOOTER_VELOCITY_CLOSE = 3600;
    public static double SHOOTER_VELOCITY_FAR = 4300;
    public static double SHOOTER_VELOCITY_AUTO_FAR = 4300;
    public static double SHOOTER_VELOCITY_AUTO_CLOSE = 3500;

    public static double CAMERA_X = 0;
    public static double CAMERA_Y = 0;
    public static double CAMERA_Z = 0;

    public static double CAMERA_YAW = 0;
    public static double CAMERA_PITCH = 0;
    public static double CAMERA_ROLL = 0;


    public static Position cameraPosition() {
        return new Position(DistanceUnit.INCH, CAMERA_X, CAMERA_Y, CAMERA_Z, 0);
    }

    public static YawPitchRollAngles cameraOrientation() {
        return new YawPitchRollAngles(AngleUnit.DEGREES, CAMERA_YAW, CAMERA_PITCH, CAMERA_ROLL, 0);
    }
}
