package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;

public class Fork extends Servo1D implements TeamConstants {

    public Fork(Servo servo, double initPos, double min, double max, boolean moveOnInit) {
        super(servo, initPos, min, max, moveOnInit);
    }

    public void setPos(double pos) {
        setPosition(pos);
    }

    public double getPos() {
        return servoPos();
    }
}