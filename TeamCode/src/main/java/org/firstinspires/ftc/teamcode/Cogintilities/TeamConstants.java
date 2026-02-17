package org.firstinspires.ftc.teamcode.Cogintilities;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public interface TeamConstants {

    enum State {
        GREEN("Green", 'G'),
        PURPLE("Purple", 'P'),
        UNKNOWN("Unknown", 'U'),
        NONE("None", 'N');
        private final String color;
        private final char character;

        State(String color, char character) {
            this.color = color;
            this.character = character;
        };
        public String getColor() {
            return color;
        }
        public char getCharacter() {
            return character;
        }

        public static String convertStatesToInitials(State... states) {
            if (states == null) {
                return "";
            } else {
                StringBuilder string = new StringBuilder();
                for (State state : states) {
                    string.append(state.getCharacter());
                }
                return string.toString();
            }
        }

    } // Slot states for spindexer

    State[] STATES_GPP = {State.GREEN, State.PURPLE, State.PURPLE};
    State[] STATES_PGP = {State.PURPLE, State.GREEN, State.PURPLE};
    State[] STATES_PPG = {State.PURPLE, State.PURPLE, State.GREEN};

    PIDFCoefficients mecanumPIDF = new PIDFCoefficients(0.012, 0, 0.00075, 0.07);

    double SPINNER_MIN = 0.0;
    double SPINNER_MAX = 1.0;
    double SPINNER_INIT_POS = 0.5;
    double SPINNER_RANGE = 1620.0; //degrees, 1620.0 estimated should be 1800 (1642.5 calculated using 600-2400 servo)
    double SPINNER_OFFSET = -7 / SPINNER_RANGE; //-3
    double SPINNER_SPEED = 240; //degrees per second, 40 rpm (measured underestimation, spec defined speed: 300/50rpm)
    double SPINNER_GRACE_TIME = 0.05; //additional time for servo to reach target position (insures it is done for small rotations)
    double SPINNER_WIGGLE_MANUAL = 10;

    double EXP_SPINNER_TICKS_PER_REV = 288;

    double DROPPER_MIN = 0;
    double DROPPER_MAX = 1;
    double DROPPER_INIT_POS = 1;
    double DROPPER_MIN_MILLIMETERS = 82; //82mm contracted length (model calculated at 81.9801mm for open)
    double DROPPER_MAX_MILLIMETERS = 112; //112mm max extension (model calculated at 111.62414mm for closed)
    double DROPPER_RANGE = 30; // 30mm stroke length
    double[] DROPPER_SERVO_RANGE = {0.16, 0.83}; //measured
    // 0.275,0.725 (supposed)
    // 0.25,0.75 (supposed w/ servo range error)
    // 0.03/0.16,0.97/0.83 (measured) (valid/value before move)

    double SHOOTER_POWER = 0.8; //TODO: Replace with velocity based code
    double SHOTTER_VELOCITY = 4300;
    double SHOOTER_TICKS_PER_REV = 28;
    PIDFCoefficients SHOOTER_VEL_PIDF_COEFFS = new PIDFCoefficients(
            26,
            0,
            14,
            12.7
    );

    MotorInformation LIFT_MOTOR_INFO = new GearedMotorInfo(
            DefaultMotorInfo.GOBILDA_312RPM,
            48/80.0,
            true
    );
    
    PIDFCoefficients LIFT_VEL_PIDF = new PIDFCoefficients(
            1,
            0,
            0,
            12
    );
    double LIFT_POS_KP = 2.5;
    double LIFT_MILLIS_PER_REV = 8;
    //8mm of linear distance per rotation
    double LIFT_MIN_INCHES = 0;
    double LIFT_MAX_INCHES = 9;

    double INTAKE_POWER_FORWARD = 1;
    double INTAKE_POWER_NEUTRAL = 0;
    double INTAKE_POWER_BACKWARD = -0.5;

    double FORK_MIN = 0;
    double FORK_MAX = 1; //TODO: Determine true value
    double FORK_INIT_POS = 0;


    double LIGHT_INIT_POS = 0;
    double LIGHT_MIN = 0;
    double LIGHT_MAX = 1;

    double DROP_TIMER = 1; // time dropper stays open in seconds

    double SEQUENCER_TIMER = 1.8; // time until next state ran in seconds (Original: 1.2)
    double SEQUENCER_TIMER_INITIAL = 1.8; // (Original: 1.2)
    double SEQUENCER_TIMER_WIGGLE = 0.4; // Used for both modes
    double SEQUENCER_TIMER_WIGGLE_BACK = 1; // Used for scan mode
    double SEQUENCER_TIMER_WAIT = 0.5; // Used for scan mode
    double SEQUENCER_TIMER_RECENTER = 2; // Used for dual mode
    double SEQUENCER_WIGGLE_DEGREES = 15;
    int SEQUENCER_SCAN_CYCLE_ATTEMPTS = 15; //Attempts before adjusting
    int SEQUENCER_SCAN_MAX_ATTEMPTS = 3; //Scan cycles before excluding (excludes for current scan only)

    double LIME_VISION_STALENESS_TOL = 200; //Staleness in milliseconds
    double LIME_VISION_POSE_MEDIATING_STALENESS_TOL = 0.5; //Staleness in seconds for which older poses will be removed
    double LIME_VISION_POSE_MEDIATING_MIN_POSES = 3;

    Position BLUE_GOAL_TARGET = new Position(DistanceUnit.INCH, -60.699807, -57.531366, 38.75, 0);
    Position RED_GOAL_TARGET = new Position(DistanceUnit.INCH, -60.699807, 57.531366, 38.75, 0);
    //Note: Coordinate system is looking from red side towards field with 0 deg to the right

    double FIELD_TILE_LENGTH = 23.562500;
    double FIELD_BACK_EDGE_DISTANCE = FIELD_TILE_LENGTH *3;
    Position FAR_LAUNCH_ZONE_1 = new Position(DistanceUnit.INCH, -FIELD_BACK_EDGE_DISTANCE, FIELD_TILE_LENGTH, 0 ,0);
    Position FAR_LAUNCH_ZONE_2 = new Position(DistanceUnit.INCH, -(FIELD_BACK_EDGE_DISTANCE - FIELD_TILE_LENGTH), 0, 0 ,0);
    Position FAR_LAUNCH_ZONE_3 = new Position(DistanceUnit.INCH, -FIELD_BACK_EDGE_DISTANCE, -FIELD_TILE_LENGTH, 0 ,0);
    Position CLOSE_LAUNCH_ZONE_1 = new Position(DistanceUnit.INCH, FIELD_BACK_EDGE_DISTANCE, FIELD_BACK_EDGE_DISTANCE, 0 ,0);
    Position CLOSE_LAUNCH_ZONE_2 = new Position(DistanceUnit.INCH, FIELD_BACK_EDGE_DISTANCE, -FIELD_BACK_EDGE_DISTANCE, 0 ,0);
    Position CLOSE_LAUNCH_ZONE_3 = new Position(DistanceUnit.INCH, 0, 0, 0 ,0);

    //TODO: Verify these values
    double ROBOT_WIDTH = 16.5;
    double ROBOT_LENGTH = 16.5;

    double DISTANCE_SENSOR_CLEAR_SPACE_NEEDED = 6; //Space needed to shoot in Inches
    double DISTANCE_SENSOR_FWD_OFFSET = 6; //Distance of sensor from center of rotation in Inches TODO: Determine Actual Value


}
