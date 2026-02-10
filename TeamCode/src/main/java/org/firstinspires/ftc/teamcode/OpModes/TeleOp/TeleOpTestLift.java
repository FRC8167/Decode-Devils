package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.BetterMotor;
import org.firstinspires.ftc.teamcode.Cogintilities.ConfigurableConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;
import org.firstinspires.ftc.teamcode.SubSystems.Lift;
import org.firstinspires.ftc.teamcode.SubSystems.Motor1D;

@TeleOp(name="TeleOpTestLift", group="Testing")
public class TeleOpTestLift extends LinearOpMode implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {


//        initializeRobot(true);
        DcMotorEx rawMotor = new BetterMotor(hardwareMap,"Lift");
//        motor.adjustMotorInformation(DefaultMotorInfo.GOBILDA_312RPM);
//        motor.setVelocity(0);
//        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        FtcDashboard dashboard = FtcDashboard.getInstance();
//        Telemetry dashboardTelemetry = dashboard.getTelemetry();
        Lift motor = new Lift(rawMotor, TeamConstants.LIFT_MIN_INCHES, TeamConstants.LIFT_MAX_INCHES);


        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        double targetPos = 0;
        TimedTimer timer = new TimedTimer(ConfigurableConstants.ALTERNATION_PERIOD/2.0);

        telemetry.addData("TargetPos: ", targetPos);
        telemetry.addData("CurrentPos: ", motor.getCurrentPositionInches());
        telemetry.update(); //Note: Updating telemetry while using initializeRobot() will delete possible warnings

        waitForStart();

        while (opModeIsActive()) {

            if (timer.isDone()) {
                if (targetPos == ConfigurableConstants.VEL_LOW_RPM) {
                    targetPos = ConfigurableConstants.VEL_HIGH_RPM;
                } else if (targetPos == ConfigurableConstants.VEL_HIGH_RPM) {
                    targetPos = ConfigurableConstants.VEL_LOW_RPM;
                } else {
                    targetPos = ConfigurableConstants.VEL_LOW_RPM;
                }
                motor.setPositionInches(targetPos);
                timer.startNewTimer(ConfigurableConstants.ALTERNATION_PERIOD/2.0);
//                motor.setPositionPCoefficient(
//                        ConfigurableConstants.kP
//                );
            }

            telemetry.addData("TargetPos: ", targetPos);
            telemetry.addData("CurrentPos: ", motor.getCurrentPositionInches());
            telemetry.addData("TargetDegs: ", motor.getTargetPosition(AngleUnit.DEGREES));
            telemetry.addData("CurrentDegs: ", motor.getCurrentPosition(AngleUnit.DEGREES));
            telemetry.update();
        }
    }
}
