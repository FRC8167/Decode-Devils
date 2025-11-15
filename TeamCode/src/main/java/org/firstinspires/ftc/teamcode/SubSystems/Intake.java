package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Intake implements TeamConstants {
    private final CRServo intakeL;
    private final CRServo intakeR;
    private double power;

    public Intake(CRServo intakeL, CRServo intakeR) {
        this.intakeR = intakeR;
        this.intakeL = intakeL;
        this.intakeR.setPower(0);
        this.intakeL.setPower(0);
        power = 0;
    }

    public void setMotorPower(double newPower) {
        power = newPower;
        intakeR.setPower(power);
        intakeL.setPower(power);
    }

    public double getMotorPower() {
        return power;
    }

}
