package org.firstinspires.ftc.teamcode.SubSystems;

import android.graphics.Color;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class DistanceDetection implements TeamConstants {
    private final Rev2mDistanceSensor distanceSensor;

    public DistanceDetection(Rev2mDistanceSensor distanceSensor) {
        this.distanceSensor = distanceSensor;
    }

    public double getDistance(DistanceUnit distanceUnit) {
        return distanceSensor.getDistance(distanceUnit);
    }

    public double getInches() {
        return distanceSensor.getDistance(DistanceUnit.INCH);
    }

    public boolean isClear() {
        return getInches() >= DISTANCE_SENSOR_CLEAR_SPACE_NEEDED;
    }

    public double getRobotObjectDistance() {
        return getInches() + DISTANCE_SENSOR_FWD_OFFSET; //Gets Distance from robot center of rotation
    }

}
