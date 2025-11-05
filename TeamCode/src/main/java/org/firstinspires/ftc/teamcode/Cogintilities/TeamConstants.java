package org.firstinspires.ftc.teamcode.Cogintilities;

public interface TeamConstants {

    enum State {
        GREEN("Green", 'G'),
        PURPLE("Purple", 'P'),
        UNKNOWN("Unknown", 'U'),
        NONE("None", 'U');
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

    } // Slot states for spindexer

    State[] STATES_GPP = {State.GREEN, State.PURPLE, State.PURPLE};
    State[] STATES_PGP = {State.PURPLE, State.GREEN, State.PURPLE};
    State[] STATES_PPG = {State.PURPLE, State.PURPLE, State.GREEN};

    double INTAKE_POWER = 0.5;

    double SPINNER_MIN = 0.0;
    double SPINNER_MAX = 1.0;
    double SPINNER_INIT_POS = 0.5;
    double SPINNER_RANGE = 1620.0; //degrees, 1620.0 estimated should be 1800 (1642.5 calculated using 600-2400 servo)

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

    double LIGHT_INIT_POS = 0;
    double LIGHT_MIN = 0;
    double LIGHT_MAX = 1;

    double DROP_TIMER = 1; // time dropper stays open in seconds

    double SEQUENCER_TIMER = 1.2; // time until next state ran in seconds (Original: 0.8)
    double SEQUENCER_TIMER_INITIAL = 1.8; // (Original: 1.2)
    double SEQUENCER_TIMER_WIGGLE = 0.4; // Used for both modes
    double SEQUENCER_TIMER_WIGGLE_BACK = 1; // Used for scan mode
    double SEQUENCER_TIMER_WAIT = 0.5; // Used for scan mode
    double SEQUENCER_TIMER_RECENTER = 2; // Used for dual mode TODO: Modify to use current pos to calculate time (1.5s per rotation)
    double SEQUENCER_WIGGLE_DEGREES = 15;
    int SEQUENCER_SCAN_CYCLE_ATTEMPTS = 15; //Attempts before adjusting
    int SEQUENCER_SCAN_MAX_ATTEMPTS = 3;
}
