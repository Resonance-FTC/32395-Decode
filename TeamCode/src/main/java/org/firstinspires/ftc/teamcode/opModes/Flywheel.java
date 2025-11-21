package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Simple Java translation of the Flywheel subsystem.
 * Handles SPIN / STOP actions and applies motor power in update().
 */
public class Flywheel {
    public enum State {
        SPINNING,
        STOPPED
    }

    public enum Actions {
        SPIN,
        STOP
    }

    private final DcMotorEx flywheel1Motor;
    private volatile State state = State.STOPPED;

    public Flywheel(HardwareMap hardwareMap) {
        // Use HardwareMap to obtain the DcMotorEx instance
        this.flywheel1Motor = hardwareMap.get(DcMotorEx.class, "f1M");
    }

    // Handle actions and update internal state
    public synchronized void handleAction(Actions action) {
        switch (action) {
            case SPIN:
                state = State.SPINNING;
                break;
            case STOP:
                state = State.STOPPED;
                break;
        }
        update();
    }

    // Apply motor outputs based on current state
    public void update() {
        switch (state) {
            case SPINNING:
                flywheel1Motor.setPower(1.0);
                break;
            case STOPPED:
                flywheel1Motor.setPower(0.0);
                break;
        }
    }

    public State getState() {
        return state;
    }
}
