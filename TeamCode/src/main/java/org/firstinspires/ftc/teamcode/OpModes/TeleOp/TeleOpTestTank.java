package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Cogintilities.Color;
import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.Cogintilities.Prism.PrismColor;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;
import org.firstinspires.ftc.teamcode.Robot.RobotConfiguration;
import org.firstinspires.ftc.teamcode.SubSystems.Motor1D;

@TeleOp(name="TeleOpTestTank", group="Testing")
public class TeleOpTestTank extends RobotConfiguration implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {

        Motor1D tankL = new Motor1D(hardwareMap.get(DcMotorEx.class, "tankL"), Motor1D.Mode.POWER_BASED, DefaultMotorInfo.GOBILDA_1150RPM);
        Motor1D tankR = new Motor1D(hardwareMap.get(DcMotorEx.class, "tankR"), Motor1D.Mode.POWER_BASED, DefaultMotorInfo.GOBILDA_1150RPM);

        waitForStart();

        while (opModeIsActive()) {

            tankL.setRawPower(Range.clip(gamepad1.left_stick_y - gamepad1.right_stick_x, -1 , 1));
            tankR.setRawPower(Range.clip(-gamepad1.left_stick_y - gamepad1.right_stick_x, -1 , 1));



        }
    }
}
