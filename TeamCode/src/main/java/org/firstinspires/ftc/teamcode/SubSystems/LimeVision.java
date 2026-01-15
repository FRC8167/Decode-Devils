package org.firstinspires.ftc.teamcode.SubSystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Cogintilities.PoseMath;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

import java.util.ArrayList;
import java.util.List;

public class LimeVision implements TeamConstants {
    private final Limelight3A limelight;
//    IMU imu;
    private int pipeline;

    private List<Pose3D> previousPoses = new ArrayList<>();
    private LLResult previousResult = null;
    private LLResult recentResult = null;
    private double previousTagID;

    public LimeVision(Limelight3A limelight) {
        this.limelight = limelight;
        this.limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        this.limelight.start(); // This tells Limelight to start looking!
        this.limelight.pipelineSwitch(0);
        pipeline = 0;
    }

//    public LimeVision(Limelight3A limelight, IMU imu) {
//        this(limelight);
//        this.imu = imu;
//    }

//    public void setImu(IMU imu) {
//        this.imu = imu;
//    }


    public void setPipeline(int pipeline) {
        this.limelight.pipelineSwitch(pipeline);
        this.pipeline = pipeline;
    }

    public void start() {
        limelight.start();
    }

    public void stop() {
        limelight.stop();
    }

    public LLResult getResult() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid() && result.getPipelineIndex() == pipeline && result.getStaleness() <= LIME_VISION_STALENESS_TOL) {
            return result;
        } else return null;
    }

    public Pose3D getRobotPose3D() {
        previousPoses = PoseMath.removeOldPoses(previousPoses, LIME_VISION_POSE_MEDIATING_STALENESS_TOL);
        LLResult result = getResult();
        if (result == null) return null;
        List<LLResultTypes.FiducialResult> fiducials = getGoalFiducials(result);
        double fiducialID;
        if (fiducials != null && !fiducials.isEmpty()) {
            fiducialID = fiducials.get(0).getFiducialId();
            if (fiducialID != previousTagID) {
                previousPoses.clear();
                previousTagID = fiducialID;
            }
        } else return null;

        Pose3D pose = result.getBotpose();
        if (pose == null || !PoseMath.poseIsValid(pose)) return null;
        previousPoses.add(PoseMath.poseWithAcquisitionTime(pose));
        return pose;
    }

    public Pose3D getMediatiatedRobotPose3D() {
        getRobotPose3D();
        if (previousPoses.size() < LIME_VISION_POSE_MEDIATING_MIN_POSES) return null;
        return PoseMath.poseMedian(previousPoses, DistanceUnit.INCH, AngleUnit.DEGREES);
    }

    public double getPreviousPosesSize() {
        return previousPoses.size();
    }

    public Pose2d getRobotPose2d() {
        return PoseMath.Pose3DtoPose2d(getRobotPose3D());
    }

    public Pose2d getMediatedRobotPose2d() {
        return PoseMath.Pose3DtoPose2d(getMediatiatedRobotPose3D());
    }

    public double getGoalBearing(LLResult result, Pose3D pose3D) {
        if (result == null) return Double.NaN;
        if (pose3D == null) return Double.NaN;
        Position position = pose3D.getPosition();
        YawPitchRollAngles orientation = pose3D.getOrientation();

        List<LLResultTypes.FiducialResult> goalFiducials = getGoalFiducials(result);
        if (goalFiducials == null || goalFiducials.isEmpty()) return Double.NaN;
        LLResultTypes.FiducialResult goalFiducial = null;
        if (goalFiducials.size() == 1) {
            goalFiducial = goalFiducials.get(0);
        } else {
            goalFiducials.size();
            for (LLResultTypes.FiducialResult fiducialResult : goalFiducials) {
                int id = fiducialResult.getFiducialId();
                double angle = orientation.getYaw(AngleUnit.DEGREES);
                if (angle >= -180 && angle < 0 && id == 20) {
                    goalFiducial = fiducialResult;
                    break;
                } else if (angle >= 0 && angle < 180 && id == 24) {
                    goalFiducial = fiducialResult;
                    break;
                }

            }
        }

        if (goalFiducial == null) return Double.NaN;

        double rawAngle;

        int id = goalFiducial.getFiducialId();
        if (id == 20) {
            rawAngle = PoseMath.poseArcTan(position, BLUE_GOAL_CENTER, AngleUnit.DEGREES);
        } else if (id == 24) {
            rawAngle = PoseMath.poseArcTan(position, RED_GOAL_CENTER, AngleUnit.DEGREES);
        } else {
            return Double.NaN;
        }

        double robotBearing = rawAngle-orientation.getYaw(AngleUnit.DEGREES);
        return AngleUnit.normalizeDegrees(robotBearing);
    }

    public double getGoalBearing(Pose3D pose3D) {
        return getGoalBearing(getResult(), pose3D);
    }

    public double getGoalBearing() {
        return getGoalBearing(getResult(), getRobotPose3D());
    }

    public double getMediatedGoalBearing() {
        return getGoalBearing(getResult(), getMediatiatedRobotPose3D());
    }

    static public List<LLResultTypes.FiducialResult> getGoalFiducials(LLResult result) { //Note: O
        if (result == null || !result.isValid()) return null;
        List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
        if (fiducialResults == null || fiducialResults.isEmpty()) return null;
        List<LLResultTypes.FiducialResult> goalResults = new ArrayList<>();
        for (LLResultTypes.FiducialResult fiducialResult : fiducialResults) {
            int id = fiducialResult.getFiducialId();
            if (id == 20 || id == 24) {
                goalResults.add(fiducialResult);
            }
        }
        return goalResults;
//        if (fiducialResults.size() == 1) {
//            LLResultTypes.FiducialResult fiducialResult = fiducialResults.get(0);
//            int id = fiducialResult.getFiducialId();
//            if (id == 20 || id == 24) {
//                return fiducialResult;
//            } else return null;
//        } else return null;
    }

    static public String tagIdLookup(int id) {
        switch (id) {
            case 20: return "BLUE GOAL";
            case 21: return "GPP";
            case 22: return "PGP";
            case 23: return "PPG";
            case 24: return "RED GOAL";
            default: return "Unknown";
        }
    }

//    public Pose3D getMT2Pos() {
//        double robotYaw = imu.getRobotYawPitchRollAngles().getYaw();
//        limelight.updateRobotOrientation(robotYaw);
//        LLResult result = getResult();
//        if (result == null) return null;
//        return result.getBotpose_MT2();
//    }

    public void takePhoto(String string) {
        limelight.captureSnapshot(string);
    }




}
