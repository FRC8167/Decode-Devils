package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.teamcode.Cogintilities.SpinnerSequencer;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.SubSystems.ColorDetection;
import org.firstinspires.ftc.teamcode.SubSystems.Dropper;
import org.firstinspires.ftc.teamcode.SubSystems.Intake;
import org.firstinspires.ftc.teamcode.SubSystems.LightRGB;
import org.firstinspires.ftc.teamcode.SubSystems.MecanumDriveSingleton;
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
    public enum AllianceColor { RED, BLUE }

    public State[] ArtifactSequence = null;


    /*------------- Private Class Variables - Preferred -------------*/
    static AllianceColor alliance;
    static List<LynxModule> ctrlHubs;


    /*----------- Define all Module Classes (SubSystems) ------------*/
    protected MecanumDriveSingleton drive;
    static protected Intake intake;
    static protected SpinStatesSingleton spinStates;
    static protected ColorDetection colorDetection;
    static protected Spindexer spindexer;
    static protected SpinnerSequencer spinnerSequencer;
    static protected LightRGB lightRGB;

    static protected Vision vision;

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
    public void initializeRobot(boolean moveServos) throws InterruptedException {

        /* Find all Control Hubs and Set Sensor Bulk Read Mode to AUTO */
        ctrlHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : ctrlHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        /* ******************* Define Hardware Map Here ******************** */
//        DcMotorEx driveMotorLF = hardwareMap.get(DcMotorEx.class, "LeftFront");
//        DcMotorEx driveMotorLR = hardwareMap.get(DcMotorEx.class, "LeftRear");
//        DcMotorEx driveMotorRF = hardwareMap.get(DcMotorEx.class, "RightFront");
//        DcMotorEx driveMotorRR = hardwareMap.get(DcMotorEx.class, "RightRear");

        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "Intake");


        RevColorSensorV3 colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");
        Servo spinServo = hardwareMap.get(Servo.class, "spinServo");
        Servo dropServo = hardwareMap.get(Servo.class, "dropServo");
        Servo servoRGB  = hardwareMap.get(Servo.class, "servoRGB");

        WebcamName webcam = hardwareMap.get(WebcamName.class, "Webcam1");

        /* Create an object of every module/subsystem needed for both autonomous and teleOp modes. */
//        drive = MecanumDriveSingleton.getInstance(driveMotorLF, driveMotorLR, driveMotorRF, driveMotorRR);
        intake = new Intake(intakeMotor);
        colorDetection = new ColorDetection(colorSensor);
        spinStates = SpinStatesSingleton.getInstance();
        spinner = new Spinner(spinServo, SPINNER_INIT_POS, SPINNER_MIN, SPINNER_MAX, moveServos);
        dropper = new Dropper(dropServo, DROPPER_INIT_POS, DROPPER_MIN, DROPPER_MAX, moveServos);
        lightRGB = new LightRGB(servoRGB, LIGHT_INIT_POS, LIGHT_MIN, LIGHT_MAX);
        spindexer = new Spindexer(spinner, dropper, spinStates, colorDetection);
        spinnerSequencer = new SpinnerSequencer(spindexer, spinStates);

        vision = new Vision(webcam);
        vision.enableAprilTagDetection();

    }

    /**
     * runOpMode must be Overridden in all OpModes
     * This is a requirement from the LinearOpMode class in the SDK
     */
    @Override
    public abstract void runOpMode() throws InterruptedException;


    /* ********* Setters, Getters, Utility and Helper Functions ********** */
    public void setAlliance(AllianceColor color){ alliance = color; }
    public static AllianceColor getAlliance(){ return alliance; }

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
