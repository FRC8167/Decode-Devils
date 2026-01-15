package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Cogintilities.DrawingUtility;
import org.firstinspires.ftc.teamcode.Cogintilities.PoseMath;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.Drawing;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;
import org.firstinspires.ftc.teamcode.SubSystems.LimeVision;

@TeleOp(name="TeleOpTestLimeLight", group="Competition")
public class TeleOpTestLimelight extends RobotConfiguration implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(new Pose2d(0,0,0), true);

        Pose3D poseMT1 = null;
        Pose3D poseMediated = null;

        DrawingUtility drawingUtility = new DrawingUtility();

        waitForStart();
//        limeVision.takePhoto("Test");

        while (opModeIsActive()) {
            drive.setDegradedDrive(false);
            drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            if (limeVision != null) {
                poseMT1 = limeVision.getRobotPose3D();
                poseMediated = limeVision.getMediatiatedRobotPose3D();
            }

            if (poseMT1 != null) {
//                telemetry.addLine("MT1:");
//                telemetry.addLine("Position:");
//                Position position = poseMT1.getPosition().toUnit(DistanceUnit.INCH);
//                telemetry.addData("X: ", position.x);
//                telemetry.addData("Y: ", position.y);
//                telemetry.addData("Z: ", position.z);
//                telemetry.addLine("Orientation:");
//                YawPitchRollAngles orientation = poseMT1.getOrientation();
//                telemetry.addData("Yaw: ", orientation.getYaw());
//                telemetry.addData("Pitch: ", orientation.getPitch());
//                telemetry.addData("Roll: ", orientation.getRoll());
//                telemetry.addLine();
//                telemetry.addData("Calculated Bearing: ", limeVision.getGoalBearing());
//                telemetry.addLine();
//                telemetry.addLine();
//                TelemetryPacket packet = new TelemetryPacket();
//                packet.fieldOverlay().setStroke("#3F51B5");
//                Drawing.drawRobot(packet.fieldOverlay(), PoseMath.Pose3DtoPose2d(poseMT1));
                drawingUtility.drawRobot("#3F51B5", poseMT1);

            }

            if (poseMediated != null) {
                telemetry.addLine("Mediated:");
                telemetry.addLine("Position:");
                Position positionMediated = poseMediated.getPosition().toUnit(DistanceUnit.INCH);
                telemetry.addData("X: ", positionMediated.x);
                telemetry.addData("Y: ", positionMediated.y);
                telemetry.addData("Z: ", positionMediated.z);
                telemetry.addLine("Orientation:");
                YawPitchRollAngles orientationMediated = poseMediated.getOrientation();
                telemetry.addData("Yaw: ", orientationMediated.getYaw());
                telemetry.addData("Pitch: ", orientationMediated.getPitch());
                telemetry.addData("Roll: ", orientationMediated.getRoll());
                telemetry.addLine();
                telemetry.addData("Calculated Bearing: ", limeVision.getMediatedGoalBearing());
                telemetry.addData("# of poses: ", limeVision.getPreviousPosesSize());
//                packet.fieldOverlay().setStroke("#3FB551");
//                Drawing.drawRobot(packet.fieldOverlay(), PoseMath.Pose3DtoPose2d(poseMediated));
                drawingUtility.drawRobot("#3FB551", poseMediated);
            }

            drawingUtility.send();

            telemetry.addLine("");
            telemetry.update();


        }
    }
}
