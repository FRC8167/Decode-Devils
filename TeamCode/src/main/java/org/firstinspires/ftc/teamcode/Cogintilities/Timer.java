package org.firstinspires.ftc.teamcode.Cogintilities;

public class Timer {
    double initialTime;
    double duration;

    public Timer(double timeInSeconds) {
        initialTime = System.currentTimeMillis();
        duration = timeInSeconds*1000;
    }

    public Timer() {
        initialTime = System.currentTimeMillis();
        duration = 0;
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
