package org.firstinspires.ftc.teamcode.SubSystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Cogintilities.GearedMotorInfo;
import org.firstinspires.ftc.teamcode.Cogintilities.MotorInformation;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Lift extends Motor1D implements TeamConstants {

    private final double minInches;
    private final double maxInches;

    public Lift(DcMotorEx motor, double minInches, double maxInches) {
        super(motor, Mode.POSITION_BASED, LIFT_MOTOR_INFO);
        setZeroPowerBehavior(ZeroPowerBehavior.BREAK);
        this.minInches = minInches;
        this.maxInches = maxInches;
    }

    public void setPositionInches(double inches) {
        setTargetPosition(
            Range.clip(
                linearInToDeg(inches),
                    minInches,
                    maxInches
            ),
            AngleUnit.DEGREES
        );
    }

    public double getTargetPositionInches() {
        return degToLinearIn(getTargetPosition(AngleUnit.DEGREES));
    }

    public double getCurrentPositionInches() {
        return degToLinearIn(getCurrentPosition(AngleUnit.DEGREES));
    }

    public double degToLinearIn(double degrees) {
        double revolutions, millis, inches;
        revolutions = degrees / 360.0;
        millis = revolutions * LIFT_MILLIS_PER_REV;
        inches = millis / 25.4;
        return inches;
    }

    public double linearInToDeg(double inches) {
        double millis, revolutions, degrees;
        millis = inches * 25.4;
        revolutions = millis / LIFT_MILLIS_PER_REV;
        degrees = revolutions * 360.0;
        return degrees;
    }


}
