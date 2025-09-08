package org.firstinspires.ftc.teamcode.Cogintilities;

public class LPF_Basic {
    private double alpha;
    private double filteredValue;
    private boolean initialized = false;

    public LPF_Basic(double cutoffHz, double sampleRateHz) {
        // Compute alpha using the standard exponential filter formula
        double dt = 1.0 / sampleRateHz;
        double rc = 1.0 / (2 * Math.PI * cutoffHz);
        this.alpha = dt / (rc + dt);
    }

    public double filter(double input) {
        if (!initialized) {
            filteredValue = input;
            initialized = true;
        } else {
            filteredValue += alpha * (input - filteredValue);
        }
        return filteredValue;
    }


    public void reset() {
        initialized = false;
    }
}
