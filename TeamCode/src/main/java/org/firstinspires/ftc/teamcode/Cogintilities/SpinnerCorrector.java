package org.firstinspires.ftc.teamcode.Cogintilities;

public abstract class SpinnerCorrector {

    private static final InterpLUT spinnerILUT = new InterpLUT();
    private static final InterpLUT spinnerILUTInverse = new InterpLUT();

    private static void addData(double in, double out) {
        spinnerILUT.add(in, out);
        spinnerILUTInverse.add(out, in);
    }

    private static void create() {
        spinnerILUT.createLUT();
        spinnerILUTInverse.createLUT();
    }

    static {
        addData(-810, 287-1080);
        addData(-800, 295-1080);
        addData(-790, 303-1080);
        addData(-780, 315-1080);
        addData(-770, 327-1080);
        addData(-760, 340-1080);
        addData(-750, 350-1080);
        addData(-740, 358-1080);
        addData(-730, 9-720);
        addData(-720, 19-720);
        addData(-710, 27-720);
        addData(-700, 45-720);
        addData(-690, 52-720);
        addData(-680, 62-720);
        addData(-670, 70-720);
        addData(-660, 78-720);
        addData(-650, 89-720);
        addData(-640, 98-720);
        addData(-630, 106-720);
        addData(-620, 115-720);
        addData(-610, 121-720);
        addData(-600, 128-720);
        addData(-590, 142-720);
        addData(-580, 155-720);
        addData(-570, 161-720);
        addData(-560, 171-720);
        addData(-550, 184-720);
        addData(-540, 194-720);
        addData(-530, 203-720);
        addData(-520, 212-720);
        addData(-510, 222-720);
        addData(-500, 235-720);
        addData(-490, 242-720);
        addData(-480, 250-720);
        addData(-470, 260-720);
        addData(-460, 273-720);
        addData(-450, 280-720);
        addData(-440, 293-720);
        addData(-430, 305-720);
        addData(-420, 314-720);
        addData(-410, 327-720);
        addData(-400, 336-720);
        addData(-390, 345-720);
        addData(-380, 354-720);
        addData(-370, 5-360);
        addData(-360, 16-360);
        addData(-350, 23-360);
        addData(-340, 33-360);
        addData(-330, 44-360);
        addData(-320, 54-360);
        addData(-310, 66-360);
        addData(-300, 74-360);
        addData(-290, 83-360);
        addData(-280, 92-360);
        addData(-270, 102-360);
        addData(-260, 112-360);
        addData(-250, 122-360);
        addData(-240, 134-360);
        addData(-230, 144-360);
        addData(-220, 153-360);
        addData(-210, 161-360);
        addData(-200, 170-360);
        addData(-190, 181-360);
        addData(-180, 190-360);
        addData(-170, 198-360);
        addData(-160, 207-360);
        addData(-150, 219-360);
        addData(-140, 227-360);
        addData(-130, 238-360);
        addData(-120, 246-360);
        addData(-110, 253-360);
        addData(-100, 265-360);
        addData(-90, 273-360);
        addData(-80, 283-360);
        addData(-70, 292-360);
        addData(-60, 304-360);
        addData(-50, 315-360);
        addData(-40, 323-360);
        addData(-30, 332-360);
        addData(-20, 341-360);
        addData(-10, 251-360);
        addData(0, 0);
        addData(10, 6);
        addData(20, 15);
        addData(30, 28);
        addData(40, 36);
        addData(50, 44);
        addData(60, 52);
        addData(70, 59);
        addData(80, 69);
        addData(90, 77);
        addData(100, 87);
        addData(110, 95);
        addData(120, 105);
        addData(130, 112);
        addData(140, 124);
        addData(150, 129);
        addData(160, 140);
        addData(170, 150);
        addData(180, 161);
        addData(190, 168);
        addData(200, 179);
        addData(210, 186);
        addData(220, 192);
        addData(230, 205);
        addData(240, 213);
        addData(250, 223);
        addData(260, 233);
        addData(270, 243);
        addData(280, 252);
        addData(290, 265);
        addData(300, 272);
        addData(310, 281);
        addData(320, 293);
        addData(330, 302);
        addData(340, 312);
        addData(350, 320);
        addData(360, 331);
        addData(370, 341);
        addData(380, 355);
        addData(390, 360+3);
        addData(400, 360+15);
        addData(410, 360+25);
        addData(420, 360+34);
        addData(430, 360+42);
        addData(440, 360+52);
        addData(450, 360+72);
        addData(460, 360+81);
        addData(470, 360+94);
        addData(480, 360+104);
        addData(490, 360+111);
        addData(500, 360+121);
        addData(510, 360+132);
        addData(520, 360+140);
        addData(530, 360+150);
        addData(540, 360+159);
        addData(550, 360+165);
        addData(560, 360+179);
        addData(570, 360+194);
        addData(580, 360+203);
        addData(590, 360+210);
        addData(600, 360+220);
        addData(610, 360+229);
        addData(620, 360+244);
        addData(630, 360+250);
        addData(640, 360+259);
        addData(650, 360+268);
        addData(660, 360+282);
        addData(670, 360+291);
        addData(680, 360+303);
        addData(690, 360+308);
        addData(700, 360+319);
        addData(710, 360+328);
        addData(720, 360+340);
        addData(730, 360+353);
        addData(740, 720);
        addData(750, 720+10);
        addData(760, 720+19);
        addData(770, 720+25);
        addData(780, 720+33);
        addData(790, 720+48);
        addData(800, 720+58);
        addData(810, 720+72);

        create();

    } //There is definitely a better way of doing this but I did it this way for now
    
    public static double convertSetToActual(double input) {
        return spinnerILUT.get(input);
    }

    public static double convertActualToSet(double input) {
        return spinnerILUTInverse.get(input);
    }



}
