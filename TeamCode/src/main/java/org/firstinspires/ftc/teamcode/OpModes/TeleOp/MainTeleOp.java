package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.Color;
import org.firstinspires.ftc.teamcode.Cogintilities.ConfigurableConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;
import org.firstinspires.ftc.teamcode.Cogintilities.VariableShooterLookup;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Arrays;

//@Disabled
@TeleOp(name="MainTeleOp", group="Competition")
public class MainTeleOp extends RobotConfiguration implements TeamConstants{

    int artifactsOnRamp;
//    AprilTagDetection posTag;
//    TimedTimer posTagValidityTimer = new TimedTimer();

    boolean skipNextDrive = false;


    TimedTimer buttonTimer;

    TimedTimer wobbleWaveTimer;

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(new Pose2d(0,0,0), false);

        artifactsOnRamp = 0;

        buttonTimer = new TimedTimer();

        while (opModeInInit() && artifactSequence == null) {
            if (vision != null) {
                vision.enableAprilTagDetection();
                vision.scanForAprilTags();
                AprilTagDetection tag = vision.getFirstTargetTag();
                if (tag != null) {
                    State[] states = vision.getFirstSequence();
                    if (states != null) {
                        telemetry.addData("States: ", State.convertStatesToInitials(states));
                        lightRGB.setColor(Color.AZURE);
                        artifactSequence = states;
                    } else {
                        telemetry.addLine("Invalid Tag");
                        lightRGB.setColor(Color.YELLOW);
                    }
                } else {
                    telemetry.addLine("No Tag Detected");
                    lightRGB.setColor(Color.RED);
                }
            } else {
                telemetry.addLine("Vision Inactive");
                lightRGB.setColor(Color.VIOLET);
                break;
            }
            telemetry.update();
        }

        waitForStart();

        while (opModeIsActive()) {

            drive.setDegradedDrive(gamepad1.right_bumper);
            if (!skipNextDrive)
                drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            skipNextDrive = false;

//            if (gamepad2.rightBumperWasPressed()) {
//                spinnerSequencer.stop();
//                spindexer.rotateBy(120);
//            }
//            else if (gamepad2.leftBumperWasPressed()) {
//                spinnerSequencer.stop();
//                spindexer.rotateBy(-120);
//            }
            if (gamepad2.dpadRightWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateBy(60);
            }
            else if (gamepad2.dpadLeftWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateBy(-60);
            } else if (gamepad2.dpadDownWasPressed()) {
                spinnerSequencer.stop();
                spindexer.setCenteredPositionDegrees(0);
            } else if (gamepad2.xWasPressed() || gamepad2.squareWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateStateToDrop(State.PURPLE);
            } else if (gamepad2.aWasPressed() || gamepad2.crossWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateStateToDrop(State.GREEN);
            }

//            if (-gamepad2.left_stick_y > 0) {
//                intake.setPower(INTAKE_POWER_FORWARD*-gamepad2.left_stick_y);
//            } else if (-gamepad2.left_stick_y < 0) {
//                intake.setPower(INTAKE_POWER_BACKWARD*gamepad2.left_stick_y);
//            } else {
//                intake.setPower(INTAKE_POWER_NEUTRAL);
//            }

            double shootVel = Double.NaN;
            if (limeVision != null) {
                double distance = limeVision.getGoalDistance();
                if (!Double.isNaN(distance))
                    shootVel = VariableShooterLookup.getVelocityByDistance(distance);
            }

            spinnerSequencer.adjustShootVel(shootVel);

            if (gamepad2.right_trigger > 0) {
                spinnerSequencer.stop();
                if (!Double.isNaN(shootVel))
                    shooter.setVelocityRPM(shootVel);
                else
                    shooter.setVelocityRPM(ConfigurableConstants.SHOOTER_VELOCITY_FAR * gamepad2.left_trigger);

            } else if (gamepad2.left_trigger > 0) {
                spinnerSequencer.stop();
                if (!Double.isNaN(shootVel))
                    shooter.setVelocityRPM(shootVel);
                else
                    shooter.setVelocityRPM(ConfigurableConstants.SHOOTER_VELOCITY_CLOSE * gamepad2.left_trigger);
            } else {
                if (spinnerSequencer.isDone()) shooter.setVelocityRPM(0);
            }

            if (gamepad1.a || gamepad1.cross) {
                double goalBearing = limeVision != null ? limeVision.getGoalBearing() : Double.NaN;
                if (!Double.isNaN(goalBearing)) {
                    drive.turnToHeadingError(goalBearing - gamepad1.right_trigger + gamepad1.left_trigger ); //TODO: Test if integration is correct
                    skipNextDrive = true;
                }
//                if (!posTagValidityTimer.isDone()) {
//                    drive.turnToHeadingError(-(Math.toDegrees(Math.atan2(posTag.ftcPose.y + 5.5, posTag.ftcPose.x - 3.5)) - 90 + (gamepad1.left_trigger - gamepad1.right_trigger) * 3));
//                    skipNextDrive = true;
//                }
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
//                shooter.setVelocityRPM(ConfigurableConstants.SHOOTER_VELOCITY_FAR);
            } else {
//                shooter.setVelocityRPM(0);
            }



            if (gamepad1.backWasPressed() || gamepad1.shareWasPressed()) {
//                if (ArtifactSequence != null) {
////                    spinnerSequencer.runStatesToDrop(ArtifactSequence);
//                    spinnerSequencer.runDual(ArtifactSequence);
//                }
                artifactsOnRamp = 0;

            }

            if (gamepad1.dpadUpWasPressed() && buttonTimer.isDone()) {
                artifactsOnRamp += 1;
                if (artifactsOnRamp > 9) {
                    artifactsOnRamp = 9;
                } else {
                    lightRGB.setColor(Color.WHITE);
                    buttonTimer.startNewTimer(0.2);
                }
            } else if (gamepad1.dpadDownWasPressed() && buttonTimer.isDone()) {
                artifactsOnRamp -= 1;
                if (artifactsOnRamp < 0) {
                    artifactsOnRamp = 0;
                } else {
                    lightRGB.setColor(Color.YELLOW);
                    buttonTimer.startNewTimer(0.2);
                }
            }

            if (gamepad2.startWasPressed() || gamepad2.optionsWasPressed()) {
                spinnerSequencer.runScanAll();
            } else if (gamepad2.backWasPressed() || gamepad2.shareWasPressed()) {
//                spinnerSequencer.runDual(spinStates.getNextToShoot(artifactsOnRamp, artifactSequence));
                spinnerSequencer.runDual(spinStates.getNextToShoot(artifactsOnRamp, artifactSequence));
            }

            if (gamepad2.yWasPressed() || gamepad2.triangleWasPressed()) {
//                ArtifactSequence = new State[]{State.PURPLE, State.PURPLE, State.PURPLE};
//
                if (artifactSequence == STATES_GPP) {
                    artifactSequence = STATES_PGP;
                }
                else if (artifactSequence == STATES_PGP) {
                    artifactSequence = STATES_PPG;
                }
                else if (artifactSequence == STATES_PPG) {
                    artifactSequence = STATES_GPP;
                }
                else {
                    artifactSequence = STATES_GPP;
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

            if (gamepad1.xWasPressed() || gamepad1.squareWasPressed()) {
                artifactSequence = STATES_GPP;
            } else if (gamepad1.yWasPressed() || gamepad1.triangleWasPressed()) {
                artifactSequence = STATES_PGP;
            } else if (gamepad1.bWasPressed() || gamepad1.circleWasPressed()) {
                artifactSequence = STATES_PPG;
            }



            if (gamepad2.bWasPressed() || gamepad2.circleWasPressed()) {
                wobbleWaveTimer = new TimedTimer();
            }
            if (gamepad2.b || gamepad2.circle) {

                State activeState = spindexer.getActiveSlotSensor() != -1 ? spinStates.getSlot(spindexer.getActiveSlotSensor()) : null;
                if (activeState != State.UNKNOWN && activeState != State.NONE) {
                    if (spinStates.isStateInStates(State.UNKNOWN))
                        spindexer.rotateStateToSensor(State.UNKNOWN);
                    else if (spinStates.isStateInStates(State.NONE))
                        spindexer.rotateStateToSensor(State.NONE);
                } else {
                    spindexer.setWiggleOffset(Math.sin(wobbleWaveTimer.getElapsedTime()*2*Math.PI)*SPINNER_WIGGLE_MANUAL);
                    spindexer.detectColor();
                }
                if (!spinStates.isStateInStates(State.UNKNOWN) && !spinStates.isStateInStates(State.NONE)) {
                    spindexer.setWiggleOffset(0);
                }
            } else {
                spindexer.setWiggleOffset(SPINNER_WIGGLE_MANUAL*gamepad2.right_stick_x);
            }


//            if (visionPos != null) {
//                visionPos.scanForAprilTags();
//                AprilTagDetection tag = visionPos.getFirstGoalTag();
//                if (tag != null) {
////                    telemetry.addData("X: ", tag.ftcPose.x);
////                    telemetry.addData("Y: ", tag.ftcPose.y);
////                    telemetry.addData("Z: ", tag.ftcPose.z);
////                    telemetry.addData("Bearing: ", tag.ftcPose.bearing);
////                    telemetry.addData("Bearing(Calculated): ", Math.toDegrees(Math.atan2(tag.ftcPose.y, tag.ftcPose.x))-90);
////                    telemetry.addData("Bearing(Calculated & Adjusted): ", Math.toDegrees(Math.atan2(tag.ftcPose.y+5.5, tag.ftcPose.x-3.5))-90);
//
//                    posTag = tag;
//                    posTagValidityTimer.startNewTimer(0.2);
//
//                    Position position = tag.robotPose.getPosition();
//                    YawPitchRollAngles orientation = tag.robotPose.getOrientation();
////                    telemetry.addData("RobotX: ", position.x);
////                    telemetry.addData("RobotY: ", position.y);
////                    telemetry.addData("RobotZ: ", position.z);
////                    telemetry.addData("RobotRoll: ", orientation.getRoll());
////                    telemetry.addData("RobotYaw: ", orientation.getYaw());
////                    telemetry.addData("RobotPitch: ", orientation.getPitch());
////                    telemetry.addData("Angle?: ", Math.toDegrees(Math.atan2(-65+position.y, -65+position.x));
////                    telemetry.addLine("");
//                }
//            }


            telemetry.addData("ArtifactsOnRamp: ", artifactsOnRamp);
            telemetry.addData("Sequence: ", State.convertStatesToInitials(artifactSequence));
            telemetry.addData("Next",    spinStates.get1stNextToShoot(artifactsOnRamp, artifactSequence));
            telemetry.addData("2ndNext", spinStates.get2ndNextToShoot(artifactsOnRamp, artifactSequence));
            telemetry.addData("3rdNext", spinStates.get3rdNextToShoot(artifactsOnRamp, artifactSequence));

            telemetry.addLine("");

            telemetry.addData("ShooterVelocity: ", shooter.getVelocityRPM());
            telemetry.addData("ShooterTargetVelocity: ", shooter.getTargetVelocityRPM());
            telemetry.addData("CenterPos: ", spindexer.getCenteredPositionDegrees());
//            telemetry.addData("RawPos: ", spindexer.getPosition());
            telemetry.addData("ActiveDrop: ", spindexer.getActiveSlotDrop());
            telemetry.addData("ActiveSensor: ", spindexer.getActiveSlotSensor());

            telemetry.addData("Slot0: ", spinStates.getSlot(0));
            telemetry.addData("Slot1: ", spinStates.getSlot(1));
            telemetry.addData("Slot2: ", spinStates.getSlot(2));

            telemetry.addData("Mode: ", spinnerSequencer.getMode());
            telemetry.addData("DualMode: ", spinnerSequencer.getDualMode());
            telemetry.addData("Done?: ", spinnerSequencer.isDone());
            telemetry.addData("Good?: ", artifactSequence != null ? !spinnerSequencer.statesAreNotPresent(spinStates.getNextToShoot(artifactsOnRamp, artifactSequence)) : null);
            telemetry.addData("NextToShoot: ", Arrays.toString(spinStates.getNextToShoot(artifactsOnRamp, artifactSequence)));
//            telemetry.addData("IntakePower: ", intake.getPower());
//            telemetry.addData("DropTimer: ", spindexer.getDropTimerRemainingTime());
//            telemetry.addData("SpinnerTimer: ", spindexer.getSpinnerRemainingTime());
//            telemetry.addData("DropPos: ", spindexer.getDropperPos());
//            telemetry.addData("SequenceActive: ", !spinnerSequencer.isDone());
            telemetry.addData("ArtifactSequenceLength: ", artifactSequence == null ? "null": artifactSequence.length);
            telemetry.addLine();
            telemetry.addData("Bearing: ", limeVision.getMediatedGoalBearing());

            telemetry.addLine();
            telemetry.addData("Color: ", colorDetection.getColor());
            telemetry.addData("H: ", colorDetection.getColorHSV()[0]);
            telemetry.addData("S: ", colorDetection.getColorHSV()[1]);
            telemetry.addData("V: ", colorDetection.getColorHSV()[2]);
            telemetry.addData("DistanceCM: ", colorDetection.getDistance(DistanceUnit.CM));

            telemetry.update();

            if (shooter.getTargetVelocityRPM() != 0) {
                double mediatedBearing = limeVision != null ? limeVision.getMediatedGoalBearing() : Double.NaN;
                if (!Double.isNaN(mediatedBearing)) {
                    if (Math.abs(mediatedBearing) <= 2) {
                        gamepad2.stopRumble();
                    } else {
                        gamepad2.rumble(Gamepad.RUMBLE_DURATION_CONTINUOUS);
                    }
                } else {
                    gamepad2.rumble(Gamepad.RUMBLE_DURATION_CONTINUOUS);
                }
            } else gamepad2.stopRumble();

            periodic();

        }
    }

    private void periodic() {
        spindexer.periodic();
        spinnerSequencer.update();
//        shooter.update();
//        lightRGB.setColor(spindexer.isSpinnerDone() ? "Blue":"Orange");
//        if (spindexer.getActiveSlotDrop() != -1) lightRGB.setColorState(spinStates.getSlot(spindexer.getActiveSlotDrop()));
//        else if (spindexer.getActiveSlotSensor() != -1) lightRGB.setColorState(spinStates.getSlot(spindexer.getActiveSlotSensor()));
//        telemetry.addData("Vel: ", shooter.getVelocityRPM());
//        telemetry.addData("TarVel: ", shooter.getTargetVelocityRPM());
//        telemetry.addData("C-Enough: ", shooter.isCloseEnough(100));
        if (buttonTimer.isDone()) {
            lightRGB.setColorState(spinStates.get1stNextToShoot(artifactsOnRamp, artifactSequence));
        }

        if (artifactSequence == null && limeVision != null) {
            State[] statesL = limeVision.getFirstSequence();
            if (statesL != null) artifactSequence = statesL;

        }

//        if (limeVision != null) {
//            telemetry.addData("LimeSequence: ",  spinStates.convertStatesToInitials(limeVision.getFirstSequence()));
//            telemetry.addData("ObeliskIDs: ",  LimeVision.getObeliskIDs(limeVision.getResult()));
//        }


//        if (shooter.isCloseEnough(100)) {
//            double targetVel = shooter.getTargetVelocityRPM();
//            if (targetVel == 0) {
//                lightRGB.setOff();
//
//            } else if (targetVel == ConfigurableConstants.SHOOTER_VELOCITY_CLOSE) {
//                lightRGB.setColor(Color.GREEN);
//            } else if (targetVel == ConfigurableConstants.SHOOTER_VELOCITY_FAR) {
//                lightRGB.setColor(Color.AZURE);
//            } else {
//                lightRGB.setColor(Color.VIOLET);
//            }
//        } else {
//            lightRGB.setColor(Color.ORANGE);
//        }
    }
}
