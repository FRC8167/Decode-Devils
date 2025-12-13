package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Intake implements TeamConstants {
    private final CRServo intake;

    public Intake(CRServo intake) {
        this.intake = intake;
    }

    public void setPower(double power) {
        intake.setPower(power);
    }
    public double getPower() {
        return intake.getPower();
    }

}
