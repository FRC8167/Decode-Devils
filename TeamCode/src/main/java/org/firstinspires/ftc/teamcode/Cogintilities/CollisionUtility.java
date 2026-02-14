package org.firstinspires.ftc.teamcode.Cogintilities;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

public class CollisionUtility {
    // Scaling factor: 100 means we preserve 2 decimal places of precision
    // before the Region converts coordinates to integers.
    private static final float SCALE_FACTOR = 10f;

    private CollisionUtility() {}

    /**
     * Checks if two general Paths intersect using scaled Regions for precision.
     */
    public static boolean checkCollision(Path path1, Path path2) {
        // Create a scaling matrix
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(SCALE_FACTOR, SCALE_FACTOR);

        // Apply scaling to clones of the paths so we don't modify the originals
        Path scaledPath1 = new Path(path1);
        Path scaledPath2 = new Path(path2);
        scaledPath1.transform(scaleMatrix);
        scaledPath2.transform(scaleMatrix);

        Region region1 = new Region();
        Region region2 = new Region();
        RectF rectF = new RectF();

        // Compute bounds and set path for the first scaled region
        scaledPath1.computeBounds(rectF, true);
        region1.setPath(scaledPath1, new Region(
                (int) Math.floor(rectF.left),
                (int) Math.floor(rectF.top),
                (int) Math.ceil(rectF.right),
                (int) Math.ceil(rectF.bottom)
        ));

        // Compute bounds and set path for the second scaled region
        scaledPath2.computeBounds(rectF, true);
        region2.setPath(scaledPath2, new Region(
                (int) Math.floor(rectF.left),
                (int) Math.floor(rectF.top),
                (int) Math.ceil(rectF.right),
                (int) Math.ceil(rectF.bottom)
        ));

        return region1.op(region2, Region.Op.INTERSECT);
    }

    /**
     * Checks collision between a rotated rectangle and a triangle.
     */
    public static boolean checkRectTriangleCollision(
            double rectCenterX, double rectCenterY, double width, double height, double angle,
            float x1, float y1, float x2, float y2, float x3, float y3) {

        Path rectPath = new Path();
        float left = (float) (rectCenterX - width / 2.0);
        float top = (float) (rectCenterY - height / 2.0);
        float right = (float) (rectCenterX + width / 2.0);
        float bottom = (float) (rectCenterY + height / 2.0);
        rectPath.addRect(left, top, right, bottom, Path.Direction.CW);
//        Path circlePath = new Path();
//        circlePath.addCircle((float) rectCenterX, (float) rectCenterY, 9, Path.Direction.CW);

        Matrix matrix = new Matrix();
        matrix.postRotate((float) angle, (float) rectCenterX, (float) rectCenterY);
        rectPath.transform(matrix);

        Path triPath = new Path();
        triPath.moveTo(x1, y1);
        triPath.lineTo(x2, y2);
        triPath.lineTo(x3, y3);
        triPath.close();

        return checkCollision(rectPath, triPath);
    }

    public static boolean checkRobotLaunchZoneOverlap(@NonNull Pose2d robotPose) {
        double rectCenterX = -robotPose.position.x + 144/2.0; //Note: For some reason x is backwards, no clue why
        double rectCenterY = -robotPose.position.y + 144/2.0; //Note: Computer graphics flip y axis
        double width = TeamConstants.ROBOT_WIDTH;
        double height = TeamConstants.ROBOT_LENGTH;
        double angle = 360 - Math.toDegrees(robotPose.heading.toDouble()); //Note: Computer graphics angles are clockwise

        float CloseX1 = (float) (TeamConstants.CLOSE_LAUNCH_ZONE_1.x + 144/2.0);
        float CloseY1 = (float) (-TeamConstants.CLOSE_LAUNCH_ZONE_1.y + 144/2.0);
        float CloseX2 = (float) (TeamConstants.CLOSE_LAUNCH_ZONE_2.x + 144/2.0);
        float CloseY2 = (float) (-TeamConstants.CLOSE_LAUNCH_ZONE_2.y + 144/2.0);
        float CloseX3 = (float) (TeamConstants.CLOSE_LAUNCH_ZONE_3.x + 144/2.0);
        float CloseY3 = (float) (-TeamConstants.CLOSE_LAUNCH_ZONE_3.y + 144/2.0);

        float FarX1 = (float) (TeamConstants.FAR_LAUNCH_ZONE_1.x + 144/2.0);
        float FarY1 = (float) (-TeamConstants.FAR_LAUNCH_ZONE_1.y + 144/2.0);
        float FarX2 = (float) (TeamConstants.FAR_LAUNCH_ZONE_2.x + 144/2.0);
        float FarY2 = (float) (-TeamConstants.FAR_LAUNCH_ZONE_2.y + 144/2.0);
        float FarX3 = (float) (TeamConstants.FAR_LAUNCH_ZONE_3.x + 144/2.0);
        float FarY3 = (float) (-TeamConstants.FAR_LAUNCH_ZONE_3.y + 144/2.0);

        return checkRectTriangleCollision(rectCenterX, rectCenterY, width, height, angle, CloseX1, CloseY1, CloseX2, CloseY2, CloseX3, CloseY3) ||
                checkRectTriangleCollision(rectCenterX, rectCenterY, width, height, angle, FarX1, FarY1, FarX2, FarY2, FarX3, FarY3);
    }

    public static boolean checkRobotLaunchZoneCloseOverlap(@NonNull Pose2d robotPose) {
        double rectCenterX = -robotPose.position.x + 144/2.0; //Note: For some reason x is backwards, no clue why
        double rectCenterY = -robotPose.position.y + 144/2.0; //Note: Computer graphics flip y axis
        double width = TeamConstants.ROBOT_WIDTH;
        double height = TeamConstants.ROBOT_LENGTH;
        double angle = 360 - Math.toDegrees(robotPose.heading.toDouble()); //Note: Computer graphics angles are clockwise

        float CloseX1 = (float) (TeamConstants.CLOSE_LAUNCH_ZONE_1.x + 144/2.0);
        float CloseY1 = (float) (-TeamConstants.CLOSE_LAUNCH_ZONE_1.y + 144/2.0);
        float CloseX2 = (float) (TeamConstants.CLOSE_LAUNCH_ZONE_2.x + 144/2.0);
        float CloseY2 = (float) (-TeamConstants.CLOSE_LAUNCH_ZONE_2.y + 144/2.0);
        float CloseX3 = (float) (TeamConstants.CLOSE_LAUNCH_ZONE_3.x + 144/2.0);
        float CloseY3 = (float) (-TeamConstants.CLOSE_LAUNCH_ZONE_3.y + 144/2.0);

        float FarX1 = (float) (TeamConstants.FAR_LAUNCH_ZONE_1.x + 144/2.0);
        float FarY1 = (float) (-TeamConstants.FAR_LAUNCH_ZONE_1.y + 144/2.0);
        float FarX2 = (float) (TeamConstants.FAR_LAUNCH_ZONE_2.x + 144/2.0);
        float FarY2 = (float) (-TeamConstants.FAR_LAUNCH_ZONE_2.y + 144/2.0);
        float FarX3 = (float) (TeamConstants.FAR_LAUNCH_ZONE_3.x + 144/2.0);
        float FarY3 = (float) (-TeamConstants.FAR_LAUNCH_ZONE_3.y + 144/2.0);

        return checkRectTriangleCollision(rectCenterX, rectCenterY, width, height, angle, CloseX1, CloseY1, CloseX2, CloseY2, CloseX3, CloseY3);
    }

    public static boolean checkRobotLaunchZoneFarOverlap(@NonNull Pose2d robotPose) {
        double rectCenterX = -robotPose.position.x + 144/2.0; //Note: For some reason x is backwards, no clue why
        double rectCenterY = -robotPose.position.y + 144/2.0; //Note: Computer graphics flip y axis
        double width = TeamConstants.ROBOT_WIDTH;
        double height = TeamConstants.ROBOT_LENGTH;
        double angle = 360 - Math.toDegrees(robotPose.heading.toDouble()); //Note: Computer graphics angles are clockwise

        float CloseX1 = (float) (TeamConstants.CLOSE_LAUNCH_ZONE_1.x + 144/2.0);
        float CloseY1 = (float) (-TeamConstants.CLOSE_LAUNCH_ZONE_1.y + 144/2.0);
        float CloseX2 = (float) (TeamConstants.CLOSE_LAUNCH_ZONE_2.x + 144/2.0);
        float CloseY2 = (float) (-TeamConstants.CLOSE_LAUNCH_ZONE_2.y + 144/2.0);
        float CloseX3 = (float) (TeamConstants.CLOSE_LAUNCH_ZONE_3.x + 144/2.0);
        float CloseY3 = (float) (-TeamConstants.CLOSE_LAUNCH_ZONE_3.y + 144/2.0);

        float FarX1 = (float) (TeamConstants.FAR_LAUNCH_ZONE_1.x + 144/2.0);
        float FarY1 = (float) (-TeamConstants.FAR_LAUNCH_ZONE_1.y + 144/2.0);
        float FarX2 = (float) (TeamConstants.FAR_LAUNCH_ZONE_2.x + 144/2.0);
        float FarY2 = (float) (-TeamConstants.FAR_LAUNCH_ZONE_2.y + 144/2.0);
        float FarX3 = (float) (TeamConstants.FAR_LAUNCH_ZONE_3.x + 144/2.0);
        float FarY3 = (float) (-TeamConstants.FAR_LAUNCH_ZONE_3.y + 144/2.0);

        return checkRectTriangleCollision(rectCenterX, rectCenterY, width, height, angle, FarX1, FarY1, FarX2, FarY2, FarX3, FarY3);
    }
}