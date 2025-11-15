package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Intake implements TeamConstants {
    private final CRServo intake;

    public Intake(CRServo intake) {
        this.intake = intake;
    }

    public void setMotorPower(double power) {
        intake.setPower(power);
    }
    public double getMotorPower() {
        return intake.getPower();
    }

}
