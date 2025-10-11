package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.SubSystems.LightRGB;
import org.firstinspires.ftc.teamcode.SubSystems.Servo1D;

@TeleOp(name="TeleOpTest", group="Competition")
public class TeleOpTest extends LinearOpMode implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

//        initializeRobot();
        Servo servo = hardwareMap.get(Servo.class, "servoRGB");
//        servo.scaleRange(DROPPER_SERVO_RANGE[0], DROPPER_SERVO_RANGE[1]);
//        double pos = 0;
        LightRGB lightRGB = new LightRGB(servo, 0,0,1);
        int color = 0; //0-9
//        servo.setPosition(0);
        waitForStart();

        while (opModeIsActive()) {
            lightRGB.setColorState(State.PURPLE);
//            lightRGB.setOn();
//            lightRGB.setColorPosition(-gamepad1.left_stick_y);
//            switch (color%10) {
//                case 0: lightRGB.setColor("Red"); break;
//                case 1: lightRGB.setColor("Orange"); break;
//                case 2: lightRGB.setColor("Yellow"); break;
//                case 3: lightRGB.setColor("Sage"); break;
//                case 4: lightRGB.setColor("Green"); break;
//                case 5: lightRGB.setColor("Azure"); break;
//                case 6: lightRGB.setColor("Blue"); break;
//                case 7: lightRGB.setColor("Indigo"); break;
//                case 8: lightRGB.setColor("Violet"); break;
//                case 9: lightRGB.setColor("White"); break;
//            }
//
//            if (gamepad1.aWasPressed()) {
//                color = color + 1;
//            }
//            servo.setPosition(-gamepad1.left_stick_y);
//            if (gamepad2.aWasPressed()) {
//                servo.setPosition(0);
////                pos -= 0.01;
////                servo.setPosition(pos);
//            } else if (gamepad2.bWasPressed()) {
//                servo.setPosition(1);
////                pos += 0.01;
////                servo.setPosition(pos);
//            }

            telemetry.addData("Pos: ", servo.getPosition());
            telemetry.addData("Color: ", color%10);
            telemetry.update();
        }
    }
}
