package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Cogintilities.BetterMotor;
import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Cogintilities.MotorInformation;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Shooter extends Motor1D implements TeamConstants {
//    public final BetterMotor motor;
//
//    private double minSpeedRPM;

    public Shooter(DcMotorEx motor) {
//        this.motor = new BetterMotor(motor);
        super(motor, Mode.VELOCITY_BASED, DefaultMotorInfo.GOBILDA_6000RPM);
        setVelocityPIDFCoefficients(26,0,14,12.7);
//        this.motor = motor;
//        MotorInformation.adjustMotor(this.motor, DefaultMotorInfo.GOBILDA_6000RPM);
//        this.motor.setDirection(DcMotorSimple.Direction.FORWARD);
//        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//        this.motor.setPower(0);
//        minSpeedRPM = 0;
    }

    public boolean isCloseEnough(double tolerance) {
        double vel = getVelocityRPM();
        double tarVel = getTargetVelocityRPM();
        return tarVel - tolerance <= vel && tarVel + tolerance >= vel;
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
