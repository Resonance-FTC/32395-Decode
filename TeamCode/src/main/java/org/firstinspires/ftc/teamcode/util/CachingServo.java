package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class CachingServo implements Servo {
    private final Servo servo;
    private double cachingTolerance = 0.001;
    private double cachedPosition = Double.NaN;

    /**
     * @param servo the servo to encapsulate in the caching control
     * @param cachingTolerance the position delta threshold at which a position write will occur.
     */
    public CachingServo(Servo servo, double cachingTolerance) {
        this.servo = servo;
        this.cachingTolerance = cachingTolerance;
    }

    public CachingServo(Servo servo) {
        this(servo, 0.001);
    }

    /**
     * Checks if the change in position since last write exceeds cachingTolerance, if so, does a hardware write (actually sets the position).
     *
     * @param position the position to which the servo should move, a value in the range [0.0, 1.0]
     */
    @Override
    public synchronized void setPosition(double position) {
        double corrected = Math.max(0.0, Math.min(1.0, position));
        if (Math.abs(corrected - cachedPosition) >= cachingTolerance ||
                (corrected <= 0.0 && cachedPosition > 0.0) ||
                (corrected >= 1.0 && cachedPosition < 1.0) ||
                Double.isNaN(cachedPosition)) {
            cachedPosition = corrected;
            servo.setPosition(corrected);
        }
    }

    /**
     * Checks if the change in position since last write exceeds cachingTolerance, if so, does a hardware write (actually sets the position).
     *
     * @param position the position to which the servo should move, a value in the range [0.0, 1.0]
     * @return if a hardware write to update the output to the servo was executed
     */
    public synchronized boolean setPositionResult(double position) {
        double corrected = Math.max(0.0, Math.min(1.0, position));
        if (Math.abs(corrected - cachedPosition) >= cachingTolerance ||
                (corrected <= 0.0 && cachedPosition > 0.0) ||
                (corrected >= 1.0 && cachedPosition < 1.0) ||
                Double.isNaN(cachedPosition)) {
            cachedPosition = corrected;
            servo.setPosition(corrected);
            return true;
        }
        return false;
    }

    /**
     * Sets cachingTolerance to 0 temporarily, then performs and returns setPositionResult.
     *
     * @param position the position to which the servo should move, a value in the range [0.0, 1.0]
     * @return if a hardware write to update the output to the servo was executed
     */
    public synchronized boolean setPositionRaw(double position) {
        double originalTolerance = this.cachingTolerance;
        this.cachingTolerance = 0.0;
        boolean result = setPositionResult(position);
        this.cachingTolerance = originalTolerance;
        return result;
    }

    // Delegate all other Servo methods to the encapsulated servo instance
    @Override
    public double getPosition() {
        return servo.getPosition();
    }

    @Override
    public void scaleRange(double min, double max) {
        servo.scaleRange(min, max);
    }

    @Override
    public ServoController getController() {
        return servo.getController();
    }

    @Override
    public int getPortNumber() {
        return servo.getPortNumber();
    }

    @Override
    public void setDirection(Direction direction) {
        servo.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return servo.getDirection();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        servo.resetDeviceConfigurationForOpMode();
    }

    /**
     * Returns an indication of the manufacturer of this device.
     *
     * @return the device's manufacturer
     */
    @Override
    public Manufacturer getManufacturer() {
        return servo.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return servo.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return servo.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return servo.getVersion();
    }

    @Override
    public void close() {
        servo.close();
    }
}
