//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.HardwareDevice;
//
//import org.firstinspires.ftc.teamcode.Cogintilities.ServoFixed;
//import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
//import org.firstinspires.ftc.teamcode.SubSystems.Servo1D;
//
//@TeleOp(name="TeleOpTest", group="Competition")
//public class TeleOpTest extends LinearOpMode implements TeamConstants {
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//
////        initializeRobot();
//        ServoFixed servo = hardwareMap.get(ServoFixed.class, "servo");
////        servo.scaleRange(DROPPER_SERVO_RANGE[0], DROPPER_SERVO_RANGE[1]);
////        double pos = 0;
//        servo.setPosition(0);
//        waitForStart();
//
//        while (opModeIsActive()) {
//            if (gamepad2.aWasPressed()) {
//                servo.setPosition(0);
////                pos -= 0.01;
////                servo.setPosition(pos);
//            } else if (gamepad2.bWasPressed()) {
//                servo.setPosition(1);
////                pos += 0.01;
////                servo.setPosition(pos);
//            }
//
//            telemetry.addData("Pos: ", servo.getPosition());
//            telemetry.update();
//        }
//    }
//}
