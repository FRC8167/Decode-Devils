package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Cogintilities.MotorConfigurations;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Shooter implements TeamConstants {
    private final DcMotorEx motor;

    private double minSpeedRPM;

    public Shooter(DcMotorEx motor) {
        this.motor = motor;
        MotorConfigurations.configureMotor(motor, MotorConfigurations.GOBILDA_6000RPM);
        this.motor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.motor.setPower(0);
        minSpeedRPM = 0;
    }

    public void setMotorPower(double power) {
        motor.setPower(power);
    }

    public double getMotorPower() {
        return motor.getPower();
    }

    public double getMotorSpeedRPM() {
        return motor.getVelocity()/SHOOTER_TICKS_PER_REV*60.0; //Speed in RPM
    }

    public double getMotorSpeed() {
        return motor.getVelocity()/SHOOTER_TICKS_PER_REV; //Speed in RPS
    }

    public boolean getBusy() {
        return motor.isBusy();
    }

    public double getMinSpeedRPM() {
        return minSpeedRPM;
    }

    public void resetMin() {
        minSpeedRPM = getMotorSpeedRPM();
    }

    public void update() {
        minSpeedRPM = Math.min(getMotorSpeedRPM(), minSpeedRPM);
    }
}
