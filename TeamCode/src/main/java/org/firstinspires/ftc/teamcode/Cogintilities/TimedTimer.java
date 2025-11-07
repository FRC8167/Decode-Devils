package org.firstinspires.ftc.teamcode.Cogintilities;

public class TimedTimer {
    private double initialTime;
    private double duration;

    private double freezeTime;
    private boolean frozen = false;

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
        frozen = false;
        freezeTime = Double.NaN;
    }

    public void reset() {
        initialTime = System.currentTimeMillis();
        frozen = false;
        freezeTime = Double.NaN;
    }

    public void freeze() {
        if (frozen) return;
        frozen = true;
        freezeTime = System.currentTimeMillis();
    }

    public void unfreeze() {
        if (!frozen) return;
        frozen = false;
        initialTime += System.currentTimeMillis() - freezeTime;
        freezeTime = Double.NaN;
    }

    private double time() {
        return (frozen ? freezeTime : System.currentTimeMillis());
    }



    public boolean isDone() {
        return (initialTime + duration <= time());
    }

    public double getRemainingTime() {
        return Math.max(0, (initialTime + duration) - time());
    }

    public double getProportionCompleted() {
        if (duration == 0) return -1;
        return (duration - (Math.max(0, (initialTime + duration) - time())))/duration;
    }

    public double getElapsedTime() {
        return initialTime - time();
    }

}
