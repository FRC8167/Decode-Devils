package org.firstinspires.ftc.teamcode.SubSystems;

import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class ColorDetection implements TeamConstants {
    RevColorSensorV3 colorSensor;
    int colorColor;
    float[] colorHSV;
    float hue, saturation, value;

    public ColorDetection(RevColorSensorV3 colorSensor) {
        this.colorSensor = colorSensor;
        colorHSV = new float[3];
        update();
    }

    public void update() {
        colorColor = colorSensor.getNormalizedColors().toColor();
        Color.colorToHSV(colorColor, colorHSV);
        hue = colorHSV[0];
        saturation = colorHSV[1];
        value = colorHSV[2];

    }

    public String getColor() {
        update();
        if (saturation < 0.8){
            if (saturation == 0 && hue == 0 && value == 0)
                return "Error";
            else
                return "Invalid Color";
        }
        else if (70 < hue && hue < 160){
            return "Green";
        } else if (220 < hue && hue < 300) {
            return "Purple";
        }
        else {
            return "Other";
        }
    }

    public State getState() {
        switch (getColor()) {
            case "Error": return State.None;
            case "Green": return State.Green;
            case "Purple": return State.Purple;
            default: return State.Unknown;
        }
    }

    public float[] getColorHSV() {
        return colorHSV;
    }

}
