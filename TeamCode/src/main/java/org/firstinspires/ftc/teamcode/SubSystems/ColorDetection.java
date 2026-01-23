package org.firstinspires.ftc.teamcode.SubSystems;

import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class ColorDetection implements TeamConstants {
    private final RevColorSensorV3 colorSensor;
    private final float[] colorHSV;
    private float hue, saturation, value;

    public ColorDetection(RevColorSensorV3 colorSensor) {
        this.colorSensor = colorSensor;
        colorHSV = new float[3];
        update();
    }

    public void update() {
        int colorColor = colorSensor.getNormalizedColors().toColor();
        Color.colorToHSV(colorColor, colorHSV);
        hue = colorHSV[0];
        saturation = colorHSV[1];
        value = colorHSV[2];

    }

    public String getColor() {
        update();
        if ((saturation == 0 && hue == 0) || (saturation == 1 && (hue == 120 || hue == 60)) || getDistance(DistanceUnit.CM) > 3) // air is 120 and value !=0 for some reason
            return "Error";
        else if (saturation < 0.4)
            return "Invalid Color";

        else if (70 <= hue && hue <= 165 && hue != 120) {
            return "Green";
        } else if (220 <= hue && hue <= 315) {
            return "Purple";
        } else {
            return "Other";
        }
    }

    public State getState() {
        switch (getColor()) {
            case "Error": return State.NONE;
            case "Green": return State.GREEN;
            case "Purple": return State.PURPLE;
            default: return State.UNKNOWN;
        }
    }

    public double getDistance(DistanceUnit distanceUnit) {
        return colorSensor.getDistance(distanceUnit);
    }

    public float[] getColorHSV() {
        return colorHSV;
    }

}
