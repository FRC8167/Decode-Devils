package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name="TeleOpTemplate", group="Competition")
public class TeleOp_Template extends RobotConfiguration {

    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();

        waitForStart();

        while (opModeIsActive()) {

        }
    }
}
