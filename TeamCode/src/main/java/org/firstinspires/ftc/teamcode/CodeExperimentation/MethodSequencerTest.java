package org.firstinspires.ftc.teamcode.CodeExperimentation;

import org.firstinspires.ftc.teamcode.Cogintilities.TeamConstants;
import org.firstinspires.ftc.teamcode.Cogintilities.TimedTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodSequencerTest implements TeamConstants {

    private TimedTimer timer;

    private final List<Runnable> methods;

    private boolean done;

    private double timeDelay = 0;


    public MethodSequencerTest() {
        methods = new ArrayList<>();
        timer = new TimedTimer();
        done = true;
    }

    public void setTimeDelay(double time) {
        timeDelay = time;
    }

    public void addMethods(Runnable... methods) {
        this.methods.addAll(Arrays.asList(methods));
    }

    public void runMethods(Runnable... methods) {
        addMethods(methods);
        start();
    }

//    public <T> void runMethod(Consumer<T> consumer, T parameter) {
//        consumer.accept(parameter);
//    }

    public void runMethod(Runnable runnable) {
        runnable.run();
    }

    public void start() {
        done = false;
    }
    public void stop() {
        done = true;
    }

    public void update() {
        if (!done && timer.isDone()) {
            if (!methods.isEmpty()) {
                methods.remove(0).run();
                timer = new TimedTimer(timeDelay);
            } else {
                done = true;
            }
        }
    }



}
