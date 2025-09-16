package org.firstinspires.ftc.teamcode.SubSystems;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class ColorDetection implements TeamConstants {
    NormalizedColorSensor colorSensor;
    int colorColor;
    float[] colorHSV;
    float hue, saturation, value;

    public ColorDetection(NormalizedColorSensor colorSensor) {
        this.colorSensor = colorSensor;
        colorHSV = new float[3];
        update();
    }

    public void update(){
        colorColor = colorSensor.getNormalizedColors().toColor();
        Color.colorToHSV(colorColor, colorHSV);
        hue = colorHSV[0];
        saturation = colorHSV[1];
        value = colorHSV[2];

    }

    public String getColor(){
        if (saturation < 30 || value < 30){
            return "Invalid Color";
        }
        else if (70 < hue && hue < 160){
            return "Green";
        } else if (260 < hue && hue <300) {
            return "Purple";
        }
        else {
            return "Other";
        }
    }

    public float[] getColorHSV(){
        return colorHSV;
    }



}
