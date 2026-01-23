package org.firstinspires.ftc.teamcode.Cogintilities;

public class TimedTimer {
    private double initialTime;
    private double duration;

    public TimedTimer(double timeInSeconds) {
        initialTime = System.currentTimeMillis();
        duration = timeInSeconds*1000;
    }

    public TimedTimer() {
        initialTime = System.currentTimeMillis();
        duration = 0;
    }

    public void startNewTimer(double timeInSeconds) {
        initialTime = System.currentTimeMillis();
        duration = timeInSeconds*1000;
    }

    public void reset() {
        initialTime = System.currentTimeMillis();
    }

    private double time() {
        return System.currentTimeMillis();
    }



    public boolean isDone() {
        return (initialTime + duration <= time());
    }

    public double getRemainingTime() {
        return Math.max(0, (initialTime + duration) - time()) / 1000.0;
    }

    public double getProportionCompleted() {
        if (duration == 0) return -1;
        return (duration - (Math.max(0, (initialTime + duration) - time())))/duration;
    }

    public double getElapsedTime() {
        return (initialTime - time()) / 1000.0;
    }

}
