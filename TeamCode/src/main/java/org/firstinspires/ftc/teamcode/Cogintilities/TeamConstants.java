package org.firstinspires.ftc.teamcode.Cogintilities;

public interface TeamConstants {

    enum State {Green, Purple, Unknown, None} // Slot states for spindexer

    double INTAKE_POWER = 0.5;

    double SPINNER_MIN = 0.0;
    double SPINNER_MAX = 1.0;
    double SPINNER_INIT_POS = 0.5;
    double SPINNER_RANGE = 1620.0; //degrees, should be 1800

    double DROPPER_MIN = 0;
    double DROPPER_MAX = 1;
    double DROPPER_INIT_POS = 1;
    double DROPPER_MIN_MILLIMETERS = 82; //82mm contracted length (model calculated at 81.9801mm for open)
    double DROPPER_MAX_MILLIMETERS = 112; //112mm max extension (model calculated at 111.62414mm for closed)
    double DROPPER_RANGE = 30; // 30mm stroke length
    double[] DROPPER_SERVO_RANGE = {0.16, 0.83};
    // 0.275,0.725 (supposed)
    // 0.03/0.16,0.97/0.83 (measured) (valid/value before move)

    double DROP_TIMER = 1; // time dropper stays open in seconds
}
