package org.firstinspires.ftc.teamcode.Cogintilities;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public final class PoseConverter {

    private PoseConverter() {}

    public static Pose2d Pose3DtoPose2d(Pose3D pose3D) {
        Position position = pose3D.getPosition();
        YawPitchRollAngles orientation = pose3D.getOrientation();

        return new Pose2d(position.x, position.y, orientation.getYaw(AngleUnit.RADIANS));
    }

//    public static Pose3D Pose2dtoPose3F(Pose2d pose2d) { //Note: Defaults for pitch and roll not known, fix when known
//
//
//        Position position = new Position(DistanceUnit.INCH, pose2d.position.x, pose2d.position.y, 0, System.nanoTime());
//        YawPitchRollAngles orientation = new YawPitchRollAngles(AngleUnit.RADIANS, pose2d.heading.toDouble(), 0, 0, System.nanoTime());
//
//        return new Pose3D(position, orientation);
//    }


}
