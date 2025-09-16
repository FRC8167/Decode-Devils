package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

//@Disabled
@TeleOp(name="MainTeleOp", group="Competition")
public class MainTeleOp extends RobotConfiguration {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                intake.setMotorPower(TeamConstants.INTAKE_POWER);
            } else {
                intake.setMotorPower(0.0); // stops the intake
            }
            telemetry.addData("Red%", test_color.getNormalizedColors().red*100);
            telemetry.addData("Green%", test_color.getNormalizedColors().green*100);
            telemetry.addData("Blue%", test_color.getNormalizedColors().blue*100);
            telemetry.update();
        }
    }
}
