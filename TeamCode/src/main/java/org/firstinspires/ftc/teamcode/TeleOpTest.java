package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.BetterMotor;
import org.firstinspires.ftc.teamcode.Cogintilities.MotorInformation;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

@TeleOp(name="TeleOpTest", group="Competition")
public class TeleOpTest extends LinearOpMode implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

//        initializeRobot();
//        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "ExpSpinner");
        BetterMotor motor = new BetterMotor(hardwareMap, "ExpSpinner");

//        MotorInformation.adjustMotor(motor, MotorInformation.REV_HEX_125RPM);
        motor.adjustMotorInformation(MotorInformation.REV_HEX_125RPM);

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//        motor.setTargetPosition(0);
        motor.setTargetPosition(0, AngleUnit.DEGREES);

        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(10,3,0,0.05));
        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, new PIDFCoefficients(0.8,0,0,0));
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setVelocityRPM(100);
//        Servo servo = hardwareMap.get(Servo.class, "servoRGB");
//        servo.scaleRange(DROPPER_SERVO_RANGE[0], DROPPER_SERVO_RANGE[1]);
//        double pos = 0;
//        LightRGB lightRGB = new LightRGB(servo, 0,0,1);
//        int color = 0; //0-9
//        servo.setPosition(0);
        waitForStart();

        while (opModeIsActive()) {
//            motor.setPower(-gamepad2.right_stick_y);
            if (gamepad2.yWasPressed()) {
//                motor.setTargetPosition(0);
                motor.setTargetPosition(0, AngleUnit.DEGREES);
//                motor.setVelocityRPM(0);
//                motor.setPower(0);
            } else if (gamepad2.bWasPressed()) {
//                motor.setTargetPosition((int) (EXP_SPINNER_TICKS_PER_REV/360 * 120));
                motor.setTargetPosition(120, AngleUnit.DEGREES);
//                motor.setVelocityRPM(50);
//                motor.setPower(0.5);
            } else if (gamepad2.aWasPressed()) {
//                motor.setTargetPosition((int) (EXP_SPINNER_TICKS_PER_REV/360 * 120 * 2));
                motor.setTargetPosition(240, AngleUnit.DEGREES);
//                motor.setVelocityRPM(100);
//                motor.setPower(1);
            } else if (gamepad2.xWasPressed()) {
//                motor.setTargetPosition((int) (EXP_SPINNER_TICKS_PER_REV/360 * 120*3));
                motor.setTargetPosition(360, AngleUnit.DEGREES);
//                motor.setVelocityRPM(-50);
//                motor.setPower(-1);
            }
//            lightRGB.setColorState(State.PURPLE);
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

//            telemetry.addData("Pos: ", motor.getCurrentPosition()/EXP_SPINNER_TICKS_PER_REV*360);
            telemetry.addData("Pos: ", motor.getCurrentPosition(AngleUnit.DEGREES));
            telemetry.addData("Vel: ", motor.getVelocity(AngleUnit.DEGREES));
            telemetry.addData("kP-ToPos", motor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION).p);
//            telemetry.addData("kI", motor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).i);
//            telemetry.addData("kD", motor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).d);
//            telemetry.addData("FF", motor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).f);
            telemetry.addData("VelRPM: ", motor.getVelocityRPM());
            telemetry.addData("Power: ", motor.getPower());
//            telemetry.addData("Color: ", color%10);
            telemetry.update();
        }
    }
}
