package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name="Display Name") //, preselectTeleOp = "TeleOpMode", group="Name of Group")
public class AutoOp_Template extends RobotConfiguration {


    @Override
    public void runOpMode() throws InterruptedException {

        initializeRobot();

        waitForStart();

//        while (opModeIsActive()) {
//        }
    }

}
