package org.firstinspires.ftc.teamcode.SubSystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Servo1D implements TeamConstants {

    Servo servo;
    double min;
    double max;


    public Servo1D(Servo servo, double initPos, double min, double max, boolean moveOnInit) {
        this.servo = servo;
        this.min = min;
        this.max = max;
        setPosition(initPos);
        if (!moveOnInit) {
            ((ServoImplEx) servo).setPwmDisable();
        }
    }


    public void setPosition(double position) {
        servo.setPosition(Range.clip(position, min, max));
    }


    public double servoPos() {
        return servo.getPosition();
    }

}
