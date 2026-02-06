package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.BetterMotor;
import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.SubSystems.Lift;

@TeleOp(name="TeleOpTestLift", group="Testing")
public class TeleOpTestLift extends LinearOpMode implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "Lift");
        Lift lift = new Lift(motor, LIFT_MIN_INCHES, LIFT_MAX_INCHES);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.aWasPressed()) {
                lift.setPositionInches(0);
            } else if (gamepad1.bWasPressed()) {
                lift.setPositionInches(5);
            } else if (gamepad1.xWasPressed()) {
                lift.setPositionInches(10);
            }

            telemetry.addData("TargetPos: ", lift.getTargetPositionInches());
            telemetry.addData("CurrentPos: ", lift.getCurrentPositionInches());
        }
    }
}
