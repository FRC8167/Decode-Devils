package org.firstinspires.ftc.teamcode.Cogintilities;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.RoadRunner.Drawing;

public final class DrawingUtility {
    private TelemetryPacket packet;

    public DrawingUtility() {
        packet = new TelemetryPacket();
    }

    public void drawRobot(String hexColor, Pose2d robotPos) {
        if (robotPos == null) return;
        packet.fieldOverlay().setStroke(hexColor);
        Drawing.drawRobot(packet.fieldOverlay(), robotPos);
    }

    public void drawRobot(String hexColor, Pose3D robotPos) {
        if (robotPos == null) return;
        packet.fieldOverlay().setStroke(hexColor);
        Drawing.drawRobot(packet.fieldOverlay(), PoseMath.Pose3DtoPose2d(robotPos));
    }

    public void drawRobot(Color color, Pose2d robotPos) {
        if (robotPos == null) return;
        packet.fieldOverlay().setStroke(color.getHexValue());
        Drawing.drawRobot(packet.fieldOverlay(), robotPos);
    }

    public void drawRobot(Color color, Pose3D robotPos) {
        if (robotPos == null) return;
        packet.fieldOverlay().setStroke(color.getHexValue());
        Drawing.drawRobot(packet.fieldOverlay(), PoseMath.Pose3DtoPose2d(robotPos));
    }

    public void send() {
        if (packet != null)
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
        packet = new TelemetryPacket();
    }
}
