package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TeleOpTemplate", group="Competition")
public class TeleOp_Template extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

//        initializeRobot();
        Servo servo = hardwareMap.get(Servo.class, "spinServo");
        servo.setPosition(1);
        servo.scaleRange(0.275,0.725);
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad2.a) {
                servo.setPosition(0);
            } else if (gamepad2.b) {
                servo.setPosition(1);
            }

            telemetry.addData("Pos: ", servo.getPosition());
            telemetry.update();
        }
    }
}
