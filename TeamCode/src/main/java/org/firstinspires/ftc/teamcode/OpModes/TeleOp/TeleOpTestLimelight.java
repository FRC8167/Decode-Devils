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
        Pose3D poseMT2 = null;

        waitForStart();
//        limeVision.takePhoto("Test");

        while (opModeIsActive()) {
            drive.setDegradedDrive(false);
            drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            if (limeVision != null) {
                poseMT1 = limeVision.getRobotPose3D();
            }

            if (poseMT1 != null) {
                telemetry.addLine("MT1:");
                telemetry.addLine("Position:");
                Position position = poseMT1.getPosition().toUnit(DistanceUnit.INCH);
                telemetry.addData("X: ", position.x);
                telemetry.addData("Y: ", position.y);
                telemetry.addData("Z: ", position.z);
                telemetry.addLine("Orientation:");
                YawPitchRollAngles orientation = poseMT1.getOrientation();
                telemetry.addData("Yaw: ", orientation.getYaw());
                telemetry.addData("Pitch: ", orientation.getPitch());
                telemetry.addData("Roll: ", orientation.getRoll());

                TelemetryPacket packet = new TelemetryPacket();
                packet.fieldOverlay().setStroke("#3F51B5");
                Drawing.drawRobot(packet.fieldOverlay(), LimeVision.Pose3DtoPose2d(poseMT1));
                packet.fieldOverlay().setStroke("#B5513F");
                Drawing.drawRobot(packet.fieldOverlay(), LimeVision.Pose3DtoPose2d(new Pose3D(RED_GOAL_CENTER, new YawPitchRollAngles(AngleUnit.DEGREES,0,0,0,0))));
                packet.fieldOverlay().setStroke("#3F51B5");
                Drawing.drawRobot(packet.fieldOverlay(), LimeVision.Pose3DtoPose2d(new Pose3D(BLUE_GOAL_CENTER, new YawPitchRollAngles(AngleUnit.DEGREES,0,0,0,0))));
                FtcDashboard.getInstance().sendTelemetryPacket(packet);


            }

//

            telemetry.addLine("");
            telemetry.update();


        }
    }
}
