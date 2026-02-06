package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.BetterMotor;
import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;

@TeleOp(name="TeleOpTestDrive", group="Testing")
public class TeleOpTestDrive extends RobotConfiguration implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {
        initializeRobot(null, false);

        waitForStart();
        while (opModeIsActive()) {
            drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            telemetry.addData("RF: ", drive.getRFpower());
            telemetry.addData("RR: ", drive.getRRpower());
            telemetry.addData("LF: ", drive.getLFpower());
            telemetry.addData("LR: ", drive.getLRpower());
            telemetry.update();
        }
    }
}
