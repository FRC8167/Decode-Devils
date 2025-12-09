package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.MotorInformation;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Shooter extends Motor1D implements TeamConstants {
//    private final DcMotorEx motor;
//
//    private double minSpeedRPM;

    public Shooter(DcMotorEx motor) {
        super(motor);
        adjustMotorInformation(MotorInformation.GOBILDA_6000RPM);
        setVelocityPIDFCoefficients(
                0.05,
                0.05,
                0.00008,
                0.00045
        );
//        this.motor = motor;
//        MotorInformation.adjustMotor(motor, MotorInformation.GOBILDA_6000RPM);
//        this.motor.setDirection(DcMotorSimple.Direction.FORWARD);
//        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//        this.motor.setPower(0);
//        minSpeedRPM = 0;
    }

//    public void setMotorPower(double power) {
//        motor.setPower(power);
//    }
//
//    public double getMotorPower() {
//        return motor.getPower();
//    }
//
//    public double getMotorSpeedRPM() {
//        return motor.getVelocity()/SHOOTER_TICKS_PER_REV*60.0; //Speed in RPM
//    }
//
//    public double getMotorSpeedRpmExperimental() {
//        return motor.getVelocity(AngleUnit.DEGREES)/360.0*60.0; //Speed in RPM
//    }
//
//    public double getMotorSpeed() {
//        return motor.getVelocity()/SHOOTER_TICKS_PER_REV; //Speed in RPS
//    }
//
//    public boolean getBusy() {
//        return motor.isBusy();
//    }
//
//    public double getMinSpeedRPM() {
//        return minSpeedRPM;
//    }
//
//    public void resetMin() {
//        minSpeedRPM = getMotorSpeedRPM();
//    }
//
//    public void update() {
//        minSpeedRPM = Math.min(getMotorSpeedRPM(), minSpeedRPM);
//    }
}
