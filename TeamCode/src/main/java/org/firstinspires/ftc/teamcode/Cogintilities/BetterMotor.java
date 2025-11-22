package org.firstinspires.ftc.teamcode.Cogintilities;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class BetterMotor extends DcMotorImplEx {

    private BetterMotor(@NonNull DcMotorEx motor) {
        super(motor.getController(), motor.getPortNumber(), motor.getDirection(), motor.getMotorType());
    }

    public BetterMotor(@NonNull HardwareMap hardwareMap, String motorName) {
        this(hardwareMap.get(DcMotorEx.class, motorName));
    }


    public void setVelocityRPM(double velocityRPM) {
        setVelocity(RPMtoDPS(velocityRPM), AngleUnit.DEGREES);
    }

    public double getVelocityRPM() {
        return DPStoRPM(getVelocity(AngleUnit.DEGREES));
    }

    public void setTargetPosition(double position, @NonNull AngleUnit unit, boolean rounded) {
        double pos = getTicksPerDeg() * unit.toDegrees(position);
        super.setTargetPosition(rounded ? (int) Math.round(pos) : (int) pos);
    }

    public void setTargetPosition(double position, AngleUnit unit) {
        setTargetPosition(position, unit, true);
    }

    public double getCurrentPosition(@NonNull AngleUnit unit) {
        int ticks = super.getCurrentPosition();
        return unit.fromDegrees((double) ticks/getTicksPerDeg());
    }

    public void adjustMotorInformation(MotorInformation motorConfiguration) {
        MotorInformation.adjustMotor(this, motorConfiguration);
    }



    public double getTicksPerRev() {
        return motorType.getTicksPerRev();
    }

    public double getTicksPerDeg() {
        return getTicksPerRev() / 360.0;
    }

    public double DPStoRPM(double dps) {
        return dps/6.0;
    }

    public double RPMtoDPS(double rpm) {
        return rpm*6.0;
    }


}
