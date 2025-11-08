package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class Dropper extends Servo1D implements TeamConstants {
    private double currentLengthMillimeters;
    private double currentExtensionMillimeters; // length is updated based on extension

    public Dropper(Servo servo, double initPos, double min, double max, boolean moveOnInit) {
        super(servo, initPos, min, max, moveOnInit);
        servo.scaleRange(DROPPER_SERVO_RANGE[0], DROPPER_SERVO_RANGE[1]);
        currentExtensionMillimeters = servoPos() * DROPPER_RANGE;
        update();

    }

    public void update() {
        currentLengthMillimeters = currentExtensionMillimeters + DROPPER_MIN_MILLIMETERS;
        setPosition(currentExtensionMillimeters / DROPPER_RANGE);
    }

    public void setCurrentLengthMillimeters(double lengthMillimeters) {
        currentExtensionMillimeters = lengthMillimeters - DROPPER_MIN_MILLIMETERS;
        update();
    }

    public void setCurrentExtensionMillimeters(double extensionMillimeters) {
        currentExtensionMillimeters = extensionMillimeters;
        update();
    }

    public double getCurrentLengthMillimeters() {
        return currentLengthMillimeters;
    }

    public double getCurrentExtensionMillimeters() {
        return currentExtensionMillimeters;
    }

    public void open() {
        setCurrentLengthMillimeters(DROPPER_MIN_MILLIMETERS);
    }

    public void close() {
        setCurrentLengthMillimeters(DROPPER_MAX_MILLIMETERS);
    }
}