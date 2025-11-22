package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ServoController;

public class CachingCRServo implements CRServo {
    private final CRServo crServo;
    private double cachingTolerance = 0.005;
    private double cachedPower = Double.NaN;

    /**
     * @param crServo the continuous rotation servo to encapsulate in the caching control
     * @param cachingTolerance the power delta threshold at which a power write will occur.
     */
    public CachingCRServo(CRServo crServo, double cachingTolerance) {
        this.crServo = crServo;
        this.cachingTolerance = cachingTolerance;
    }

    public CachingCRServo(CRServo crServo) {
        this(crServo, 0.005);
    }

    /**
     * Sets the logical direction in which this motor operates.
     *
     * @param direction the direction to set for this motor
     * @see #getDirection()
     */
    @Override
    public void setDirection(Direction direction) {

    }

    /**
     * Returns the current logical direction in which this motor is set as operating.
     *
     * @return the current logical direction in which this motor is set as operating.
     * @see #setDirection(Direction)
     */
    @Override
    public Direction getDirection() {
        return null;
    }

    /**
     * Checks if the change in power since last write exceeds cachingTolerance, if so, does a hardware write (actually sets the power).
     *
     * @param power the new power level of the servo, a value in the interval [-1.0, 1.0]
     */
    @Override
    public synchronized void setPower(double power) {
        double corrected = Math.max(-1.0, Math.min(1.0, power));
        if (Math.abs(corrected - cachedPower) >= cachingTolerance ||
                (corrected == 0.0 && cachedPower != 0.0) ||
                (corrected >= 1.0 && cachedPower < 1.0) ||
                (corrected <= -1.0 && cachedPower > -1.0) ||
                Double.isNaN(cachedPower)) {
            cachedPower = corrected;
            crServo.setPower(corrected);
        }
    }

    /**
     * Checks if the change in power since last write exceeds cachingTolerance, if so, does a hardware write (actually sets the power).
     *
     * @param power the new power level of the servo, a value in the interval [-1.0, 1.0]
     * @return if a hardware write to update the output to the servo was executed
     */
    public synchronized boolean setPowerResult(double power) {
        double corrected = Math.max(-1.0, Math.min(1.0, power));
        if (Math.abs(corrected - cachedPower) >= cachingTolerance ||
                (corrected == 0.0 && cachedPower != 0.0) ||
                (corrected >= 1.0 && cachedPower < 1.0) ||
                (corrected <= -1.0 && cachedPower > -1.0) ||
                Double.isNaN(cachedPower)) {
            cachedPower = corrected;
            crServo.setPower(corrected);
            return true;
        }
        return false;
    }

    /**
     * Sets cachingTolerance to 0 temporarily, then performs and returns setPowerResult.
     *
     * @param power the new power level of the servo, a value in the interval [-1.0, 1.0]
     * @return if a hardware write to update the output to the servo was executed
     */
    public synchronized boolean setPowerRaw(double power) {
        double originalTolerance = this.cachingTolerance;
        this.cachingTolerance = 0.0;
        boolean result = setPowerResult(power);
        this.cachingTolerance = originalTolerance;
        return result;
    }

    @Override
    public double getPower() {
        return crServo.getPower();
    }

    @Override
    public void close() {
        crServo.close();
    }

    @Override
    public Manufacturer getManufacturer() {
        return crServo.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return crServo.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return crServo.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return crServo.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        crServo.resetDeviceConfigurationForOpMode();
    }

    /**
     * Returns the underlying servo controller on which this servo is situated.
     *
     * @return the underlying servo controller on which this servo is situated.
     * @see #getPortNumber()
     */
    @Override
    public ServoController getController() {
        return crServo.getController();
    }

    /**
     * Returns the port number on the underlying servo controller on which this motor is situated.
     *
     * @return the port number on the underlying servo controller on which this motor is situated.
     * @see #getController()
     */
    @Override
    public int getPortNumber() {
        return crServo.getPortNumber();
    }
}
