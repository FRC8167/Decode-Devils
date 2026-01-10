package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Cogintilities.PoseConverter;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.Drawing;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;

@TeleOp(name="TeleOpTestLimeLight", group="Competition")
public class TeleOpTestLimelight extends RobotConfiguration implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(new Pose2d(0,0,0), true);

        Pose3D poseMT1 = null;
        Pose3D poseMT2 = null;

        waitForStart();
        limeVision.takePhoto("Test");

        while (opModeIsActive()) {
            drive.setDegradedDrive(false);
            drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            if (limeVision != null) {
                poseMT1 = limeVision.getMT1Pos();
                poseMT2 = limeVision.getMT2Pos();
            }

            if (poseMT1 != null || poseMT2 != null) {

                TelemetryPacket packet = new TelemetryPacket();

                if (poseMT1 != null) {
                    telemetry.addLine("MT1:");
                    telemetry.addLine("Position:");
                    Position position = poseMT1.getPosition();
                    telemetry.addData("X: ", position.x);
                    telemetry.addData("Y: ", position.y);
                    telemetry.addData("Z: ", position.z);
                    telemetry.addLine("Orientation:");
                    YawPitchRollAngles orientation = poseMT1.getOrientation();
                    telemetry.addData("Yaw: ", orientation.getYaw());
                    telemetry.addData("Pitch: ", orientation.getPitch());
                    telemetry.addData("Roll: ", orientation.getRoll());

                    packet.fieldOverlay().setStroke("#3F51B5");
                    Drawing.drawRobot(packet.fieldOverlay(), PoseConverter.Pose3DtoPose2d(poseMT1));
                }

                if (poseMT1 != null && poseMT2 != null) {
                    telemetry.addLine("");
                }

                if (poseMT2 != null) {
                    telemetry.addLine("MT1:");
                    telemetry.addLine("Position:");
                    Position position = poseMT2.getPosition();
                    telemetry.addData("X: ", position.x);
                    telemetry.addData("Y: ", position.y);
                    telemetry.addData("Z: ", position.z);
                    telemetry.addLine("Orientation:");
                    YawPitchRollAngles orientation = poseMT2.getOrientation();
                    telemetry.addData("Yaw: ", orientation.getYaw());
                    telemetry.addData("Pitch: ", orientation.getPitch());
                    telemetry.addData("Roll: ", orientation.getRoll());

                    packet.fieldOverlay().setStroke("#3FB551");
                    Drawing.drawRobot(packet.fieldOverlay(), PoseConverter.Pose3DtoPose2d(poseMT2));
                }
                FtcDashboard.getInstance().sendTelemetryPacket(packet);
            }
            telemetry.addLine("");
            telemetry.update();


        }
    }
}
