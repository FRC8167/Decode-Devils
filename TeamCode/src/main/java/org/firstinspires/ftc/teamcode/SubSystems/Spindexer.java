package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.RobotConfiguration.State;

public class Spindexer implements TeamConstants {
    Servo spinServo;



    public Spindexer(Servo spinServo) {
        this.spinServo = spinServo;
    }


}
