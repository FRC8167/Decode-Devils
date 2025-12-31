package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Cogintilities.BetterMotor;
import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

@TeleOp(name="TeleOpTestShooter", group="Competition")
public class TeleOpTestShooter extends LinearOpMode implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

//        initializeRobot(true);
        BetterMotor motor = new BetterMotor(hardwareMap,"Shooter");
        motor.adjustMotorInformation(DefaultMotorInfo.GOBILDA_6000RPM);
        motor.setVelocity(0);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        FtcDashboard dashboard = FtcDashboard.getInstance();
//        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        double targetVel = 0;
//        TimedTimer timer = new TimedTimer(ConfigurableConstants.ALTERNATION_PERIOD/2.0);

        telemetry.addData("TargetVel: ", targetVel);
        telemetry.addData("CurrentVel: ", motor.getVelocityRPM());
        telemetry.update(); //Note: Updating telemetry while using initializeRobot() will delete possible warnings

        waitForStart();

        while (opModeIsActive()) {

//            if (timer.isDone()) {
//                if (targetVel == ConfigurableConstants.VEL_LOW_RPM) {
//                    targetVel = ConfigurableConstants.VEL_HIGH_RPM;
//                } else if (targetVel == ConfigurableConstants.VEL_HIGH_RPM) {
//                    targetVel = ConfigurableConstants.VEL_LOW_RPM;
//                } else {
//                    targetVel = ConfigurableConstants.VEL_LOW_RPM;
//                }
//                motor.setVelocityRPM(targetVel);
//                timer.startNewTimer(ConfigurableConstants.ALTERNATION_PERIOD/2.0);
//                motor.setVelocityPIDFCoefficients(
//                        ConfigurableConstants.KP,
//                        ConfigurableConstants.KI,
//                        ConfigurableConstants.KD,
//                        ConfigurableConstants.FF
//                );
//            }

            telemetry.addData("TargetVel: ", targetVel);
            telemetry.addData("CurrentVel: ", motor.getVelocityRPM());
            telemetry.update();
        }
    }
}
