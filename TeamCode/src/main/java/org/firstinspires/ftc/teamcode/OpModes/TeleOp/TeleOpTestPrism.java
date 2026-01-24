package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Cogintilities.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.SubSystems.DataPrism;

@TeleOp(name="TeleOpTestPrism", group="Testing")
public class TeleOpTestPrism extends LinearOpMode implements TeamConstants {

    @Override
    public void runOpMode() throws InterruptedException {


        DataPrism dataPrism = new DataPrism(hardwareMap.get(GoBildaPrismDriver.class,"prism"));
        State[] states = STATES_PGP;


        int numberScored = 0;



        waitForStart();
        dataPrism.updateShootColors(numberScored, TeamConstants.STATES_GPP);

        while (opModeIsActive()) {
            if (gamepad1.dpadUpWasPressed() && numberScored < 9) {
                numberScored += 1;
                dataPrism.updateShootColors(numberScored, TeamConstants.STATES_GPP);
            } else if (gamepad1.dpadDownWasPressed() && numberScored > 0) {
                numberScored -= 1;
                dataPrism.updateShootColors(numberScored, TeamConstants.STATES_GPP);
            }

            if (gamepad1.aWasPressed()) {
                dataPrism.clear();
            }
            telemetry.addData("Scored: ", numberScored);
            telemetry.update();
        }
    }

//    public void updateColors(GoBildaPrismDriver prism, int scored) {
//        prism.clearAllAnimations();
//        if (scored != 0) {
//            PrismAnimations.Solid solid = new PrismAnimations.Solid();
//            solid.setBrightness(50);
//            solid.setStartIndex(0);
//            solid.setStopIndex(scored - 1);
//            solid.setPrimaryColor(PrismColor.WHITE);
//            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solid);
//        }
//        if (scored < 9) {
//            PrismAnimations.Solid solid = new PrismAnimations.Solid();
//            solid.setBrightness(50);
//            solid.setStartIndex(scored);
//            solid.setStopIndex(scored);
//            solid.setPrimaryColor(PrismColor.GREEN);
//            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, solid);
//        }
//
//        if (scored < 8) {
//            PrismAnimations.Solid solid = new PrismAnimations.Solid();
//            solid.setBrightness(50);
//            solid.setStartIndex(scored + 1);
//            solid.setStopIndex(scored + 1);
//            solid.setPrimaryColor(PrismColor.PURPLE);
//            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, solid);
//        }
//        if (scored < 7) {
//            PrismAnimations.Solid solid = new PrismAnimations.Solid();
//            solid.setBrightness(50);
//            solid.setStartIndex(scored + 2);
//            solid.setStopIndex(scored + 2);
//            solid.setPrimaryColor(PrismColor.PURPLE);
//            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, solid);
//        }
//    }
}
