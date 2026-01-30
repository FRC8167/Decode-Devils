package org.firstinspires.ftc.teamcode.Cogintilities;

import org.firstinspires.ftc.teamcode.Cogintilities.Other.InterpLUT;

public final class VariableShooterLookup {
    private VariableShooterLookup() {}

    private static final InterpLUT shooterILUT = new InterpLUT();

    private static void addData(double in, double out) {
        shooterILUT.add(in, out);
    }

    private static void create() {
        shooterILUT.createLUT();
    }

    static {
        addData(48.5, 3600);
        addData(53.5,3500);
        addData(59, 3450);
        addData(64.5, 3500);
        addData(84.01135, 3650);
        addData(97, 3800);
        addData(121.5474, 4300);
        addData(140, 4500);
        addData(172, 4900);
        create();

    }

    public static double getVelocityByLookupDistance(double distance) {
        if (distance < 48.5) return 3600;
        if (distance > 172) return 4900;
        return shooterILUT.get(distance);
    }

//    public static double getVelocityByCubicDistance(double distance) {
//        return -0.00237692*Math.pow(distance,3) + 0.835932*Math.pow(distance,2) - 77.95287*distance+5669.57898;
//    }

}
