package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@Disabled
@TeleOp(name="MainTeleOp", group="Competition")
public class MainTeleOp extends RobotConfiguration {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(true);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                spindexer.rotateBy(120);
            }
            else if (gamepad1.left_bumper) {
                spindexer.rotateBy(-120);
            }
            else if (gamepad1.dpad_right) {
                spindexer.rotateBy(60);
            }
            else if (gamepad1.dpad_left) {
                spindexer.rotateBy(-60);
            }

            telemetry.addData("Color: ", colorDetection.getColor());
            telemetry.addData("H: ", colorDetection.getColorHSV()[0]);
            telemetry.addData("S: ", colorDetection.getColorHSV()[1]);
            telemetry.addData("V: ", colorDetection.getColorHSV()[2]);
            telemetry.addData("CenterPos: ", spindexer.getCenteredPositionDegrees());
            telemetry.update();
        }
    }
}
