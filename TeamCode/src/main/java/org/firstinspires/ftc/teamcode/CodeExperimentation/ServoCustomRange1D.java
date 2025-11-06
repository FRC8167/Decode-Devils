package org.firstinspires.ftc.teamcode.CodeExperimentation;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class ServoCustomRange1D implements TeamConstants {

    Servo servo;
    double min;
    double max;


    public ServoCustomRange1D(Servo servo, ServoImplEx.PwmRange range, double initPos, double min, double max, boolean moveOnInit) {
        this.servo = servo;
        this.min = min;
        this.max = max;
        if (range != null && servo instanceof ServoImplEx) {
            ((ServoImplEx) servo).setPwmRange(range);
        }
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

    public void setPulseRange(int customMinPulse, int customMaxPulse) {
        if (servo instanceof ServoImplEx) {
            ((ServoImplEx) servo).setPwmRange(new ServoImplEx.PwmRange(customMinPulse, customMaxPulse));
        }
//        servo.scaleRange(Range.scale(customMinPulse, 500, 2500, 0, 1), Range.scale(customMaxPulse, 500, 2500, 0, 1));
    }

}
