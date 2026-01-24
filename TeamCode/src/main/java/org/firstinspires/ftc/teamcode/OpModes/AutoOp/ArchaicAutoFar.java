package org.firstinspires.ftc.teamcode.OpModes.AutoOp;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Cogintilities.Color;
import org.firstinspires.ftc.teamcode.Cogintilities.ConfigurableConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


//@Disabled
@Autonomous(name="ArchaicAutoFar", group="Autonomous", preselectTeleOp = "MainTeleOp")
public class ArchaicAutoFar extends RobotConfiguration implements TeamConstants {

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(new Pose2d(0,0,0), true);

        int step = 0;

        TimedTimer timer = new TimedTimer();

        while (opModeInInit()) {
            if (vision != null) {
                vision.enableAprilTagDetection();
                vision.scanForAprilTags();
                AprilTagDetection tag = vision.getFirstTargetTag();
                if (tag != null) {
                    State[] states = vision.getFirstSequence();
                    if (states != null) {
                        telemetry.addData("States: ", State.convertStatesToInitials(states));
                        setArtifactSequence(states);
                        lightRGB_M.setColor(Color.AZURE);
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
            }

            if (limeVision != null) {

                double adjustedBearing = limeVision.getMediatedGoalBearing();
                telemetry.addLine("");
                telemetry.addData("Bearing(Calculated & Adjusted): ", adjustedBearing);
                if (Math.abs(adjustedBearing) <= 1) {
                    telemetry.addLine("Position OK");
                }
                if (!Double.isNaN(adjustedBearing))
                    dataPrism.bearingColors(adjustedBearing, 1);
                else dataPrism.clear();

                Pose3D pose = limeVision.getMediatiatedRobotPose3D();
                if (pose != null){
                    if (pose.getPosition().y > 2) {
                        setAlliance(AllianceColor.RED);
                    } else if (pose.getPosition().y < 2) {
                        setAlliance(AllianceColor.BLUE);
                    } else setAlliance(null);
                } else setAlliance(null);
                telemetry.addLine();
                AllianceColor allianceColor = getAlliance();
                telemetry.addData("Alliance: ", allianceColor != null ? allianceColor.name() : "null");
            }

            telemetry.update();
        }

        waitForStart();

        dataPrism.clear();

        TimedTimer parkTimer = new TimedTimer(25);

        if (vision != null) {
            vision.enableAprilTagDetection();
            vision.scanForAprilTags();
            AprilTagDetection tag = vision.getFirstTargetTag();
            if (tag != null) {
                State[] states = vision.getFirstSequence();
                if (states != null) {
                    setArtifactSequence(states);
                }
            }
            vision.disableAprilTagDetection();
        }

//        double firstPos = 60;
//        double firstOff = 0;
//        double secondPos = -60;
//        double secondOff = 0;
//
//        if (artifactSequence != null) {
//            State firstState = artifactSequence[0];
//            State secondState = artifactSequence[1];
//            lightRGB.setColorState(firstState);
//            if (firstState == State.GREEN || secondState == State.GREEN) {
//                firstPos = -60;
//                firstOff = 0;
//                secondPos = 60;
//                secondOff = 0;
//            }
//        }

        shooter.setVelocityRPM(ConfigurableConstants.SHOOTER_VELOCITY_AUTO_FAR);

        double position;

        if (getArtifactSequence() == STATES_PPG) {
            position = 360;
            spindexer.setCenteredPositionDegrees(0);
        } else if (getArtifactSequence() == STATES_PGP) {
            position = 480;
            spindexer.setCenteredPositionDegrees(120);
        } else if (getArtifactSequence() == STATES_GPP) {
            position = -360;
            spindexer.setCenteredPositionDegrees(0);
        } else {
            position = -360;
            spindexer.setCenteredPositionDegrees(0);
        }



        while (opModeIsActive()) {
            if (timer.isDone()) {
                if (shooter.isCloseEnough(100) && spindexer.isSpinnerDone() && step == 0) {
                    spindexer.drop();
                    step = 1;
                    timer.startNewTimer(2);
                }

                else if (step == 1) {
                    spindexer.drop();
                    if (spindexer.getCenteredPositionDegrees() != position) {
                        spindexer.setCenteredPositionDegrees(position);
                    }
                    if (spindexer.isSpinnerDone()) {
                        step = 2;
                        timer.startNewTimer(3);
                    }
                }

                else if (step == 2) {
                    spindexer.drop();
//                    if (spindexer.getCenteredPositionDegrees() != secondPos) {
//                        spindexer.setWiggleOffset(secondOff);
//                        spindexer.setCenteredPositionDegrees(secondPos);
//                    }
                    if (spindexer.isSpinnerDone()) {
                        step = 3;
//                        timer.startNewTimer(3);
                    }
                }

                else if (step == 3) {
                    shooter.setVelocityRPM(0);
                    spindexer.setCenteredPositionDegrees(0);
                    step = 4;

                }

                else if (step == 4 && parkTimer.isDone()) {
                    drive.mecanumDrive(1, 0, 0);
                    timer.startNewTimer(0.3);
                    step = 5;

                } else if (step == 5) {
                    drive.mecanumDrive(0, 0, 0);
                    lightRGB_M.setOff();
                    timer.startNewTimer(2);
                    step = 6;
                } else if (step == 6) {
                    break;
                }
            }

            if (step <= 3) {
                spindexer.drop();
            }


            spindexer.update();
            telemetry.addData("Step: ", step);
            telemetry.addData("RemainingTime: ", timer.getRemainingTime());
            telemetry.addData("Done: ", timer.isDone());
            telemetry.update();
        }




        }

    }

