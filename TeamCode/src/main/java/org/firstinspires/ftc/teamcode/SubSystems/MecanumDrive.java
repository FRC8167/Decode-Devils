package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class MecanumDrive {

    private final DcMotorEx front_left_drive, back_left_drive, front_right_drive, back_right_drive;
    private double drive, strafe, turn;
    boolean degradedMode;
    double degradedMultiplier = 0.45;

    // Static variable to hold a single_instance of type Singleton
//    private static MecanumDrive single_instance = null;


    public MecanumDrive(DcMotorEx leftFront, DcMotorEx leftRear, DcMotorEx rightFront, DcMotorEx rightRear) {
        this.front_left_drive = leftFront;
        this.back_left_drive   = leftRear;
        this.front_right_drive = rightFront;
        this.back_right_drive  = rightRear;

        /* Assign Motor Directions */
        this.front_left_drive.setDirection(DcMotorEx.Direction.FORWARD);
        this.front_right_drive.setDirection(DcMotorEx.Direction.REVERSE);
        this.back_left_drive.setDirection(DcMotorEx.Direction.FORWARD);
        this.back_right_drive.setDirection(DcMotorEx.Direction.REVERSE);

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
