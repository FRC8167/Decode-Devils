package org.firstinspires.ftc.teamcode.Cogintilities;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class LimeVision implements TeamConstants{
    Limelight3A limelight;
    IMU imu;
    int pipeline;

    public LimeVision(Limelight3A limelight) {
        this.limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        this.limelight.start(); // This tells Limelight to start looking!
        limelight.pipelineSwitch(0);
        pipeline = 0;
    }

    public LimeVision(Limelight3A limelight, IMU imu) {
        this(limelight);
        this.imu = imu;
    }

    public void setImu(IMU imu) {
        this.imu = imu;
    }

    public void start() {
        limelight.start();
    }

    public void stop() {
        limelight.stop();
    }

    public LLResult getResult() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid() && result.getPipelineIndex() == 0 && result.getStaleness() <= LimeVision_Staleness_Tol) {
            return result;
        } else return null;
    }

    public Pose3D getMT1Pos() {
        LLResult result = getResult();
        if (result == null) return null;
        return result.getBotpose();
    }

    public Pose3D getMT2Pos() {
        double robotYaw = imu.getRobotYawPitchRollAngles().getYaw();
        limelight.updateRobotOrientation(robotYaw);
        LLResult result = getResult();
        if (result == null) return null;
        return result.getBotpose_MT2();
    }

    public void takePhoto(String string) {
        limelight.captureSnapshot(string);
    }


}
