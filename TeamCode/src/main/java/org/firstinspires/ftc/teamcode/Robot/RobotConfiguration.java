package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.SpinnerSequencer;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.SubSystems.ColorDetection;
import org.firstinspires.ftc.teamcode.SubSystems.Dropper;
import org.firstinspires.ftc.teamcode.SubSystems.Intake;
import org.firstinspires.ftc.teamcode.SubSystems.Shooter;
import org.firstinspires.ftc.teamcode.SubSystems.LightRGB;
import org.firstinspires.ftc.teamcode.SubSystems.MecanumDriveBasic;
import org.firstinspires.ftc.teamcode.SubSystems.SpinStatesSingleton;
import org.firstinspires.ftc.teamcode.SubSystems.Spindexer;
import org.firstinspires.ftc.teamcode.SubSystems.Spinner;
import org.firstinspires.ftc.teamcode.SubSystems.Vision;

import java.util.List;
import java.util.Locale;

/**
 * This class should be used to define all the subsystem modules and assign the hardware used in
 * those modules. The keyword 'abstract' indicates that an object of this class cannot be created
 * directly in an opMode. Instead, a class must be created that extends or inherits from this class.
 * In our case, all OpModes will extend RobotConfig. This allows the opMode to use all the
 * variables, objects and methods defined below. It also will create an OpMode that uses the SDK's
 * LinearOpMode framework as this class itself extends the LinearOpMode class.
 */

public abstract class RobotConfiguration extends LinearOpMode implements TeamConstants{

    /*------------ Public Class Variables - Frowned Upon ------------*/




    /*------------- Private Class Variables - Preferred -------------*/
    protected enum AllianceColor { RED, BLUE }
    static protected AllianceColor alliance;
    static List<LynxModule> ctrlHubs;
    static protected State[] ArtifactSequence = null;
    protected Telemetry telemetry;


    /*----------- Define all Module Classes (SubSystems) ------------*/
    static protected MecanumDriveBasic drive;
    static protected MecanumDrive autoDrive;
    static protected Shooter shooter;
    static protected Intake intake;
//    static protected Fork fork;
    static protected SpinStatesSingleton spinStates;
    static protected ColorDetection colorDetection;
    static protected Spindexer spindexer;
    static protected SpinnerSequencer spinnerSequencer;
    static protected LightRGB lightRGB;

    static protected Vision vision;
    static protected Vision visionPos;

    static private Spinner spinner;
    static private Dropper dropper;



    /*---------------------- Vision Objects -------------------------*/



    /**
     * initializeRobot:
     * Initialize robot with a specified start pose (used by Road Runner. This function should be
     * called immediately in the OpMode's runOpMode function. A null value error will result if you
     * try to use any devices connected to the control hub that have not been initialized.  This
     * function creates the Hardware Map and the module objects that use these devices.
     *
     * @throws InterruptedException
     */
    public void initializeRobot(Pose2d startPos, boolean moveServos) throws InterruptedException {

        /* Find all Control Hubs and Set Sensor Bulk Read Mode to AUTO */
        ctrlHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : ctrlHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        /* ******************* Define Hardware Map Here ******************** */
        DcMotorEx driveMotorLF = hardwareMap.get(DcMotorEx.class, "LeftFront");
        DcMotorEx driveMotorLR = hardwareMap.get(DcMotorEx.class, "LeftRear");
        DcMotorEx driveMotorRF = hardwareMap.get(DcMotorEx.class, "RightFront");
        DcMotorEx driveMotorRR = hardwareMap.get(DcMotorEx.class, "RightRear");

        DcMotorEx shooterMotor = hardwareMap.get(DcMotorEx.class, "Shooter");
//        DcMotorEx shooterMotor = new BetterMotor(hardwareMap, "Shooter");

        CRServo intakeServo = hardwareMap.get(CRServo.class, "intakeServo");

        RevColorSensorV3 colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");
        Servo spinServo = hardwareMap.get(Servo.class, "spinServo");
        Servo dropServo = hardwareMap.get(Servo.class, "dropServo");
//        Servo forkServo = hardwareMap.get(Servo.class, "forkServo");
        Servo servoRGB  = hardwareMap.get(Servo.class, "servoRGB");

        WebcamName webcam = hardwareMap.get(WebcamName.class, "Webcam1");
        WebcamName webcam2 = hardwareMap.get(WebcamName.class, "Webcam2");

        /* Create an object of every module/subsystem needed for both autonomous and teleOp modes. */
        drive            = new MecanumDriveBasic(driveMotorLF, driveMotorLR, driveMotorRF, driveMotorRR);
        autoDrive        = new MecanumDrive(hardwareMap, startPos);
        shooter          = new Shooter(shooterMotor);
        intake           = new Intake(intakeServo);
        colorDetection   = new ColorDetection(colorSensor);
        spinStates       = SpinStatesSingleton.getInstance();
        spinner          = new Spinner(spinServo, SPINNER_INIT_POS+SPINNER_OFFSET, SPINNER_MIN, SPINNER_MAX, moveServos);
        dropper          = new Dropper(dropServo, DROPPER_INIT_POS, DROPPER_MIN, DROPPER_MAX, moveServos);
//        fork             = new Fork   (forkServo, FORK_INIT_POS, FORK_MIN, FORK_MAX, moveServos);
        lightRGB         = new LightRGB(servoRGB, LIGHT_INIT_POS, LIGHT_MIN, LIGHT_MAX);
        spindexer        = new Spindexer(spinner, dropper, spinStates, colorDetection);
        spinnerSequencer = new SpinnerSequencer(spindexer, shooter, spinStates);

        if (webcam.isAttached()) {
            vision = new Vision(webcam);
            vision.enableAprilTagDetection();
        }

        if (isStopRequested()) return;

        if (webcam2.isAttached()) {
            visionPos = new Vision(webcam2);
            visionPos.enableAprilTagDetection();
        }

        if (isStopRequested()) return;

// Default to standard telemetry as a safe starting point.
        this.telemetry = super.telemetry;
        boolean dashboardInitialized = false;

        try {
            FtcDashboard dashboard = FtcDashboard.getInstance();

            if (dashboard != null) {
                Telemetry dashboardTelemetry = dashboard.getTelemetry();
                if (dashboardTelemetry != null) {
                    this.telemetry = new MultipleTelemetry(super.telemetry, dashboardTelemetry);
                    dashboardInitialized = true;
                } else {
                    super.telemetry.addData("Warning", "FTC Dashboard connected, but telemetry is null.");
                }
            } else {
                super.telemetry.addData("Info", "FTC Dashboard not connected. Using standard telemetry.");
            }
        } catch (Exception e) {
            super.telemetry.addData("Error", "FTC Dashboard failed to initialize: " + e.getMessage());
        }

        if (dashboardInitialized) {
            this.telemetry.addData("Status", "FTC Dashboard telemetry is active.");
        } else {
            this.telemetry.addData("Status", "Standard telemetry is active.");
        }

        this.telemetry.update();

    }

    /**
     * runOpMode must be Overridden in all OpModes
     * This is a requirement from the LinearOpMode class in the SDK
     */
    @Override
    public abstract void runOpMode() throws InterruptedException;


    /* ********* Setters, Getters, Utility and Helper Functions ********** */
    protected void setAlliance(AllianceColor color){ alliance = color; }
    protected static AllianceColor getAlliance(){ return alliance; }

//    public String hubA() {
//        double currentmA = 0;
//        for (LynxModule hub : ctrlHubs) {
//            currentmA += hub.getCurrent(CurrentUnit.AMPS);
//        }
//        return String.format(Locale.getDefault(), "%.3f mA", currentmA);
//    }

    public static String ctrlHubV() {
        return String.format(Locale.getDefault(), "%.2f", ctrlHubs.get(0).getInputVoltage(VoltageUnit.VOLTS));
    }


    public static String expHubV() {
        return String.format(Locale.getDefault(), "%.2f", ctrlHubs.get(1).getInputVoltage(VoltageUnit.VOLTS));
    }


}
