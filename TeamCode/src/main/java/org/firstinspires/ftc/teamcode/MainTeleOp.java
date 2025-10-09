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
                spinnerSequencer.stop();
                spindexer.rotateBy(120);
            }
            else if (gamepad1.leftBumperWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateBy(-120);
            }
            else if (gamepad1.dpadRightWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateBy(60);
//                spindexer.setCenteredPositionDegrees(500);
//                spindexer.setPosition(1);
            }
            else if (gamepad1.dpadLeftWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateBy(-60);
//                spindexer.setCenteredPositionDegrees(-500);
//                spindexer.setPosition(0);
            } else if (gamepad1.dpadDownWasPressed()) {
                spinnerSequencer.stop();
                spindexer.setCenteredPositionDegrees(0);
            } else if (gamepad1.xWasPressed()) {
//                spindexer.rotateSlotToSensor(0);
                spinnerSequencer.stop();
                spindexer.rotateStateToDrop(State.PURPLE);
//            } else if (gamepad1.yWasPressed()) {
//                spindexer.rotateSlotToSensor(1);
//            } else if (gamepad1.bWasPressed()) {
//                spindexer.rotateSlotToSensor(2);
            } else if (gamepad1.aWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateStateToDrop(State.GREEN);
            }

            if (gamepad1.left_stick_button) {
                spinnerSequencer.stop();
                spindexer.detectColor();
            } else if (gamepad1.right_stick_button) {
                spindexer.drop();
            }

            if (gamepad1.backWasPressed()) {
                if (ArtifactSequence != null) spinnerSequencer.runStates(
                        ArtifactSequence
                );
            }

            if (gamepad1.startWasPressed()) {
                if (ArtifactSequence == STATES_GPP) {
                    ArtifactSequence = STATES_PGP;
                    ArtifactSequenceString = "PGP";
                }
                else if (ArtifactSequence == STATES_PGP) {
                    ArtifactSequence = STATES_PPG;
                    ArtifactSequenceString = "PPG";
                }
                else if (ArtifactSequence == STATES_PPG) {
                    ArtifactSequence = STATES_GPP;
                    ArtifactSequenceString = "GPP";
                }
                else {
                    ArtifactSequence = STATES_GPP;
                    ArtifactSequenceString = "GPP";
                }

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
            telemetry.addData("SequenceActive: ", !spinnerSequencer.isDone());
            telemetry.addData("ArtifactSequence: ", ArtifactSequenceString);
            telemetry.update();

            spindexer.periodic();
            spinnerSequencer.update();
        }
    }
}
