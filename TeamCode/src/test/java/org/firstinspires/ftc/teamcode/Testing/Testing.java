package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.junit.Test;

/**
 * OFFLINE ROBOT SIMULATOR
 * Use this to test your logic with a real PC Gamepad (Wireless or Wired).
 */
public class Testing {

    public static void main(String[] args) {
        System.out.println("--- Jamepad Simulator Starting ---");
        
        System.out.println("Initializing ConvertedGamepad...");
        ConvertedGamepad gamepad = new ConvertedGamepad(0);
        
        System.out.println("Initializing MockDcMotorEx...");
        MockDcMotorEx liftMotor = new MockDcMotorEx();
        liftMotor.setMode(com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        System.out.println("Initializing Visualizer...");
        SimulationVisualizer visualizer = new SimulationVisualizer();
        
        if (gamepad.isConnected()) {
            System.out.println("Controller detected!");
        } else {
            System.out.println("No controller detected initially.");
        }
        
        boolean running = true;
        int loopCount = 0;

        System.out.println("Entering Simulator Loop...");
        System.out.println("Press 'START' on your gamepad to exit.");
        
        try {
            while (running) {
                loopCount++;
                gamepad.update();

                if (!gamepad.isConnected()) {
                    if (loopCount % 50 == 0) {
                        System.out.print("Waiting for controller connection...\r");
                        System.out.flush();
                    }
                    Thread.sleep(1000);
                    continue;
                }

                // 3. Run your Subsystem Logic (Example)

//                liftMotor.setPower(gamepad.left_stick_y);
                liftMotor.setTargetPosition((int) (-gamepad.left_stick_y * 1000));
                liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftMotor.setPower(1);
//                System.out.println("Power: " + liftMotor.getPower());
                liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                
                // 4. Update Simulation
                liftMotor.update();

                // 5. Visualizer Update (Sends to Dashboard + Console)
                visualizer.updateData(liftMotor.getCurrentPosition(), liftMotor.getVelocity(), liftMotor.getTargetPosition());
                
                if (gamepad.start) {
                    System.out.println("\nQuitting...");
                    running = false; 
                }

                Thread.sleep(20); // ~50Hz refresh rate
            }
        } catch (Exception e) {
            System.err.println("\nError in simulator loop:");
            e.printStackTrace();
        } finally {
            System.out.println("\nCleaning up...");
            ConvertedGamepad.quitSystem();
            visualizer.dispose();
            System.out.println("Simulator Closed.");
        }
    }

    @Test
    public void testMockMotorPhysics() {
        MockDcMotorEx motor = new MockDcMotorEx();
        
        System.out.println("Testing Motor Physics and PIDF...");
        
        // Test 1: Static Friction (kS) in Open Loop
        motor.setMode(com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(0.02); // Below kS (0.05)
        for(int i=0; i<10; i++) {
            motor.update();
            try { Thread.sleep(10); } catch (Exception e) {}
        }
        System.out.println("Position after low power (Open Loop, should be 0): " + motor.getCurrentPosition());
        
        // Test 2: Static Friction (kS) in Closed Loop
        motor.setMode(com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setPower(0.02); // Below kS (0.05)
        for(int i=0; i<10; i++) {
            motor.update();
            try { Thread.sleep(10); } catch (Exception e) {}
        }
        System.out.println("Position after low power (Closed Loop, should be 0): " + motor.getCurrentPosition());

        // Test 3: Acceleration and Velocity
        motor.setPower(1.0);
        for(int i=0; i<50; i++) {
            motor.update();
            if (i % 10 == 0) System.out.println("Accelerating... Velocity: " + motor.getVelocity());
            try { Thread.sleep(10); } catch (Exception e) {}
        }
        
        // Test 4: RunToPosition Settle
        System.out.println("Testing RUN_TO_POSITION...");
        motor.setTargetPosition(1000);
        motor.setMode(com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.5);
        
        int cycles = 0;
        while (motor.isBusy() && cycles < 100) {
            motor.update();
            if (cycles % 20 == 0) System.out.println("Moving to 1000... Pos: " + motor.getCurrentPosition());
            cycles++;
            try { Thread.sleep(10); } catch (Exception e) {}
        }
        System.out.println("Final Position (should be near 1000): " + motor.getCurrentPosition());
        System.out.println("isBusy after arrival: " + motor.isBusy());
    }

    @Test
    public void runSimulator() {
        // Trigger the main method for interactive testing
        main(new String[]{});
    }
}
