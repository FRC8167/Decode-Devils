package org.firstinspires.ftc.teamcode.Cogintilities;

import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.ServoType;

@ServoType(flavor = ServoFlavor.STANDARD, usPulseLower = 500, usPulseUpper = 2500, xmlTag = "ServoFullRange")
@DeviceProperties(name = "@string/configTypeServoTest", xmlTag = "ServoTest", builtIn = true, defaultDevice = false)
public interface ServoTest {
}
