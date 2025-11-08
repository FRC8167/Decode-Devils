package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

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
            }

            if (gamepad1.right_stick_button) {
                spinnerSequencer.stop();
                spindexer.drop();
                shooter.setMotorPower(SHOOTER_POWER);
            } else {
                shooter.setMotorPower(0);
            }

            if (gamepad1.backWasPressed()) {
                if (ArtifactSequence != null) {
//                    spinnerSequencer.runStatesToDrop(ArtifactSequence);
                    spinnerSequencer.runDual(ArtifactSequence);
                }
            }

            if (gamepad1.startWasPressed()) {
                spinnerSequencer.runScanAll();
            }

            if (gamepad1.yWasPressed()) {
//                ArtifactSequence = new State[]{State.PURPLE, State.PURPLE, State.PURPLE};
//
                if (ArtifactSequence == STATES_GPP) {
                    ArtifactSequence = STATES_PGP;
                }
                else if (ArtifactSequence == STATES_PGP) {
                    ArtifactSequence = STATES_PPG;
                }
                else if (ArtifactSequence == STATES_PPG) {
                    ArtifactSequence = STATES_GPP;
                }
                else {
                    ArtifactSequence = STATES_GPP;
                }
//                vision.scanForAprilTags();
//                AprilTagDetection tag = vision.getFirstTargetTag();
//                if (tag != null) {
//                    State[] states = vision.getTagStates(tag);
//                    if (states != null) {
//                        ArtifactSequence = states;
//                    }
//                }
            }

            if (gamepad1.b) {
                shooter.resetMin();
//                if (vision != null) {
//                    vision.scanForAprilTags();
//                    AprilTagDetection tag = vision.getFirstTargetTag();
//                    if (tag != null) {
//                        telemetry.addData("X", tag.ftcPose.x);
//                        telemetry.addData("Y", tag.ftcPose.y);
//                        telemetry.addData("Z", tag.ftcPose.z);
//                        telemetry.addData("Range", tag.ftcPose.range);
//                    }
//                }
            }




            telemetry.addData("ShooterSpeed: ", shooter.getMotorSpeedRPM());
            telemetry.addData("ShooterMinSpeed: ", shooter.getMinSpeedRPM());
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
            telemetry.addData("DropTimer: ", spindexer.getDropTimerRemainingTime());
            telemetry.addData("SpinnerTimer: ", spindexer.getSpinnerRemainingTime());
            telemetry.addData("DropPos: ", spindexer.getDropperPos());
            telemetry.addData("SequenceActive: ", !spinnerSequencer.isDone());
            telemetry.addData("ArtifactSequence: ", spinStates.convertStatesToInitials(ArtifactSequence));
            telemetry.addData("ArtifactSequenceLength: ", ArtifactSequence == null ? "null":ArtifactSequence.length);
            telemetry.update();

            periodic();
        }
    }

    private void periodic() {
        spindexer.periodic();
        spinnerSequencer.update();
        shooter.update();
//        lightRGB.setColor(spindexer.isSpinnerDone() ? "Blue":"Orange");
        if (spindexer.getActiveSlotDrop() != -1) lightRGB.setColorState(spinStates.getSlot(spindexer.getActiveSlotDrop()));
        else if (spindexer.getActiveSlotSensor() != -1) lightRGB.setColorState(spinStates.getSlot(spindexer.getActiveSlotSensor()));

    }
}
