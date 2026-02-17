package org.firstinspires.ftc.teamcode.SubSystems;



import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.Cogintilities.CollisionUtility;
import org.firstinspires.ftc.teamcode.Cogintilities.PoseMath;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;

public class CompositePositioning implements TeamConstants {

    private final LimeVision limeVision;
    private final DistanceDetection distanceDetection;
    private final MecanumDrive autoDrive;
    private RobotConfiguration.AllianceColor allianceColor;

    private Pose2d limePos2d = null;
    private Pose2d rawLimePos2d = null;
    private Pose2d autoDrivePos2d = null;
    private PoseVelocity2d poseVel = null;

    private Pose2d compositePos2d = null;


    public CompositePositioning(LimeVision limeVision, DistanceDetection distanceDetection, MecanumDrive autoDrive) {
        this.limeVision = limeVision;
        this.distanceDetection = distanceDetection;
        this.autoDrive = autoDrive;
    }

    public Pose2d getCompositePos2d() {
        return compositePos2d; //Note: Is likely not edit safe, external edits may mess with functionality
    }

    public Pose2d getLimePos2d() {
        return limePos2d;
    }
    public Pose2d getAutoDrivePos2d() {
        return autoDrivePos2d;
    }

    public PoseVelocity2d getPoseVel() {
        return poseVel;
    }

    public void update(RobotConfiguration.AllianceColor allianceColor) {
        this.allianceColor = allianceColor;
        poseVel = autoDrive.updatePoseEstimate();
        if (limeVision != null) {
            limePos2d = limeVision.getMediatedRobotPose2d();
            rawLimePos2d = limeVision.getRobotPose2d();
        } else limePos2d = null;

        autoDrivePos2d = autoDrive.localizer.getPose();

        if (limePos2d == null) {
            if (autoDrivePos2d != null) {
                compositePos2d = autoDrivePos2d;
            }
        } else {
            compositePos2d = limePos2d;
            if (isStationary() || autoDrivePos2d == null || PoseMath.poseAngularYawDifference(rawLimePos2d, autoDrivePos2d, AngleUnit.DEGREES) > 5) //TODO: Adjust/Confirm tolerance & logic
                autoDrive.localizer.setPose(limePos2d);
        }
    }

    public boolean isStationary() {
        return (poseVel != null && poseVel.linearVel.x == 0 && poseVel.linearVel.y == 0 && poseVel.angVel == 0);
    }

    public boolean isStationaryEnough() {
        return (poseVel != null && Math.abs(poseVel.linearVel.x) <= 5 && Math.abs(poseVel.linearVel.y) <= 5 && Math.abs(poseVel.angVel) <= Math.toDegrees(5));
    } //I believe units are in/s & rad/s (ccw positive)

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

    public double getAllianceGoalBearing_LocalizerPreferred() {
        if (compositePos2d == null) return Double.NaN;
        if (allianceColor == null) {
            if (autoDrivePos2d != null)
                return limeVision.getGoalBearing(PoseMath.pose2dtoPose3D(autoDrivePos2d));
            else return limeVision.getGoalBearing(PoseMath.pose2dtoPose3D(compositePos2d));
        }

        Position targetPos = getAllianceGoalPosition();
        if (targetPos == null) return Double.NaN;

        if (autoDrivePos2d != null) {
            double rawAngle = PoseMath.poseArcTan(autoDrivePos2d, targetPos, AngleUnit.DEGREES);
            double robotBearing = rawAngle - Math.toDegrees(autoDrivePos2d.heading.toDouble());

            return AngleUnit.normalizeDegrees(robotBearing);
        } else {
            double rawAngle = PoseMath.poseArcTan(compositePos2d, targetPos, AngleUnit.DEGREES);
            double robotBearing = rawAngle - Math.toDegrees(compositePos2d.heading.toDouble());

            return AngleUnit.normalizeDegrees(robotBearing);
        }
    }

    public double getAllianceGoalBearing_Lime() {
        if (limePos2d == null) return Double.NaN;
        if (allianceColor == null) {
            return limeVision.getGoalBearing(PoseMath.pose2dtoPose3D(limePos2d));
        }

        Position targetPos = getAllianceGoalPosition();
        if (targetPos == null) return Double.NaN;

        double rawAngle = PoseMath.poseArcTan(limePos2d, targetPos, AngleUnit.DEGREES);
        double robotBearing = rawAngle - Math.toDegrees(limePos2d.heading.toDouble());

        return AngleUnit.normalizeDegrees(robotBearing);
    }

    public double getAllianceGoalBearing_RawLime() {
        if (compositePos2d == null) return Double.NaN;
        if (allianceColor == null) {
            if (rawLimePos2d != null) return limeVision.getGoalBearing(PoseMath.pose2dtoPose3D(rawLimePos2d));
            else if (autoDrivePos2d != null) return limeVision.getGoalBearing(PoseMath.pose2dtoPose3D(autoDrivePos2d));
            else return Double.NaN;
        }

        Position targetPos = getAllianceGoalPosition();
        if (targetPos == null) return Double.NaN;

        double rawAngle;
        double robotBearing;
        if (rawLimePos2d != null) {
            rawAngle = PoseMath.poseArcTan(rawLimePos2d, targetPos, AngleUnit.DEGREES);
            robotBearing = rawAngle - Math.toDegrees(rawLimePos2d.heading.toDouble());
        }

        else {
            rawAngle = PoseMath.poseArcTan(compositePos2d, targetPos, AngleUnit.DEGREES);
            robotBearing = rawAngle - Math.toDegrees(compositePos2d.heading.toDouble());
        }

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

    public boolean checkRobotLaunchZoneOverlap() {
        if (compositePos2d == null) return false;
        else return CollisionUtility.checkRobotLaunchZoneOverlap(compositePos2d);
    }
    public boolean checkRobotLaunchZoneCloseOverlap() {
        if (compositePos2d == null) return false;
        else return CollisionUtility.checkRobotLaunchZoneCloseOverlap(compositePos2d);
    }
    public boolean checkRobotLaunchZoneFarOverlap() {
        if (compositePos2d == null) return false;
        else return CollisionUtility.checkRobotLaunchZoneFarOverlap(compositePos2d);
    }

    public boolean checkRobotGoalAlignment() {
        return (Math.abs(getAllianceGoalBearing_Lime()) <= 2); //Requires tag to be visible. Note: may prove to cause future issues
    }

    public boolean checkOverallReadiness() {
        return checkRobotLaunchZoneOverlap() && checkRobotGoalAlignment() && isStationaryEnough() && distanceDetection.isClear(); //TODO: Add dist. sensor when attached & implemented
    }

}