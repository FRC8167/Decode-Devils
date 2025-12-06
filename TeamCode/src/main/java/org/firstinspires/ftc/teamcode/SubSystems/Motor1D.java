package org.firstinspires.ftc.teamcode.SubSystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.MotorInformation;

public class Motor1D {

    private final DcMotorEx motor;

    public enum ZeroPowerBehavior {
        BREAK,
        FLOAT;
        public DcMotorEx.ZeroPowerBehavior revert() {
            switch (this) {
                case BREAK:
                    return DcMotorEx.ZeroPowerBehavior.BRAKE;
                case FLOAT:
                    return DcMotorEx.ZeroPowerBehavior.FLOAT;
                default:
                    return null;
            }
        }
        public static ZeroPowerBehavior migrate(DcMotorEx.ZeroPowerBehavior zeroPowerBehavior) {
            switch (zeroPowerBehavior) {
                case BRAKE:
                    return BREAK;
                case FLOAT:
                    return FLOAT;
                default:
                    return null;
            }
        }

        public void applyTo(@NonNull DcMotorEx motor) {
            motor.setZeroPowerBehavior(revert());
        }
        public static ZeroPowerBehavior pullFrom(@NonNull DcMotorEx motor) {
            return migrate(motor.getZeroPowerBehavior());
        }

    }

    public enum Direction { FORWARD, REVERSE;
        public Direction inverted() {
            return this==FORWARD ? REVERSE : FORWARD;
        }
        public DcMotorSimple.Direction revert() {
            switch (this) {
                case FORWARD:
                    return DcMotorSimple.Direction.FORWARD;
                case REVERSE:
                    return DcMotorSimple.Direction.REVERSE;
                default:
                    return null;
            }
        }
        public static Direction migrate(DcMotorSimple.Direction direction) {
            switch (direction) {
                case FORWARD:
                    return FORWARD;
                case REVERSE:
                    return REVERSE;
                default:
                    return null;
            }
        }

        public void applyTo(@NonNull DcMotorEx motor) {
            motor.setDirection(revert());
        }
        public static Direction pullFrom(@NonNull DcMotorEx motor) {
            return migrate(motor.getDirection());
        }
    }

    public enum Mode {
        POWER_BASED,
        VELOCITY_BASED,
        POSITION_BASED,
        STOPPED;

        public DcMotorEx.RunMode revert() {
            switch (this) {
                case POWER_BASED:
                    return DcMotorEx.RunMode.RUN_WITHOUT_ENCODER;
                case VELOCITY_BASED:
                    return DcMotorEx.RunMode.RUN_USING_ENCODER;
                case POSITION_BASED:
                    return DcMotorEx.RunMode.RUN_TO_POSITION;
                case STOPPED:
                    return DcMotorEx.RunMode.STOP_AND_RESET_ENCODER;
                default:
                    return null;
            }
        }

        public static Mode migrate(DcMotorEx.RunMode mode) {
            switch (mode) {
                case RUN_WITHOUT_ENCODER:
                    return POWER_BASED;
                case RUN_USING_ENCODER:
                    return VELOCITY_BASED;
                case RUN_TO_POSITION:
                    return POSITION_BASED;
                case STOP_AND_RESET_ENCODER:
                    return STOPPED;
                default:
                    return null;
            }
        }

        public void applyTo(@NonNull DcMotorEx motor) {
            motor.setMode(revert());
        }

        public static Mode pullFrom(@NonNull DcMotorEx motor) {
            return migrate(motor.getMode());
        }
    }

//    private double rawPower = 0; // "Power" as used by archaic RunMode RUN_WITHOUT_ENCODER
    //Note: removing rawPower may be beneficial
//    private double fractionalSpeed = 1; // "Power" as used by archaic RunMode RUN_TO_POSITION
    private double maxVelocityRPM; //For RunMode RUN_TO_POSITION



    public Motor1D(DcMotorEx motor) {
        this.motor = motor;
        reset();
    }

    public Motor1D(DcMotorEx motor, MotorInformation motorInfo) {
        this.motor = motor;
        adjustMotorInformation(motorInfo);
        reset();
    }

    public void reset() {
        setMode(Mode.STOPPED);
        maxVelocityRPM = motor.getMotorType().getMaxRPM();
    }



    void setDirection(Direction direction) {
        direction.applyTo(motor);
    }

    public Direction getDirection() {
        return Direction.pullFrom(motor);
    }

    public double getRawPower() {
        if (motor.getMode() == DcMotorEx.RunMode.RUN_WITHOUT_ENCODER) {
            return motor.getPower();
        } else {
            return Double.NaN;
        }
    }

//    public double getFractionalSpeed() {
//        return fractionalSpeed; //Does not override raw speed or return NaN when not active to allow value to be set when not immediately in use
//    }

    public void setRawPower(double power) {
        if (motor.getMode() != DcMotorEx.RunMode.RUN_WITHOUT_ENCODER)
            motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(power);
    }

//    public void setFractionalSpeed(double speed) {
//        fractionalSpeed = speed;
//        if (motor.getMode() == DcMotorEx.RunMode.RUN_TO_POSITION) {
//            motor.setPower(fractionalSpeed);
//        }
//    }

    public void setTargetPosition(int position) {
        motor.setTargetPosition(position);
        if (motor.getMode() != DcMotorEx.RunMode.RUN_TO_POSITION)
            motor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        motor.setPower(1);
    }

    public void setTargetPosition(double position, @NonNull AngleUnit unit) {
        int ticks = degToTicks(unit.getUnnormalized().toDegrees(position));
        setTargetPosition(ticks);
    }

    public int getTargetPosition() {
        return motor.getTargetPosition();
    }

    public double getTargetPosition(@NonNull AngleUnit unit) {
        int ticks = getTargetPosition();
        return unit.getUnnormalized().fromDegrees(ticksToDeg(ticks));
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    public double getCurrentPosition(@NonNull AngleUnit unit) {
        int ticks = getCurrentPosition();
        return unit.getUnnormalized().fromDegrees(ticksToDeg(ticks));
    }

    public void setMode(Mode mode) {
        motor.setPower(0);
        motor.setVelocity(0);
        motor.setTargetPosition(motor.getCurrentPosition());
        mode.applyTo(motor);
        switch (mode) {
            case POWER_BASED:
            case VELOCITY_BASED:
                motor.setPower(0);
                break;
            case POSITION_BASED:
                motor.setPower(1);
                setVelocityRPMInternal(maxVelocityRPM);
                break;
        }

    }

    public Mode getMode() {
        return Mode.pullFrom(motor);
    }

    public void resetEncoder() {
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
    }

    public boolean isBusy() {
        return motor.isBusy();
    }

    public void enable() {
        motor.setMotorEnable();
    }

    public void disable() {
        motor.setMotorDisable();
    }

    public boolean isEnabled() {
        return motor.isMotorEnabled();
    }

    public void setVelocity(double velocity) {
        if (motor.getMode() != DcMotorEx.RunMode.RUN_USING_ENCODER)
            motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor.setVelocity(velocity);
    }


    private void setVelocityRPMInternal(double velocityRPM) {
        motor.setVelocity(RPMtoDPS(velocityRPM), AngleUnit.DEGREES);
    }

    public void setVelocityRPM(double velocityRPM) {
        if (motor.getMode() != DcMotorEx.RunMode.RUN_USING_ENCODER)
            motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        setVelocityRPMInternal(velocityRPM);
    }

    public void setMaxVelocityRPM(double velocityRPM) {
        maxVelocityRPM = velocityRPM;
        if (motor.getMode() == DcMotorEx.RunMode.RUN_TO_POSITION) {
            setVelocityRPMInternal(velocityRPM);
        }
    }

    public void setVelocityFractional(double fraction) {
        motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor.setPower(fraction);
    }

    public double getVelocity() {
        return motor.getVelocity();
    }

    public double getVelocityRPM() {
        return DPStoRPM(motor.getVelocity(AngleUnit.DEGREES));
    }

    public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
        motor.setVelocityPIDFCoefficients(p, i, d, f);
    }

    public void setVelocityPIDFCoefficients(PIDFCoefficients pidfCoefficients) {
        motor.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);
    }

    public PIDFCoefficients getVelocityPIDFCoefficients() {
        return motor.getPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public void setPositionPCoefficient(double p) {
        motor.setPositionPIDFCoefficients(p);
    }

    public double getPositionPCoefficient() {
        return motor.getPIDFCoefficients(DcMotorEx.RunMode.RUN_TO_POSITION).p;
    }

    public void setTargetPositionTolerance(int tolerance) {
        motor.setTargetPositionTolerance(tolerance);
    }

    public void setTargetPositionTolerance(double tolerance, @NonNull AngleUnit unit) {
        int ticks = degToTicks(unit.getUnnormalized().toDegrees(tolerance));
        motor.setTargetPositionTolerance(ticks);
    }

    public int getTargetPositionTolerance() {
        return motor.getTargetPositionTolerance();
    }

    public double getTargetPositionTolerance(@NonNull AngleUnit unit) {
        int ticks = motor.getTargetPositionTolerance();
        return unit.getUnnormalized().fromDegrees(ticksToDeg(ticks));
    }

    public double getCurrent(CurrentUnit unit) {
        return motor.getCurrent(unit);
    }


    public void adjustMotorInformation(MotorInformation motorInfo) {
        MotorInformation.adjustMotor(motor, motorInfo);
    }

    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        zeroPowerBehavior.applyTo(motor);
    }

    public ZeroPowerBehavior getZeroPowerBehavior() {
        return ZeroPowerBehavior.pullFrom(motor);
    }




    public double ticksToDeg(double ticks) {
        return ticks/getTicksPerDeg();
    }

    public int degToTicks(double degrees) {
        return (int) (degrees*getTicksPerDeg());
    }

    public double getTicksPerRev() {
        return motor.getMotorType().getTicksPerRev();
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
