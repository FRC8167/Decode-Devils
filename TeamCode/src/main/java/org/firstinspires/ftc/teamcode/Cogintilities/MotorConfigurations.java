package org.firstinspires.ftc.teamcode.Cogintilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public enum MotorConfigurations {
    GOBILDA_6000RPM(20,     6000),
    GOBILDA_1620RPM(103.8,  1620),
    GOBILDA_1150RPM(145.1,  1150),
    GOBILDA_435RPM (384.5,  435),
    GOBILDA_312RPM (537.7,  312),
    GOBILDA_223RPM (751.8,  223),
    GOBILDA_117RPM (1425.1, 117),
    GOBILDA_84RPM  (1993.6, 84),
    GOBILDA_60RPM  (2786.2, 60),
    GOBILDA_43RPM  (3895.9, 43),
    GOBILDA_30RPM  (5281.1, 30);
    private final double ticksPerRev;
    private final double maxRPM;

    MotorConfigurations(double ticksPerRev, double maxRPM) {
        this.ticksPerRev = ticksPerRev;
        this.maxRPM = maxRPM;
    }

    public double getTicksPerRev() {
        return ticksPerRev;
    }

    public double getMaxRPM() {
        return maxRPM;
    }

    public void configureMotor(DcMotor motor) {
        configureMotor(motor, this);
    }

    public static void configureMotor(DcMotor motor, MotorConfigurations motorConfig) {
        MotorConfigurationType newConfig = motor.getMotorType().clone();
        newConfig.setTicksPerRev(motorConfig.getTicksPerRev());
        newConfig.setMaxRPM(motorConfig.getMaxRPM());
        motor.setMotorType(newConfig);
    }
}
