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
    Limelight3A limelight;
//    IMU imu;
    int pipeline;

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

    public void start() {
        limelight.start();
    }

    public void stop() {
        limelight.stop();
    }

    public LLResult getResult() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid() && result.getPipelineIndex() == 0 && result.getStaleness() <= LIME_VISION_STALENESS_TOL) {
            return result;
        } else return null;
    }

    public Pose3D getRobotPose3D() {
        LLResult result = getResult();
        if (result == null) return null;
        return result.getBotpose();
    }

    public Pose2d getRobotPose2d() {
        return Pose3DtoPose2d(getRobotPose3D());
    }

    public double getGoalBearing() {
        LLResult result = getResult();
        if (result == null) return Double.NaN;
        Pose3D pose3D = result.getBotpose();
        if (pose3D == null) return Double.NaN;
        Position position = pose3D.getPosition();
        YawPitchRollAngles orientation = pose3D.getOrientation();

//        TODO: finish coding bearing calculation
    }

    static public List<LLResultTypes.FiducialResult> getGoalFiducial(LLResult result) { //Note: O
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

    public static Pose2d Pose3DtoPose2d(Pose3D pose3D) {
        Position position = pose3D.getPosition().toUnit(DistanceUnit.INCH);
        YawPitchRollAngles orientation = pose3D.getOrientation();

        return new Pose2d(position.x, position.y, orientation.getYaw(AngleUnit.RADIANS));
    }


}
