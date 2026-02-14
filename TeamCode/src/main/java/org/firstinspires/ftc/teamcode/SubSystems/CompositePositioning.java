package org.firstinspires.ftc.teamcode.SubSystems;



import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.Cogintilities.PoseMath;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;

public class CompositePositioning implements TeamConstants {

    private final LimeVision limeVision;
    private final MecanumDrive autoDrive;
    private final RobotConfiguration.AllianceColor allianceColor; //TODO: Confirm that this updates with alliance color change

    private Pose2d limePos2d = null;
    private Pose2d autoDrivePos2d = null;

    private Pose2d compositePos2d = null;


    public CompositePositioning(LimeVision limeVision, MecanumDrive autoDrive, RobotConfiguration.AllianceColor allianceColor) {
        this.limeVision = limeVision;
        this.autoDrive = autoDrive;
        this.allianceColor = allianceColor;
    }

    public void update() {
        autoDrive.updatePoseEstimate();

        limePos2d = limeVision.getMediatedRobotPose2d();
        autoDrivePos2d = autoDrive.localizer.getPose();;

        if (limePos2d == null) {
            if (autoDrivePos2d != null) {
                compositePos2d = autoDrivePos2d;
            }
        } else {
            compositePos2d = limePos2d;
            autoDrive.localizer.setPose(limePos2d);
        }
    }

    public Position getAllianceGoalPosition() {
        if (compositePos2d == null) return null;

        if (allianceColor == RobotConfiguration.AllianceColor.BLUE) {
            return BLUE_GOAL_TARGET;
        } else if (allianceColor == RobotConfiguration.AllianceColor.RED) {
            return RED_GOAL_TARGET;
        } else return null;
    }

    public double getAllianceGoalBearing() {
        if (compositePos2d == null) return Double.NaN;
        if (allianceColor == null) {
            return limeVision.getGoalBearing(PoseMath.pose2dtoPose3D(compositePos2d));
        }

        Position targetPos = getAllianceGoalPosition();
        if (targetPos == null) return Double.NaN;

        double rawAngle = PoseMath.poseArcTan(compositePos2d, targetPos, AngleUnit.DEGREES);
        double robotBearing = rawAngle- Math.toDegrees(compositePos2d.heading.toDouble());

        return AngleUnit.normalizeDegrees(robotBearing);
    }

    public double getAllianceGoalDistance() {
        if (compositePos2d == null) return Double.NaN;
        if (allianceColor == null) {
            return limeVision.getGoalDistance(PoseMath.pose2dtoPose3D(compositePos2d));
        }

        Position targetPos = getAllianceGoalPosition();
        if (targetPos == null) return Double.NaN;

        return PoseMath.poseFlattenedDistance(compositePos2d, targetPos, DistanceUnit.INCH);
    }







}