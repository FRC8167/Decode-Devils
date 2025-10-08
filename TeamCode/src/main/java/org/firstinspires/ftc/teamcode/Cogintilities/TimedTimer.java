package org.firstinspires.ftc.teamcode.Cogintilities;

public class TimedTimer {
    private double initialTime;
    private final double duration;

    public TimedTimer(double timeInSeconds) {
        initialTime = System.currentTimeMillis();
        duration = timeInSeconds*1000;
    }

    public TimedTimer() {
        initialTime = System.currentTimeMillis();
        duration = 0;
    }

    public void reset() {
        initialTime = System.currentTimeMillis();
    }

    public boolean isDone() {
        return (initialTime + duration <= System.currentTimeMillis());
    }

    public double getRemainingTime() {
        return Math.max(0, (initialTime + duration) - System.currentTimeMillis());
    }

    public double getElapsedTime() {
        return initialTime - System.currentTimeMillis();
    }
}
