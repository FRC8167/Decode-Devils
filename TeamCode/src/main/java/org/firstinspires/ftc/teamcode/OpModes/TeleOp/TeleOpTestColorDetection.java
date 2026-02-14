package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Cogintilities.DrawingUtility;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;

@TeleOp(name="TeleOpTestColorDetection", group="Testing")
public class TeleOpTestColorDetection extends RobotConfiguration implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(null, true);

        waitForStart();
//        limeVision.takePhoto("Test");

        spindexer.setCenteredPositionDegrees(60);
        while (opModeIsActive()) {
            telemetry.addData("Color: ", colorDetection.getColor());
            telemetry.addData("H: ", colorDetection.getColorHSV()[0]);
            telemetry.addData("S: ", colorDetection.getColorHSV()[1]);
            telemetry.addData("V: ", colorDetection.getColorHSV()[2]);
            telemetry.addData("DistanceCM: ", colorDetection.getDistance(DistanceUnit.CM));
            telemetry.update();

        }
    }
}
