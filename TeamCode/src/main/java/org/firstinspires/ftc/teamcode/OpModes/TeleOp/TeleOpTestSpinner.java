package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.SubSystems.Spinner;

@TeleOp(name="TeleOpTestSpinner", group="Competition")
public class TeleOpTestSpinner extends LinearOpMode implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {


        Spinner spinner = new Spinner(hardwareMap.get(Servo.class, "spinServo"), 0.5, 0, 1, true);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad2.yWasPressed()) {
                spinner.setCenteredPositionDegrees(0);
            } else if (gamepad2.bWasPressed()) {
                spinner.rotateBy(10);
            } else if (gamepad2.aWasPressed()) {

            } else if (gamepad2.xWasPressed()) {
                spinner.rotateBy(-10);
            }

            telemetry.addData("Pos: ", spinner.getCenteredPositionDegrees());
            telemetry.update();
        }
    }
}
