package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.Range;

public class Servo1D {

    private final Servo servo;
    private final double min;
    private final double max;


    public Servo1D(Servo servo, double initPos, double min, double max, boolean moveOnInit) {
        this.servo = servo;
        this.min = min;
        this.max = max;
        setPosition(initPos);
        if (!moveOnInit && servo instanceof ServoImplEx) {
            ((ServoImplEx) servo).setPwmDisable();
        }
    }


    public void setPosition(double position) {
        servo.setPosition(Range.clip(position, min, max));
    }


    public double servoPos() {
        return servo.getPosition();
    }

    public void disable() {
        if (servo instanceof ServoImplEx) {
            ((ServoImplEx) servo).setPwmDisable();
        }
    }

    public void enable() {
        if (servo instanceof ServoImplEx) {
            ((ServoImplEx) servo).setPwmEnable();
        }
    }

    public boolean isEnabled() {
        if (servo instanceof ServoImplEx) {
            return ((ServoImplEx) servo).isPwmEnabled();
        } else return false;
    }

//    public void setPulseRange(int customMinPulse, int customMaxPulse) {
//        if (servo instanceof ServoImplEx) {
//            ((ServoImplEx) servo).setPwmRange(new ServoImplEx.PwmRange(customMinPulse, customMaxPulse));
//        }
////        servo.scaleRange(Range.scale(customMinPulse, 500, 2500, 0, 1), Range.scale(customMaxPulse, 500, 2500, 0, 1));
//    }

}
