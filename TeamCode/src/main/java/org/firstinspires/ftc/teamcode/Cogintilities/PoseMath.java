package org.firstinspires.ftc.teamcode.Cogintilities;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class PoseMath {
    private PoseMath() {}

    static public <T,T2> double pose3DDistance(T firstPose, T2 secondPose) {
        Position firstPosition = normalizeToPosition(firstPose);
        Position secondPosition = normalizeToPosition(secondPose);
        if (firstPosition == null || secondPosition == null) return Double.NaN;
        double x1 = firstPosition.x;
        double y1 = firstPosition.y;
        double z1 = firstPosition.z;
        double x2 = secondPosition.x;
        double y2 = secondPosition.y;
        double z2 = secondPosition.z;
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2) + Math.pow(z2-z1,2));
    }

    static public <T> double pose3DDistance(T pose) {
        Position originPosition = new Position(DistanceUnit.INCH,0,0,0,0);
        return pose3DDistance(originPosition, pose);
    }

    static public <T> Position normalizeToPosition(T pose) {
        if (pose == null) return null;
        else if (pose instanceof Pose3D) return ((Pose3D) pose).getPosition();
        else if (pose instanceof Position) return (Position) pose;
        else return null;
    }

    static public <T,T2> double poseArcTan(T firstPose, T2 secondPose, AngleUnit unit) {
        Position firstPosition = normalizeToPosition(firstPose);
        Position secondPosition = normalizeToPosition(secondPose);
        if (firstPosition == null || secondPosition == null) return Double.NaN;
        double x1 = firstPosition.x;
        double y1 = firstPosition.y;
        double x2 = secondPosition.x;
        double y2 = secondPosition.y;
        double radianAngle = Math.atan2(y2-y1, x2-x1);
        return unit.fromRadians(radianAngle);
    }

    static public <T> double poseArcTan(T pose, AngleUnit unit) {
        Position originPosition = new Position(DistanceUnit.INCH,0,0,0,0);
        return poseArcTan(originPosition, pose, unit);
    }

}
