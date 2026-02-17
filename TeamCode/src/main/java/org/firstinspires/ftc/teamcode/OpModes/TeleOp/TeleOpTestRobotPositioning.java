package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Cogintilities.Color;
import org.firstinspires.ftc.teamcode.Cogintilities.DrawingUtility;
import org.firstinspires.ftc.teamcode.Cogintilities.PoseMath;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;

@TeleOp(name="TeleOpTestRobotPositioning", group="Testing")
public class TeleOpTestRobotPositioning extends RobotConfiguration implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(null, true);

        telemetry.addData("AutoPos: ", autoDrive.localizer.getPose());
        telemetry.update();

//        Pose3D poseMT1 = null;
        Pose3D pose;

        DrawingUtility drawingUtility = new DrawingUtility();

        waitForStart();
//        limeVision.takePhoto("Test");

        while (opModeIsActive()) {
            drive.setDegradedDrive(false);
            drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            Pose2d compositePos2d = compositePositioning.getCompositePos2d();
            Pose2d limePos = compositePositioning.getLimePos2d();
            Pose2d drivePos = compositePositioning.getAutoDrivePos2d();
            pose = PoseMath.pose2dtoPose3D(compositePos2d);

            if (compositePositioning.checkOverallReadiness()) {
                lightRGB_M.setColor(Color.AZURE);
            } else lightRGB_M.setOff();

            if (compositePositioning.checkRobotLaunchZoneFarOverlap()) {
                lightRGB_R.setColor(Color.GREEN);
            } else lightRGB_R.setOff();

            if (compositePositioning.checkRobotLaunchZoneCloseOverlap()) {
                lightRGB_L.setColor(Color.GREEN);
            } else lightRGB_L.setOff();

            telemetry.addData("Is in launch zone: ", compositePositioning.checkRobotLaunchZoneOverlap());
            telemetry.addData("Is in close launch zone: ", compositePositioning.checkRobotLaunchZoneCloseOverlap());
            telemetry.addData("Is in far launch zone: ", compositePositioning.checkRobotLaunchZoneFarOverlap());
            telemetry.addLine();

            if (pose != null) {
                telemetry.addData("CompositePos: ", pose.toString());
//                telemetry.addLine("Position:");
//                Position positionMediated = pose.getPosition().toUnit(DistanceUnit.INCH);
//                telemetry.addData("X: ", positionMediated.x);
//                telemetry.addData("Y: ", positionMediated.y);
//                telemetry.addData("Z: ", positionMediated.z);
//                telemetry.addLine("Orientation:");
//                YawPitchRollAngles orientationMediated = pose.getOrientation();
//                telemetry.addData("Yaw: ", orientationMediated.getYaw());
//                telemetry.addData("Pitch: ", orientationMediated.getPitch());
//                telemetry.addData("Roll: ", orientationMediated.getRoll());
//                telemetry.addLine();
//                telemetry.addData("Calculated Bearing: ", compositePositioning.getAllianceGoalBearing());
//                telemetry.addData("# of poses: ", limeVision.getPreviousPosesSize());
//                packet.fieldOverlay().setStroke("#3FB551");
//                Drawing.drawRobot(packet.fieldOverlay(), PoseMath.pose3DtoPose2d(pose));
                drawingUtility.drawRobot("#FF5555", pose);
            }

            if (limePos != null) {
                telemetry.addData("LimePos: ", limePos.toString());
                drawingUtility.drawRobot("#55FF55", limePos);
            }
            if (drivePos != null) {
                telemetry.addData("DrivePos: ", drivePos.toString());
                drawingUtility.drawRobot("#5555FF", drivePos);
            }

            PoseVelocity2d poseVelocity2d = compositePositioning.getPoseVel();

            if (poseVelocity2d != null) {
                telemetry.addLine();
                telemetry.addData("XVel: ", poseVelocity2d.linearVel.x);
                telemetry.addData("YVel: ", poseVelocity2d.linearVel.y);
                telemetry.addData("AVel: ", poseVelocity2d.angVel);
            }

            drawingUtility.send();

            telemetry.addLine();

            State[] states = limeVision.getFirstSequence();
            telemetry.addData("States: ", State.convertStatesToInitials(states));

            telemetry.addLine();

            telemetry.addData("Distance: ", distanceDetection.getInches());
            telemetry.addData("IsClear: ", distanceDetection.isClear());

            telemetry.update();

            compositePositioning.update(null);


        }
    }
}
