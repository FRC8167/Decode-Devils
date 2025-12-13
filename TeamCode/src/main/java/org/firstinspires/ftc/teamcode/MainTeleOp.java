package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Cogintilities.ConfigurableConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

//@Disabled
@TeleOp(name="MainTeleOp", group="Competition")
public class MainTeleOp extends RobotConfiguration implements TeamConstants{

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(true);

        waitForStart();

        while (opModeIsActive()) {

            drive.setDegradedDrive(gamepad1.right_bumper);
            drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            if (gamepad2.rightBumperWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateBy(120);
            }
            else if (gamepad2.leftBumperWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateBy(-120);
            }
            else if (gamepad2.dpadRightWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateBy(60);
            }
            else if (gamepad2.dpadLeftWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateBy(-60);
            } else if (gamepad2.dpadDownWasPressed()) {
                spinnerSequencer.stop();
                spindexer.setCenteredPositionDegrees(0);
            } else if (gamepad2.xWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateStateToDrop(State.PURPLE);
            } else if (gamepad2.aWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateStateToDrop(State.GREEN);
            }

            if (-gamepad2.left_stick_y > 0) {
                intake.setPower(INTAKE_POWER_FORWARD*-gamepad2.left_stick_y);
            } else if (-gamepad2.left_stick_y < 0) {
                intake.setPower(INTAKE_POWER_BACKWARD*gamepad2.left_stick_y);
            } else {
                intake.setPower(INTAKE_POWER_NEUTRAL);
            }

//            if (gamepad2.dpad_up) {
//                intake.setMotorPower(INTAKE_POWER_FORWARD);
//            } else if (gamepad2.dpad_down) {
//                intake.setMotorPower(INTAKE_POWER_BACKWARD);
//            } else {
//                intake.setMotorPower(INTAKE_POWER_NEUTRAL);
//            }

            if (gamepad2.left_stick_button) {
                spinnerSequencer.stop();
                spindexer.detectColor();
            }

            if (gamepad2.right_stick_button) {
                spinnerSequencer.stop();
                spindexer.drop();
//                shooter.setRawPower(SHOOTER_POWER);
//                if (shooter.getTargetVelocityRPM() != ConfigurableConstants.SHOOTER_VELOCITY)
                    shooter.setVelocityRPM(ConfigurableConstants.SHOOTER_VELOCITY);
            } else {
//                if (shooter.getTargetVelocityRPM() != 0)
                    shooter.setVelocityRPM(0);
//                shooter.setRawPower(0);
            }

            if (gamepad2.backWasPressed()) {
                if (ArtifactSequence != null) {
//                    spinnerSequencer.runStatesToDrop(ArtifactSequence);
                    spinnerSequencer.runDual(ArtifactSequence);
                }
            }

            if (gamepad2.startWasPressed()) {
                spinnerSequencer.runScanAll();
            }

            if (gamepad2.yWasPressed()) {
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

            if (gamepad2.b) {
//                shooter.resetMin();

            }

            if (-gamepad2.right_stick_y >= 0) {
                fork.setPosition(-gamepad2.right_stick_y*.1);
            } else {
                fork.setPosition(0.06);
            }


//0.06



            telemetry.addData("ShooterVelocity: ", shooter.getVelocityRPM());
            telemetry.addData("ShooterTargetVelocity: ", shooter.getTargetVelocityRPM());
//            telemetry.addData("ShooterPower: ", shooter.getRawPower());
//            telemetry.addData("PIDF: ", shooter.getVelocityPIDFCoefficients().toString());
//            telemetry.addData("ShooterMode: ", shooter.getMode().toString());
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
            telemetry.addData("IntakePower: ", intake.getPower());
            telemetry.addData("DropTimer: ", spindexer.getDropTimerRemainingTime());
            telemetry.addData("SpinnerTimer: ", spindexer.getSpinnerRemainingTime());
            telemetry.addData("DropPos: ", spindexer.getDropperPos());
            telemetry.addData("ForkPos: ", fork.getPos());
            telemetry.addData("SequenceActive: ", !spinnerSequencer.isDone());
            telemetry.addData("ArtifactSequence: ", spinStates.convertStatesToInitials(ArtifactSequence));
            telemetry.addData("ArtifactSequenceLength: ", ArtifactSequence == null ? "null":ArtifactSequence.length);
            telemetry.addData("LF: ", drive.getLFpower());
            telemetry.addData("RF: ", drive.getRFpower());
            telemetry.addData("LR: ", drive.getLRpower());
            telemetry.addData("RR: ", drive.getRRpower());
            telemetry.update();

            periodic();
        }
    }

    private void periodic() {
        spindexer.periodic();
        spinnerSequencer.update();
//        shooter.update();
//        lightRGB.setColor(spindexer.isSpinnerDone() ? "Blue":"Orange");
        if (spindexer.getActiveSlotDrop() != -1) lightRGB.setColorState(spinStates.getSlot(spindexer.getActiveSlotDrop()));
        else if (spindexer.getActiveSlotSensor() != -1) lightRGB.setColorState(spinStates.getSlot(spindexer.getActiveSlotSensor()));

    }
}
