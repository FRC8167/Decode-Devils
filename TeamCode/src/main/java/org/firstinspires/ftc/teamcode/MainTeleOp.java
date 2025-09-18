package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//@Disabled
@TeleOp(name="MainTeleOp", group="Competition")
public class MainTeleOp extends RobotConfiguration {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
            }

            telemetry.addData("Color: ", colorDetection.getColor());
            telemetry.addData("H: ", colorDetection.getColorHSV()[0]);
            telemetry.addData("S: ", colorDetection.getColorHSV()[1]);
            telemetry.addData("V: ", colorDetection.getColorHSV()[2]);
            telemetry.update();
        }
    }
}
