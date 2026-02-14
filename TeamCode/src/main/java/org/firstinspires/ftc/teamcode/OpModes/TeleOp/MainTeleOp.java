package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Cogintilities.Color;
import org.firstinspires.ftc.teamcode.Cogintilities.ConfigurableConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.DrawingUtility;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;
import org.firstinspires.ftc.teamcode.Cogintilities.VariableShooterLookup;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

//@Disabled
@TeleOp(name="MainTeleOp", group="Competition")
public class MainTeleOp extends RobotConfiguration implements TeamConstants{

    int artifactsOnRamp;
//    AprilTagDetection posTag;
//    TimedTimer posTagValidityTimer = new TimedTimer();

    boolean autoAimDrive = false;


    TimedTimer buttonTimer;

    TimedTimer wobbleWaveTimer;

    DrawingUtility drawingUtility = new DrawingUtility();

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(null, false);

        artifactsOnRamp = 0;


//        buttonTimer = new TimedTimer();

        while (opModeInInit() && getArtifactSequence() == null) {
            if (vision != null) {
                vision.enableAprilTagDetection();
                vision.scanForAprilTags();
                AprilTagDetection tag = vision.getFirstTargetTag();
                if (tag != null) {
                    State[] states = vision.getFirstSequence();
                    if (states != null) {
                        telemetry.addData("States: ", State.convertStatesToInitials(states));
                        lightRGB_M.setColor(Color.AZURE);
                        setArtifactSequence(states);
                    } else {
                        telemetry.addLine("Invalid Tag");
                        lightRGB_M.setColor(Color.YELLOW);
                    }
                } else {
                    telemetry.addLine("No Tag Detected");
                    lightRGB_M.setColor(Color.RED);
                }
            } else {
                telemetry.addLine("Vision Inactive");
                lightRGB_M.setColor(Color.VIOLET);
                break;
            }
            telemetry.update();
        }

        double autoAimTurnCommand = 0;

        waitForStart();

        dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());

        vision.disableAprilTagDetection();

        while (opModeIsActive()) {
            drive.setDegradedDrive(gamepad1.right_bumper);
            if (!autoAimDrive)
                drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            else drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, autoAimTurnCommand);
            autoAimDrive = false;

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
            double distance = compositePositioning.getAllianceGoalDistance();
            if (!Double.isNaN(distance))
                shootVel = VariableShooterLookup.getVelocityByLookupDistance(distance);



            if (gamepad2.right_trigger > 0) {
                if (Double.isNaN(shootVel))
                    shootVel = ConfigurableConstants.SHOOTER_VELOCITY_FAR * gamepad2.right_trigger;
                if (spinnerSequencer.isDone()) {
                    shooter.setVelocityRPM(shootVel);
                }

            } else if (gamepad2.left_trigger > 0) {
                if (Double.isNaN(shootVel))
                    shootVel = ConfigurableConstants.SHOOTER_VELOCITY_CLOSE * gamepad2.left_trigger;
                if (spinnerSequencer.isDone()) {
                    shooter.setVelocityRPM(shootVel);
                }
            } else {
                if (spinnerSequencer.isDone())
                    shooter.setVelocityRPM(0);
            }

            spinnerSequencer.adjustShootVel(shootVel);


            if (gamepad1.aWasPressed() || gamepad1.crossWasPressed())
                drive.resetHeadingPIDF();
            if (gamepad1.a || gamepad1.cross) {
//                double goalBearing = limeVision != null ? limeVision.getGoalBearing() : Double.NaN;
                double goalBearing = compositePositioning.getAllianceLocalizerGoalBearing();
                if (!Double.isNaN(goalBearing)) {
                    autoAimTurnCommand = drive.getTurnToHeadingErrorCommand(goalBearing - gamepad1.right_trigger + gamepad1.left_trigger ); //TODO: Test if integration is correct
                    autoAimDrive = true;
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


            boolean rightBumper = gamepad2.right_bumper;
            boolean rightBumperPressed = gamepad2.rightBumperWasPressed();
            boolean leftBumper = gamepad2.left_bumper;
            boolean leftBumperPressed = gamepad2.leftBumperWasPressed();


            if (rightBumperPressed && !leftBumper) {
                artifactsOnRamp += 1;
                if (artifactsOnRamp > 9) {
                    artifactsOnRamp = 9;
                } else {
                    dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
//                    lightRGB_M.setColor(Color.WHITE);
//                    buttonTimer.startNewTimer(0.2);
                }
            } else if (leftBumperPressed && !rightBumper) {
                artifactsOnRamp -= 1;
                if (artifactsOnRamp < 0) {
                    artifactsOnRamp = 0;
                } else {
                    dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
//                    lightRGB_M.setColor(Color.YELLOW);
//                    buttonTimer.startNewTimer(0.2);
                }
            }

            if (rightBumper && leftBumper && (leftBumperPressed || rightBumperPressed)) {
                artifactsOnRamp = 0;
                dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
            }

            if (gamepad2.startWasPressed() || gamepad2.optionsWasPressed()) {
                spinnerSequencer.runScanAll();
            } else if (gamepad2.backWasPressed() || gamepad2.shareWasPressed()) {
//                spinnerSequencer.runDual(spinStates.getNextToShoot(artifactsOnRamp, artifactSequence));
                spinnerSequencer.runBestStatesToDrop(spinStates.getNextToShoot(artifactsOnRamp, getArtifactSequence()));
            }

            if (gamepad1.dpadLeftWasPressed()) {
                setAlliance(AllianceColor.BLUE);
            } else if (gamepad1.dpadRightWasPressed()) {
                setAlliance(AllianceColor.RED);
            }

            if (gamepad2.yWasPressed() || gamepad2.triangleWasPressed()) {
//                ArtifactSequence = new State[]{State.PURPLE, State.PURPLE, State.PURPLE};
//                AllianceColor allianceColor = getAlliance();
//                if (allianceColor == null) setAlliance(AllianceColor.BLUE);
//                else {
//                    switch (allianceColor) {
//                        case BLUE: setAlliance(AllianceColor.RED);
//                        case RED: setAlliance(AllianceColor.BLUE);
//                    }
//                }
//
//                if (getArtifactSequence() == STATES_GPP) {
//                    setArtifactSequence(STATES_PGP);
//                    dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
//                }
//                else if (getArtifactSequence() == STATES_PGP) {
//                    setArtifactSequence(STATES_PPG);
//                    dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
//                }
//                else if (getArtifactSequence() == STATES_PPG) {
//                    setArtifactSequence(STATES_GPP);
//                    dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
//                }
//                else {
//                    setArtifactSequence(STATES_GPP);
//                    dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
//                }
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
                setArtifactSequence(STATES_GPP);
                dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
            } else if (gamepad1.yWasPressed() || gamepad1.triangleWasPressed()) {
                setArtifactSequence(STATES_PGP);
                dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
            } else if (gamepad1.bWasPressed() || gamepad1.circleWasPressed()) {
                setArtifactSequence(STATES_PPG);
                dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
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
                    int dropIndex = spindexer.getActiveSlotDrop();
                    int oldSensorIndex = spindexer.getActiveSlotOldSensor();
                    if (dropIndex != -1) {
                        lightRGB_R.setColorState(spinStates.getSlot((dropIndex + 1) % 3));
                        State mState = spinStates.getSlot(dropIndex);
                        if (mState == spinStates.get1stNextToShoot(artifactsOnRamp, getArtifactSequence()) && getArtifactSequence() != null)
                            lightRGB_M.setColor(Color.AZURE);
                        else lightRGB_M.setColorState(mState);
                        lightRGB_L.setColorState(spinStates.getSlot((dropIndex - 1 + 3) % 3));
                    } else if (oldSensorIndex != -1) {
                        lightRGB_R.setColorState(spinStates.getSlot((oldSensorIndex - 1 + 3) % 3));
                        lightRGB_M.setColorState(spinStates.getSlot(oldSensorIndex));
                        lightRGB_L.setColorState(spinStates.getSlot((oldSensorIndex + 1) % 3));
                    }
                    spindexer.setCentered();
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
            telemetry.addData("Sequence: ", State.convertStatesToInitials(getArtifactSequence()));
            telemetry.addData("Next",    spinStates.get1stNextToShoot(artifactsOnRamp, getArtifactSequence()));
            telemetry.addData("2ndNext", spinStates.get2ndNextToShoot(artifactsOnRamp, getArtifactSequence()));
            telemetry.addData("3rdNext", spinStates.get3rdNextToShoot(artifactsOnRamp, getArtifactSequence()));

            telemetry.addLine("");

            telemetry.addData("ShooterVelocity: ", shooter.getVelocityRPM());
            telemetry.addData("ShooterTargetVelocity: ", shooter.getTargetVelocityRPM());
            telemetry.addData("CenterPos: ", spindexer.getCenteredPositionDegrees());
//            telemetry.addData("RawPos: ", spindexer.getPosition());
            telemetry.addData("ActiveDrop: ", spindexer.getActiveSlotDrop());
            telemetry.addData("ActiveSensor: ", spindexer.getActiveSlotSensor());

            telemetry.addLine();

            telemetry.addData("AllianceColor: ", getAlliance());

//            telemetry.addData("Slot0: ", spinStates.getSlot(0));
//            telemetry.addData("Slot1: ", spinStates.getSlot(1));
//            telemetry.addData("Slot2: ", spinStates.getSlot(2));

//            telemetry.addData("Mode: ", spinnerSequencer.getMode());
//            telemetry.addData("DualMode: ", spinnerSequencer.getDualMode());
//            telemetry.addData("Done?: ", spinnerSequencer.isDone());
//            telemetry.addData("Good?: ", getArtifactSequence() != null ? !spinnerSequencer.statesAreNotPresent(spinStates.getNextToShoot(artifactsOnRamp, getArtifactSequence())) : null);
//            telemetry.addData("NextToShoot: ", Arrays.toString(spinStates.getNextToShoot(artifactsOnRamp, getArtifactSequence())));
//            telemetry.addData("IntakePower: ", intake.getPower());
//            telemetry.addData("DropTimer: ", spindexer.getDropTimerRemainingTime());
//            telemetry.addData("SpinnerTimer: ", spindexer.getSpinnerRemainingTime());
//            telemetry.addData("DropPos: ", spindexer.getDropperPos());
//            telemetry.addData("SequenceActive: ", !spinnerSequencer.isDone());
//            telemetry.addData("ArtifactSequenceLength: ", getArtifactSequence() == null ? "null": getArtifactSequence().length);
            telemetry.addLine();
            telemetry.addData("Bearing: ",  compositePositioning.getAllianceGoalBearing());
            telemetry.addData("Distance: ", compositePositioning.getAllianceGoalDistance());

//            telemetry.addLine();
//            telemetry.addData("Color: ", colorDetection.getColor());
//            telemetry.addData("H: ", colorDetection.getColorHSV()[0]);
//            telemetry.addData("S: ", colorDetection.getColorHSV()[1]);
//            telemetry.addData("V: ", colorDetection.getColorHSV()[2]);
//            telemetry.addData("DistanceCM: ", colorDetection.getDistance(DistanceUnit.CM));

            telemetry.update();

            if (shooter.getTargetVelocityRPM() != 0) {
//                double mediatedBearing = limeVision != null ? limeVision.getMediatedGoalBearing() : Double.NaN;
                double mediatedBearing = compositePositioning.getAllianceGoalBearing();
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
        compositePositioning.update(getAlliance());
        drawingUtility.drawRobot(Color.BLUE, compositePositioning.getCompositePos2d());
        drawingUtility.send();

        spindexer.periodic();
        spinnerSequencer.update();
//        shooter.update();
//        lightRGB.setColor(spindexer.isSpinnerDone() ? "Blue":"Orange");
//        if (spindexer.getActiveSlotDrop() != -1) lightRGB.setColorState(spinStates.getSlot(spindexer.getActiveSlotDrop()));
//        else if (spindexer.getActiveSlotSensor() != -1) lightRGB.setColorState(spinStates.getSlot(spindexer.getActiveSlotSensor()));
//        telemetry.addData("Vel: ", shooter.getVelocityRPM());
//        telemetry.addData("TarVel: ", shooter.getTargetVelocityRPM());
//        telemetry.addData("C-Enough: ", shooter.isCloseEnough(100));
//            lightRGB_M.setColorState(spinStates.get1stNextToShoot(artifactsOnRamp, artifactSequence));
            int dropIndex = spindexer.getActiveSlotDrop();
            int oldSensorIndex = spindexer.getActiveSlotOldSensor();
            if (spindexer.isSpinnerDone()) {
                if (dropIndex != -1) {
                    lightRGB_R.setColorState(spinStates.getSlot((dropIndex + 1) % 3));
                    State mState = spinStates.getSlot(dropIndex);
                    if (mState == spinStates.get1stNextToShoot(artifactsOnRamp, getArtifactSequence()) && getArtifactSequence() != null)
                        lightRGB_M.setColor(Color.AZURE);
                    else lightRGB_M.setColorState(mState);
                    lightRGB_L.setColorState(spinStates.getSlot((dropIndex - 1 + 3) % 3));
                } else if (oldSensorIndex != -1) {
                    lightRGB_R.setColorState(spinStates.getSlot((oldSensorIndex - 1 + 3) % 3));
                    lightRGB_M.setColorState(spinStates.getSlot(oldSensorIndex));
                    lightRGB_L.setColorState(spinStates.getSlot((oldSensorIndex + 1) % 3));

                }
            }

        if (getArtifactSequence() == null && limeVision != null) {
            State[] statesL = limeVision.getFirstSequence();
            if (statesL != null) {
                setArtifactSequence(statesL);
                dataPrism.updateShootColors(artifactsOnRamp, getArtifactSequence());
            }

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
