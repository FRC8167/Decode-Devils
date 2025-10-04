package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

//@Disabled
@TeleOp(name="MainTeleOp", group="Competition")
public class MainTeleOp extends RobotConfiguration implements TeamConstants{

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(true);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.rightBumperWasPressed()) {
                spindexer.rotateBy(120);
            }
            else if (gamepad1.leftBumperWasPressed()) {
                spindexer.rotateBy(-120);
            }
            else if (gamepad1.dpadRightWasPressed()) {
                spindexer.rotateBy(60);
//                spindexer.setCenteredPositionDegrees(500);
//                spindexer.setPosition(1);
            }
            else if (gamepad1.dpadLeftWasPressed()) {
                spindexer.rotateBy(-60);
//                spindexer.setCenteredPositionDegrees(-500);
//                spindexer.setPosition(0);
            } else if (gamepad1.dpadDownWasPressed()) {
                spindexer.setCenteredPositionDegrees(0);
            } else if (gamepad1.xWasPressed()) {
//                spindexer.rotateSlotToSensor(0);
                spindexer.rotateStateToDrop(State.Purple);
//            } else if (gamepad1.yWasPressed()) {
//                spindexer.rotateSlotToSensor(1);
//            } else if (gamepad1.bWasPressed()) {
//                spindexer.rotateSlotToSensor(2);
            } else if (gamepad1.aWasPressed()) {
                spindexer.rotateStateToDrop(State.Green);
            }

            if (gamepad1.left_stick_button) {
                spindexer.detectColor();
            } else if (gamepad1.right_stick_button) {
                spindexer.drop();
            }

            if (gamepad2.a) {
                spindexer.setDropperPosition(0);
            } else if (gamepad2.b) {
                spindexer.setDropperPosition(1);
            }

            telemetry.addData("Color: ", colorDetection.getColor());
            telemetry.addData("H: ", colorDetection.getColorHSV()[0]);
            telemetry.addData("S: ", colorDetection.getColorHSV()[1]);
            telemetry.addData("V: ", colorDetection.getColorHSV()[2]);
            telemetry.addData("CenterPos: ", spindexer.getCenteredPositionDegrees());
            telemetry.addData("RawPos: ", spindexer.getPosition());
            telemetry.addData("ActiveDrop: ", spindexer.getActiveSlotDrop());
            telemetry.addData("ActiveSensor: ", spindexer.getActiveSlotSensor());
            telemetry.addData("Slot0: ", spinStates.getSlot(0));
            telemetry.addData("Slot1: ", spinStates.getSlot(1));
            telemetry.addData("Slot2: ", spinStates.getSlot(2));
            telemetry.addData("Timer: ", spindexer.getRemainingTime());
            telemetry.addData("DropPos: ", spindexer.getDropperPos());
            telemetry.update();

            spindexer.periodic();
        }
    }
}
