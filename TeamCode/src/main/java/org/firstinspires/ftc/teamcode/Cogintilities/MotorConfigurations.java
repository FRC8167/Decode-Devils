package org.firstinspires.ftc.teamcode.Cogintilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public enum MotorConfigurations {
    GOBILDA_6000RPM(28,     6000, 1   ),
    GOBILDA_1620RPM(103.8,  1620, 3.7 ),
    GOBILDA_1150RPM(145.1,  1150, 5.2 ),
    GOBILDA_435RPM (384.5,  435,  13.7),
    GOBILDA_312RPM (537.7,  312,  19.2),
    GOBILDA_223RPM (751.8,  223,  26.9),
    GOBILDA_117RPM (1425.1, 117,  50.9),
    GOBILDA_84RPM  (1993.6, 84,   71.2),
    GOBILDA_60RPM  (2786.2, 60,   99.5),
    GOBILDA_43RPM  (3895.9, 43,   139 ),
    GOBILDA_30RPM  (5281.1, 30,   188 ),

    REV_HEX_125RPM (288,    125,  72);

    private final double ticksPerRev;
    private final double maxRPM;
    private final double gearing;

    MotorConfigurations(double ticksPerRev, double maxRPM, double gearing) {
        this.ticksPerRev = ticksPerRev;
        this.maxRPM = maxRPM;
        this.gearing = gearing;
    }

    public double getTicksPerRev() {
        return ticksPerRev;
    }

    public double getMaxRPM() {
        return maxRPM;
    }

    public double getGearing() {
        return gearing;
    }

    public void configureMotor(DcMotor motor) {
        configureMotor(motor, this);
    }

    public static void configureMotor(DcMotor motor, MotorConfigurations motorConfig) {
        MotorConfigurationType newConfig = motor.getMotorType().clone();
        newConfig.setTicksPerRev(motorConfig.getTicksPerRev());
        newConfig.setMaxRPM(motorConfig.getMaxRPM());
        newConfig.setGearing(motorConfig.getGearing());
        motor.setMotorType(newConfig);
    }
}
