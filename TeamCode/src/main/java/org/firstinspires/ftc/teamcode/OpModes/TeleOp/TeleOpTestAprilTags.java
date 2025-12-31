package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Cogintilities.Color;
import org.firstinspires.ftc.teamcode.Cogintilities.ConfigurableConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.Drawing;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name="TeleOpTestAprilTags", group="Competition")
public class TeleOpTestAprilTags extends RobotConfiguration implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(new Pose2d(0,0,0), true);

        Position cameraPosition = ConfigurableConstants.cameraPosition();
        YawPitchRollAngles cameraOrientation = ConfigurableConstants.cameraOrientation();

        Pose2d pose;

        waitForStart();

        while (opModeIsActive()) {

//            if (!cameraPosition.equals(ConfigurableConstants.cameraPosition()) || !cameraOrientation.equals(ConfigurableConstants.cameraOrientation())) {
//                cameraPosition = ConfigurableConstants.cameraPosition();
//                cameraOrientation = ConfigurableConstants.cameraOrientation();
//                visionPos.rebuild();
//            }

            if (gamepad1.aWasPressed()) {
                visionPos.rebuild();
            }




            visionPos.scanForAprilTags();

            AprilTagDetection tagPos = visionPos.getFirstGoalTag();
            if (tagPos != null) {
                telemetry.addData("X: ", tagPos.ftcPose.x);
                telemetry.addData("Y: ", tagPos.ftcPose.y);
                telemetry.addData("Z: ", tagPos.ftcPose.z);
                telemetry.addData("Bearing: ", tagPos.ftcPose.bearing);
                double bearing = tagPos.ftcPose.bearing;
                if (-2.5 < bearing && bearing < 2.5)
                    lightRGB.setColor(Color.AZURE);
                else
                    lightRGB.setColor(Color.RED);
                Position position = tagPos.robotPose.getPosition();
                YawPitchRollAngles orientation = tagPos.robotPose.getOrientation();
                telemetry.addData("RobotX: ", position.x);
                telemetry.addData("RobotY: ", position.y);
                telemetry.addData("RobotZ: ", position.z);
                telemetry.addData("RobotRoll: ", orientation.getRoll());
                telemetry.addData("RobotYaw: ", orientation.getYaw());
                telemetry.addData("RobotPitch: ", orientation.getPitch());
//                    telemetry.addData("Angle?: ", Math.toDegrees(Math.atan2(-65+position.y, -65+position.x));
                telemetry.addLine("");

                pose = new Pose2d(position.x, position.y, Math.toDegrees(orientation.getYaw()));
            } else
                pose = new Pose2d(0, 0, 0);
            lightRGB.setOff();

//            AprilTagDetection tagPos = visionPos.getFirstGoalTag();
            telemetry.addData("VisionPos: ", tagPos == null ? null:tagPos.id);
            telemetry.addData("VisionPosActive: ", visionPos.isAprilTagDetectionEnabled());
            telemetry.addLine("Test");
            telemetry.update();

            TelemetryPacket packet = new TelemetryPacket();
            packet.fieldOverlay().setStroke("#3F51B5");
            Drawing.drawRobot(packet.fieldOverlay(), pose);
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        }
    }
}
