package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Intake implements TeamConstants {
    DcMotorEx motor;
    double  power;

    public Intake(DcMotorEx motor) {
        this.motor = motor;
        motor.setPower(power);
    }

    public void setMotorPower(double power) {
        motor.setPower(power);
    }

    public void setDirection() {
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public boolean getBusy(){
        return motor.isBusy();
    }
}
