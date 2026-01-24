package org.firstinspires.ftc.teamcode.SubSystems;

import org.firstinspires.ftc.teamcode.Cogintilities.Prism.PrismColor;
import org.firstinspires.ftc.teamcode.Cogintilities.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Cogintilities.Prism.PrismAnimations;
import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;

public class DataPrism implements TeamConstants {

    GoBildaPrismDriver prism;
    int prevIndex = -2;

    public DataPrism(GoBildaPrismDriver prism) {
        this.prism = prism;
        this.prism.clearAllAnimations();
        this.prism.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
        this.prism.enableDefaultBootArtboard(true);
    }

    public void updateShootColors(int scored, State[] sequence) {
        prevIndex = -2;
        prism.clearAllAnimations();
        if (sequence == null || sequence.length != 3) return;
        PrismColor[] colors = new PrismColor[]{
                stateToPrismColor(sequence[0]),
                stateToPrismColor(sequence[1]),
                stateToPrismColor(sequence[2]),
        };
        if (scored > 0) {
            PrismAnimations.Solid solid = new PrismAnimations.Solid();
            solid.setBrightness(1);
            solid.setStartIndex(0);
            solid.setStopIndex(scored - 1);
            solid.setPrimaryColor(PrismColor.TEAL);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solid);
        }
        if (scored < 9) {
            PrismAnimations.Solid solid = new PrismAnimations.Solid();
            solid.setBrightness(10);
            solid.setStartIndex(scored);
            solid.setStopIndex(scored);
            solid.setPrimaryColor(colors[scored % 3]);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, solid);
        }

        if (scored < 8) {
            PrismAnimations.Solid solid = new PrismAnimations.Solid();
            solid.setBrightness(10);
            solid.setStartIndex(scored + 1);
            solid.setStopIndex(scored + 1);
            solid.setPrimaryColor(colors[(scored + 1) % 3]);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_2, solid);
        }
        if (scored < 7) {
            PrismAnimations.Solid solid = new PrismAnimations.Solid();
            solid.setBrightness(10);
            solid.setStartIndex(scored + 2);
            solid.setStopIndex(scored + 2);
            solid.setPrimaryColor(colors[(scored + 2) % 3]);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_3, solid);
        }
        if (scored < 6) {
            PrismAnimations.Solid solid = new PrismAnimations.Solid();
            solid.setBrightness(1);
            solid.setStartIndex(scored + 3);
            solid.setStopIndex(8);
            solid.setPrimaryColor(PrismColor.RED);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_4, solid);
        }

//        PrismAnimations.Solid solid = new PrismAnimations.Solid();
//        solid.setBrightness(1);
//        solid.setStartIndex(9);
//        solid.setStopIndex(-1);
//        solid.setPrimaryColor(PrismColor.RED);
//        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_5, solid);

    }

    public void bearingColors(double bearing, double tolerance) {
        int indexPosition;
        if (Math.abs(bearing) <= tolerance) {
            indexPosition = -1;
        } else {
            if (bearing < 0) {
                if (Math.abs(bearing) - tolerance < 2) {
                    indexPosition = 7;
                } else if (Math.abs(bearing) - tolerance < 4) {
                    indexPosition = 8;
                } else if (Math.abs(bearing) - tolerance < 6) {
                    indexPosition = 9;
                } else if (Math.abs(bearing) - tolerance < 8) {
                    indexPosition = 10;
                } else {
                    indexPosition = 11;
                }
            } else {
                if (Math.abs(bearing) - tolerance < 2) {
                    indexPosition = 4;
                } else if (Math.abs(bearing) - tolerance < 4) {
                    indexPosition = 3;
                } else if (Math.abs(bearing) - tolerance < 6) {
                    indexPosition = 2;
                } else if (Math.abs(bearing) - tolerance < 8) {
                    indexPosition = 1;
                } else {
                    indexPosition = 0;
                }
            }
        }
        if (indexPosition == prevIndex) return;
        prism.clearAllAnimations();
        PrismAnimations.Solid solid = new PrismAnimations.Solid();
        solid.setBrightness(10);
        solid.setStartIndex(5);
        solid.setStopIndex(6);
        if (indexPosition == -1) {
            solid.setPrimaryColor(PrismColor.TEAL);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solid);
        } else {
            solid.setPrimaryColor(PrismColor.BLUE);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solid);

            PrismAnimations.Solid solid2 = new PrismAnimations.Solid();
            solid2.setBrightness(10);
            solid2.setStartIndex(indexPosition);
            solid2.setStopIndex(indexPosition);
            solid2.setPrimaryColor(PrismColor.ORANGE);
            prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_1, solid2);

        }
        prevIndex = indexPosition;

    }

    public void setColor(PrismColor color) {
        prevIndex = -2;
        PrismAnimations.Solid solid = new PrismAnimations.Solid();
        solid.setBrightness(10);
        solid.setStartIndex(0);
        solid.setStopIndex(12);
        solid.setPrimaryColor(color);
        prism.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, solid);
    }

    public void clear() {
        prevIndex = -2;
        prism.clearAllAnimations();
    }

    public PrismColor stateToPrismColor(State state) {
        switch (state) {
            case PURPLE:
                return PrismColor.PURPLE;
            case GREEN:
                return PrismColor.GREEN;
            case UNKNOWN:
                return PrismColor.RED;
            case NONE:
                return PrismColor.TRANSPARENT;
            default:
                return null;
        }
    }



}