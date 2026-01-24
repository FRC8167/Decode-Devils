package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Cogintilities.Color;
import org.firstinspires.ftc.teamcode.Cogintilities.DrawingUtility;
import org.firstinspires.ftc.teamcode.Cogintilities.Prism.PrismColor;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;

@TeleOp(name="TeleOpTestCar", group="Testing")
public class TeleOpTestCar extends RobotConfiguration implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(new Pose2d(0,0,0), true);

        waitForStart();
//        limeVision.takePhoto("Test");

        boolean rightBlink = false;
        boolean rightOn = false;
        boolean leftBlink  = false;
        boolean leftOn  = false;

        boolean back  = false;

        TimedTimer blinkTimer = new TimedTimer();

        while (opModeIsActive()) {
            drive.setDegradedDrive(false);
            drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            if (gamepad1.rightBumperWasPressed()) {
                if (leftBlink) leftBlink = false;
                rightBlink = !rightBlink;
                leftOn = false;
                rightOn = false;
            }
            if (gamepad1.leftBumperWasPressed()) {
                if (rightBlink) rightBlink = false;
                leftBlink = !leftBlink;
                leftOn = false;
                rightOn = false;
            }

            if (rightBlink && blinkTimer.isDone()) {
                rightOn = !rightOn;
                blinkTimer.startNewTimer(0.5);
            }

            if (leftBlink && blinkTimer.isDone()) {
                leftOn = !leftOn;
                blinkTimer.startNewTimer(0.5);
            }

            if (rightOn) {
                lightRGB_R.setColor(Color.YELLOW);
            } else {
                lightRGB_R.setOff();
            }

            if (leftOn) {
                lightRGB_L.setColor(Color.YELLOW);
            } else {
                lightRGB_L.setOff();
            }

            if (gamepad1.left_stick_y == 0 && gamepad1.left_stick_x == 0 && gamepad1.right_stick_x == 0) {
                if (!back)dataPrism.setColor(PrismColor.RED);
                back = true;
            } else {
                if (back) dataPrism.clear();
                back = false;
            }




        }
    }
}
