package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Cogintilities.Color;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;
import org.firstinspires.ftc.teamcode.Cogintilities.VariableShooterLookup;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

//@Disabled
@TeleOp(name="TeleOpTestSmartShooter", group="Testing")
public class TeleOpTestSmartShooter extends RobotConfiguration implements TeamConstants{

    int artifactsOnRamp;
//    AprilTagDetection posTag;
//    TimedTimer posTagValidityTimer = new TimedTimer();

    boolean skipNextDrive = false;


    TimedTimer buttonTimer;

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(new Pose2d(0,0,0), true);

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
                        telemetry.addData("States: ", spinStates.convertStatesToInitials(states));
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

//        FileManagement.createFile("LookupData");
        double testShootVel = 0;
        waitForStart();

        while (opModeIsActive()) {

            drive.setDegradedDrive(gamepad1.right_bumper);
            if (!skipNextDrive)
                drive.mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            skipNextDrive = false;

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
            } else if (gamepad2.xWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateStateToDrop(State.PURPLE);
            } else if (gamepad2.aWasPressed()) {
                spinnerSequencer.stop();
                spindexer.rotateStateToDrop(State.GREEN);
            }

//            if (gamepad2.dpadUpWasPressed()) {
//                testShootVel += 50;
//            } else if (gamepad2.dpadDownWasPressed()) {
//                testShootVel -= 50;
//            }


            if (-gamepad2.left_stick_y > 0) {
                intake.setPower(INTAKE_POWER_FORWARD*-gamepad2.left_stick_y);
            } else if (-gamepad2.left_stick_y < 0) {
                intake.setPower(INTAKE_POWER_BACKWARD*gamepad2.left_stick_y);
            } else {
                intake.setPower(INTAKE_POWER_NEUTRAL);
            }

//            shooter.setVelocityRPM(
////                    Math.min(
////                            ConfigurableConstants.SHOOTER_VELOCITY_FAR * gamepad2.right_trigger +
////                                    ConfigurableConstants.SHOOTER_VELOCITY_CLOSE * gamepad2.left_trigger,
////                            shooter.getMaxVelocityRPM()
////                    )
////            );
            if (gamepad2.right_trigger > 0) {
                shooter.setVelocityRPM(testShootVel);
            } else if (gamepad2.left_trigger > 0) {
                shooter.setVelocityRPM(testShootVel);
            } else {
                shooter.setVelocityRPM(0);
            }

            if (gamepad1.a) {
                double goalBearing = limeVision.getGoalBearing();
                if (!Double.isNaN(goalBearing)) {
                    drive.turnToHeadingError(goalBearing); //TODO: Test if integration is correct
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

//            if (gamepad2.left_stick_button) {
//                spinnerSequencer.stop();
//                spindexer.detectColor();
//            }

            if (gamepad2.right_stick_button) {
                spinnerSequencer.stop();
                spindexer.drop();
//                shooter.setVelocityRPM(ConfigurableConstants.SHOOTER_VELOCITY_FAR);
            } else {
//                shooter.setVelocityRPM(0);
            }

            spindexer.setWiggleOffset(SPINNER_WIGGLE_MANUAL*gamepad2.right_stick_x);

            if (gamepad1.backWasPressed()) {
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

//            if (gamepad2.startWasPressed()) {
//                spinnerSequencer.runScanAll();
//            }

            if (gamepad2.yWasPressed()) {
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

            if (gamepad1.xWasPressed()) {
                artifactSequence = STATES_GPP;
            } else if (gamepad1.yWasPressed()) {
                artifactSequence = STATES_PGP;
            } else if (gamepad1.bWasPressed()) {
                artifactSequence = STATES_PPG;
            }

            if (gamepad2.b) {
//                shooter.resetMin();

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
            double distance = limeVision.getGoalDistance();
            testShootVel = VariableShooterLookup.getVelocityByDistance(distance);

//            if (gamepad2.rightBumperWasPressed()) {
//                FileManagement.appendToFile("LookupData", "TestedVelocity, Distance: " +testShootVel +", " + distance);
//            }

            telemetry.addData("TestedVelocity: ", testShootVel);
            telemetry.addData("Distance: ", distance);

            telemetry.update();

            if (gamepad2.rightBumperWasPressed()) {
                while (!gamepad2.rightStickButtonWasPressed()) {
                    sleep(20);
                }
            }

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
        if (buttonTimer.isDone() && shooter.getTargetVelocityRPM() == 0) {
            lightRGB.setColorState(spinStates.get2ndNextToShoot(artifactsOnRamp, artifactSequence));
        } else if (shooter.getTargetVelocityRPM() != 0) {
//            if (!posTagValidityTimer.isDone()) {
//                if (Math.abs(Math.toDegrees(Math.atan2(posTag.ftcPose.y + 5.5, posTag.ftcPose.x - 3.5)) - 90 + (gamepad1.left_trigger - gamepad1.right_trigger) * 3) <= 2) {
//                    lightRGB.setColor(Color.AZURE);
//                } else {
//                    lightRGB.setColor(Color.ORANGE);
//                }
            double bearing = limeVision.getMediatedGoalBearing();
            if (!Double.isNaN(bearing)) {
                if (Math.abs(bearing) <= 2) {
                    lightRGB.setColor(Color.AZURE); //TODO: Decide if color feedback is worth its drawbacks or come up with alternative (rumble?)
                } else lightRGB.setColor(Color.ORANGE);
            } else {
                lightRGB.setOff();
            }
        }
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
