package org.firstinspires.ftc.teamcode.SubSystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Cogintilities.Color;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class LightRGB extends Servo1D implements TeamConstants {

    public LightRGB(Servo servo, double initPos, double min, double max) {
        super(servo, initPos, min, max, true);
        setCurrentPosition(initPos);
    }

    public void setOff() {
        setPosition(0);
    }


//    public void setColor(String color) {
//        setOn();
//        switch (color) {
//            case "Red"   : currentPosition = 0.279; break;
//            case "Orange": currentPosition = 0.333; break;
//            case "Yellow": currentPosition = 0.388; break;
//            case "Sage"  : currentPosition = 0.444; break;
//            case "Green" : currentPosition = 0.500; break;
//            case "Azure" : currentPosition = 0.555; break;
//            case "Blue"  : currentPosition = 0.611; break;
//            case "Indigo": currentPosition = 0.666; break;
//            case "Violet": currentPosition = 0.722; break;
//            case "White" : currentPosition = 1.000; break;
//        }
//        update();
//    }

    public void setColor(@NonNull Color color) {
        setPosition(color.getServoValue());
    }

    public void setCurrentPosition(double currentPosition) {
        setPosition(currentPosition);
    }

    public void setColorPosition(double colorPosition) {
        setPosition(Range.scale(colorPosition,0,1,0.279,0.722));
    }

    public void setColorState(State state) {
        Color color = Color.fromState(state);
        if (color != null) {
            setColor(color);
        } else {
            setOff();
        }
//        switch (state) {
//            case PURPLE : setColor("Violet"); break;
//            case GREEN  : setColor("Green");  break;
//            case UNKNOWN: setColor("Red");    break;
//            case NONE   : setOff();           break;
//        }
    }

//    public void update() {
//        if (isOn) setPosition(currentPosition);
//        else setPosition(0);
//    }

}