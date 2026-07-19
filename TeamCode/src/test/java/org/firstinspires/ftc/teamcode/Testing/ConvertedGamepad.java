package org.firstinspires.ftc.teamcode.Testing;

import androidx.annotation.NonNull;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper for the FTC Gamepad class that updates its state using Jamepad.
 * This allows you to use physical controllers in a PC-based simulator.
 */
public class ConvertedGamepad extends Gamepad {
    private static ControllerManager controllers;
    private final int preferredIndex;
    private int actualIndex = -1;

    // Internal monitors for edge detection (WasPressed/WasReleased)
    private final Map<String, ButtonMonitor> monitors = new HashMap<>();

    public ConvertedGamepad(int preferredIndex) {
        this.preferredIndex = preferredIndex;
        initSystem();
        update(); // Initial check for connection
    }

    public static void initSystem() {
        if (controllers == null) {
            controllers = new ControllerManager();
            controllers.initSDLGamepad();
        }
    }

    public static void quitSystem() {
        if (controllers != null) {
            controllers.quitSDLGamepad();
            controllers = null;
        }
    }

    public void update() {
        if (controllers == null) return;
        controllers.update();
        
        actualIndex = -1;
        int connectedCount = 0;
        for (int i = 0; i < controllers.getNumControllers(); i++) {
            if (controllers.getState(i).isConnected) {
                if (connectedCount == preferredIndex) {
                    actualIndex = i;
                    break;
                }
                connectedCount++;
            }
        }

        if (actualIndex != -1) {
            ControllerState state = controllers.getState(actualIndex);
            mapStateToGamepad(state);
        } else {
            reset();
        }
    }

    @Override
    public void reset() {
        super.reset();
        monitors.clear();
    }

    private void mapStateToGamepad(@NonNull ControllerState state) {
        // Joysticks with software deadzone
        // Note: Both Jamepad (SDL) and the FTC SDK natively treat UP as NEGATIVE.
        // Therefore, we do not need to manually invert the Y-axis here.
        this.left_stick_x = applyDeadzone(state.leftStickX);
        this.left_stick_y = applyDeadzone(state.leftStickY);
        this.right_stick_x = applyDeadzone(state.rightStickX);
        this.right_stick_y = applyDeadzone(state.rightStickY);

        // Buttons & Edge Detection
        this.a = monitor("a", state.a);
        this.b = monitor("b", state.b);
        this.x = monitor("x", state.x);
        this.y = monitor("y", state.y);
        
        this.dpad_up = monitor("dpad_up", state.dpadUp);
        this.dpad_down = monitor("dpad_down", state.dpadDown);
        this.dpad_left = monitor("dpad_left", state.dpadLeft);
        this.dpad_right = monitor("dpad_right", state.dpadRight);

        this.left_bumper = monitor("left_bumper", state.lb);
        this.right_bumper = monitor("right_bumper", state.rb);
        
        this.left_trigger = state.leftTrigger;
        this.right_trigger = state.rightTrigger;

        this.start = monitor("start", state.start);
        this.back = monitor("back", state.back);
        this.guide = monitor("guide", state.guide);

        this.left_stick_button = monitor("left_stick_button", state.leftStickClick);
        this.right_stick_button = monitor("right_stick_button", state.rightStickClick);
        this.touchpad = monitor("touchpad", state.touchpadButton);

        // Aliases
        this.cross = this.a;
        this.circle = this.b;
        this.square = this.x;
        this.triangle = this.y;
        
        this.refreshTimestamp();
    }

    @Override
    public void refreshTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Applies a small deadzone to joystick values to ignore physical stick drift.
     */
    private float applyDeadzone(float val) {
        float deadzone = 0.05f; // Standard 5% deadzone
        if (Math.abs(val) < deadzone) return 0.0f;
        return val;
    }

    private boolean monitor(String name, boolean pressed) {
        monitors.computeIfAbsent(name, k -> new ButtonMonitor()).update(pressed);
        return pressed;
    }

    private ButtonMonitor get(String name) {
        return monitors.computeIfAbsent(name, k -> new ButtonMonitor());
    }

    // --- Overridden FTC Edge Detection Methods ---
    
    @Override public boolean aWasPressed() { return get("a").wasPressed(); }
    @Override public boolean bWasPressed() { return get("b").wasPressed(); }
    @Override public boolean xWasPressed() { return get("x").wasPressed(); }
    @Override public boolean yWasPressed() { return get("y").wasPressed(); }
    @Override public boolean dpadUpWasPressed() { return get("dpad_up").wasPressed(); }
    @Override public boolean dpadDownWasPressed() { return get("dpad_down").wasPressed(); }
    @Override public boolean dpadLeftWasPressed() { return get("dpad_left").wasPressed(); }
    @Override public boolean dpadRightWasPressed() { return get("dpad_right").wasPressed(); }
    @Override public boolean leftBumperWasPressed() { return get("left_bumper").wasPressed(); }
    @Override public boolean rightBumperWasPressed() { return get("right_bumper").wasPressed(); }
    @Override public boolean leftStickButtonWasPressed() { return get("left_stick_button").wasPressed(); }
    @Override public boolean rightStickButtonWasPressed() { return get("right_stick_button").wasPressed(); }
    @Override public boolean startWasPressed() { return get("start").wasPressed(); }
    @Override public boolean backWasPressed() { return get("back").wasPressed(); }

    @Override public boolean aWasReleased() { return get("a").wasReleased(); }
    @Override public boolean bWasReleased() { return get("b").wasReleased(); }
    @Override public boolean xWasReleased() { return get("x").wasReleased(); }
    @Override public boolean yWasReleased() { return get("y").wasReleased(); }

    @Override public boolean crossWasPressed() { return aWasPressed(); }
    @Override public boolean circleWasPressed() { return bWasPressed(); }
    @Override public boolean squareWasPressed() { return xWasPressed(); }
    @Override public boolean triangleWasPressed() { return yWasPressed(); }

    public boolean isConnected() {
        return actualIndex != -1;
    }

    private static class ButtonMonitor {
        private boolean curr, last, pressed, released;
        void update(boolean newState) {
            last = curr; curr = newState;
            if (curr && !last) pressed = true;
            if (!curr && last) released = true;
        }
        boolean wasPressed() { boolean r = pressed; pressed = false; return r; }
        boolean wasReleased() { boolean r = released; released = false; return r; }
    }
}
