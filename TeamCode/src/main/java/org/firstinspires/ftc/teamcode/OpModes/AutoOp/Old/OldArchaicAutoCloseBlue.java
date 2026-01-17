package org.firstinspires.ftc.teamcode.OpModes.AutoOp.Old;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Cogintilities.Color;
import org.firstinspires.ftc.teamcode.Cogintilities.ConfigurableConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


//@Disabled
@Autonomous(name="OldArchaicAutoCloseBlue", group="Autonomous", preselectTeleOp = "MainTeleOp")
public class OldArchaicAutoCloseBlue extends RobotConfiguration implements TeamConstants {

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot(new Pose2d(0,0,0), true);
        setAlliance(AllianceColor.BLUE);

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
                        telemetry.addData("States: ", spinStates.convertStatesToInitials(states));
                        lightRGB.setColor(Color.AZURE);
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
            }
            telemetry.update();
        }

        waitForStart();

        TimedTimer parkTimer = new TimedTimer(25);

        if (vision != null) {
            vision.enableAprilTagDetection();
            vision.scanForAprilTags();
            AprilTagDetection tag = vision.getFirstTargetTag();
            if (tag != null) {
                State[] states = vision.getTagStates(tag);
                if (states != null) {
                    artifactSequence = states;
                }
            }
            vision.disableAprilTagDetection();
        }

        double firstPos = 60;
        double firstOff = 0;
        double secondPos = -60;
        double secondOff = 0;

        if (artifactSequence != null) {
            State firstState = artifactSequence[0];
            State secondState = artifactSequence[1];
            lightRGB.setColorState(firstState);
            if (firstState == State.GREEN || secondState == State.GREEN) {
                firstPos = -60;
                firstOff = 0;
                secondPos = 60;
                secondOff = 0;
            }
        }

        shooter.setVelocityRPM(ConfigurableConstants.SHOOTER_VELOCITY_AUTO_CLOSE);
        drive.mecanumDrive(-1, 0, 0);
        timer.startNewTimer(1);

        while (opModeIsActive()) {
            if (timer.isDone()) {
                if (step == 0) {
                    drive.mecanumDrive(0, 0, 0);
                }

                if (shooter.isCloseEnough(100) && step == 0) {
                    step = 1;
                    timer.startNewTimer(1);
                }

                else if (step == 1) {
                    spindexer.drop();
                    if (spindexer.getCenteredPositionDegrees() != firstPos) {
                        spindexer.setWiggleOffset(firstOff);
                        spindexer.setCenteredPositionDegrees(firstPos);
                    }
                    if (spindexer.isSpinnerDone()) {
                        step = 2;
                        timer.startNewTimer(3);
                    }
                }

                else if (step == 2) {
                    spindexer.drop();
                    if (spindexer.getCenteredPositionDegrees() != secondPos) {
                        spindexer.setWiggleOffset(secondOff);
                        spindexer.setCenteredPositionDegrees(secondPos);
                    }
                    if (spindexer.isSpinnerDone()) {
                        step = 3;
                        timer.startNewTimer(3);
                    }
                }

                else if (step == 3) {
                    shooter.setVelocityRPM(0);
                    spindexer.setCenteredPositionDegrees(0);
                    step = 4;

                }

                else if (step == 4 && parkTimer.isDone()) {
                    drive.mecanumDrive(0, -1, 0);
                    timer.startNewTimer(0.4);
                    step = 5;

                } else if (step == 5) {
                    drive.mecanumDrive(0, 0, 0);
                    lightRGB.setOff();
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

