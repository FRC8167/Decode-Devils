package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.Other.controller.PIDFController;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class MecanumDriveBasic {

    private final DcMotorEx front_left_drive, back_left_drive, front_right_drive, back_right_drive;
    private double drive, strafe, turn;
    boolean degradedMode;
    double degradedMultiplier = 0.45;

    PIDFController headingPIDF = new PIDFController(0,0,0,0);

    // Static variable to hold a single_instance of type Singleton
//    private static MecanumDrive single_instance = null;


    public MecanumDriveBasic(DcMotorEx leftFront, DcMotorEx leftRear, DcMotorEx rightFront, DcMotorEx rightRear) {
        this.front_left_drive = leftFront;
        this.back_left_drive   = leftRear;
        this.front_right_drive = rightFront;
        this.back_right_drive  = rightRear;

        /* Assign Motor Directions */
        this.front_left_drive.setDirection(DcMotorEx.Direction.REVERSE);
        this.front_right_drive.setDirection(DcMotorEx.Direction.FORWARD);
        this.back_left_drive.setDirection(DcMotorEx.Direction.REVERSE);
        this.back_right_drive.setDirection(DcMotorEx.Direction.FORWARD);

        /* Initialize Motor Power to 0 */
        degradedMode = false;
        setMotorPower(0,0,0, 0);
        drive = strafe = turn = 0;
    }

//    public static synchronized MecanumDrive getInstance(DcMotorEx leftFront, DcMotorEx leftRear, DcMotorEx rightFront, DcMotorEx rightRear)
//    {
//        if (single_instance == null)
//            single_instance = new MecanumDrive(leftFront, leftRear, rightFront, rightRear);
//
//        return single_instance;
//    }


    /**
     * Drive robot using mecanum drive wheels. Calculates the motor power required for the given
     * inputs and reduces power if the degradedMode is true. This is intended to use joystick inputs
     * for the commands and range between 0.0 and 1.0.  There are no checks to ensure the values are in
     * range.
     * @param driveCmd      Drive command, typically gamepad.left_stick_y (negated)
     * @param strafeCmd     Strafe command, typically gamepad.Left_stick_x
     * @param turnCmd       Turn command, typically gamepad.Right_stick_s
     */
    public void mecanumDrive(double driveCmd, double strafeCmd, double turnCmd) {

        drive  = (degradedMode) ? driveCmd  * (degradedMultiplier) : driveCmd * 0.8;
        strafe = (degradedMode) ? strafeCmd * (degradedMultiplier) : strafeCmd * 0.8;
        turn   = (degradedMode) ? turnCmd   * (degradedMultiplier) : turnCmd * 0.8;

        double denominator = Math.max(Math.abs(driveCmd) + Math.abs(strafeCmd) + Math.abs(turnCmd), 1);
        double frontLeftPower  = (drive + strafe + turn) / denominator;
        double backLeftPower   = (drive - strafe + turn) / denominator;
        double frontRightPower = (drive - strafe - turn) / denominator;
        double backRightPower  = (drive + strafe - turn) / denominator;

        setMotorPower(frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }

    public void resetHeadingPIDF() {
        headingPIDF.setCoefficients(TeamConstants.mecanumPIDF);
        headingPIDF.setTolerance(0.5);
        headingPIDF.reset();
    }

    public double getTurnToHeadingErrorCommand(double error) {
        return headingPIDF.calculate(error, 0);
//        double newTurnCmd;
//        double headingFineGain   = 0.004;
//
//        /* Need Angle Wrap calculation to ensure turning the shortest distance */
//        if (Math.abs(error) > 0.5) {
//            newTurnCmd = Range.clip(headingFineGain * -error, -1.0, 1.0);
//            newTurnCmd += (newTurnCmd > 0) ? 0.075 : -0.075;
//        }
//        else newTurnCmd = 0;
//
//        mecanumDrive(0, 0, newTurnCmd);

    }


    // Drive with the specified heading in radians
    public void driveWithHeading(double driveCmd, double strafeCmd, double turnCmd, double currentHeading, double headingDeg) {
        double error, gain, newTurnCmd;
        double headingCourseGain = 0.1;
        double headingFineGain   = 0.05;

        error = headingDeg - currentHeading;
        /* Need Angle Wrap calculation to ensure turning the shortest distance */
        if(error > 10) {
            newTurnCmd = Range.clip(headingCourseGain * error, -1.0, 1.0);
        } else {
            newTurnCmd = Range.clip(headingFineGain   * error, -1.0, 1.0);
        }

        mecanumDrive(driveCmd, strafeCmd, newTurnCmd);

    }


    // This routine drives the robot field relative
    public void driveFieldRelative(double forward, double right, double rotate, double currentHeading) {
        // First, convert direction being asked to drive to polar coordinates
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(right, forward);

        // Second, rotate angle by the angle the robot is pointing
        theta = AngleUnit.normalizeRadians(theta - currentHeading);
//                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // Third, convert back to cartesian
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        // Finally, call the drive method with robot relative forward and right amounts
        mecanumDrive(newForward, newRight, rotate);
    }


    /**
     * Sets the power level of the four drive motors. Input must range between 0.0 and 1.0
     * @param lfPower Left front motor power
     * @param rfPower right front motor power
     * @param lrPower left rear motor power
     * @param rrPower right rear motor power
     */
    public void setMotorPower(double lfPower,double rfPower, double lrPower, double rrPower) { //Change to private
        front_left_drive.setPower(lfPower);
        front_right_drive.setPower(rfPower);
        back_left_drive.setPower(lrPower);
        back_right_drive.setPower(rrPower);
    }


    /**
     * Limit applied power to the motors.  Degraded power levels are set in TeamConstants
     * @param condition Drive power will be degraded when true
     */
    public void setDegradedDrive(boolean condition) {
        // 0.45 default
        degradedMode = condition;
    }


    public double getDriveCmd() { return drive;  }
    public double getTurnCmd()  { return turn;   }
    public double getStrafe()   { return strafe; }
    public double getLFpos()    { return front_left_drive.getCurrentPosition(); }
    public double geLRpos()     { return back_left_drive.getCurrentPosition(); }
    public double geRFpos()     { return front_right_drive.getCurrentPosition(); }
    public double geRRpos()     { return back_right_drive.getCurrentPosition(); }
    public double getLFpower()  { return front_left_drive.getPower(); }
    public double getLRpower()  { return back_left_drive.getPower(); }
    public double getRFpower()  { return front_right_drive.getPower(); }
    public double getRRpower()  { return back_right_drive.getPower(); }
}
