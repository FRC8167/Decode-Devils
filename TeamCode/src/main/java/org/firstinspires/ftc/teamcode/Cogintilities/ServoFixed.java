package org.firstinspires.ftc.teamcode.Cogintilities;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.ServoType;

/**
 * Instances of Servo interface provide access to servo hardware devices.
 */
@ServoType(flavor = ServoFlavor.STANDARD, xmlTag = "ServoFixed")
@DeviceProperties(name = "@string/configTypeServoFixed", xmlTag = "ServoFixed")
@ServoType(flavor = ServoFlavor.STANDARD, usPulseLower = 500, usPulseUpper = 2500, xmlTag = "ServoFixedFullRange")
@DeviceProperties(name = "@string/configTypeServoFixedFullRange", xmlTag = "ServoFixedFullRange", defaultDevice = false)
public interface ServoFixed extends Servo {

}
