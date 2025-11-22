package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class CachingDcMotorEx implements DcMotorEx {
    private final DcMotorEx dcMotorEx;
    private double cachingTolerance = 0.005;
    private double cachedPower = Double.NaN;

    public CachingDcMotorEx(DcMotorEx dcMotorEx, double cachingTolerance) {
        this.dcMotorEx = dcMotorEx;
        this.cachingTolerance = cachingTolerance;
    }

    public CachingDcMotorEx(DcMotorEx dcMotorEx) {
        this(dcMotorEx, 0.005);
    }

    @Override
    public void setDirection(Direction direction) {
        dcMotorEx.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return dcMotorEx.getDirection();
    }

    @Override
    public synchronized void setPower(double power) {
        double corrected = Math.max(-1.0, Math.min(1.0, power));
        if (Math.abs(corrected - cachedPower) >= cachingTolerance ||
                (corrected == 0.0 && cachedPower != 0.0) ||
                (corrected >= 1.0 && cachedPower < 1.0) ||
                (corrected <= -1.0 && cachedPower > -1.0) ||
                Double.isNaN(cachedPower)) {
            cachedPower = corrected;
            dcMotorEx.setPower(corrected);
        }
    }

    @Override
    public double getPower() {
        return dcMotorEx.getPower();
    }

    public synchronized boolean setPowerResult(double power) {
        double corrected = Math.max(-1.0, Math.min(1.0, power));
        if (Math.abs(corrected - cachedPower) >= cachingTolerance ||
                (corrected == 0.0 && cachedPower != 0.0) ||
                (corrected >= 1.0 && cachedPower < 1.0) ||
                (corrected <= -1.0 && cachedPower > -1.0) ||
                Double.isNaN(cachedPower)) {
            cachedPower = corrected;
            dcMotorEx.setPower(corrected);
            return true;
        }
        return false;
    }

    public synchronized boolean setPowerRaw(double power) {
        double originalTolerance = this.cachingTolerance;
        this.cachingTolerance = 0.0;
        boolean result = setPowerResult(power);
        this.cachingTolerance = originalTolerance;
        return result;
    }

    @Override
    public void setMode(RunMode mode) {
        dcMotorEx.setMode(mode);
    }

    @Override
    public RunMode getMode() {
        return dcMotorEx.getMode();
    }

    @Override
    public MotorConfigurationType getMotorType() {
        return dcMotorEx.getMotorType();
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        dcMotorEx.setMotorType(motorType);
    }

    @Override
    public DcMotorController getController() {
        return dcMotorEx.getController();
    }

    @Override
    public int getPortNumber() {
        return dcMotorEx.getPortNumber();
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        dcMotorEx.setZeroPowerBehavior(zeroPowerBehavior);
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return dcMotorEx.getZeroPowerBehavior();
    }

    @Override
    public void setPowerFloat() {
        dcMotorEx.setPowerFloat();
    }

    @Override
    public boolean getPowerFloat() {
        return dcMotorEx.getPowerFloat();
    }

    @Override
    public void setTargetPosition(int position) {
        dcMotorEx.setTargetPosition(position);
    }

    @Override
    public int getTargetPosition() {
        return dcMotorEx.getTargetPosition();
    }

    @Override
    public boolean isBusy() {
        return dcMotorEx.isBusy();
    }

    @Override
    public int getCurrentPosition() {
        return dcMotorEx.getCurrentPosition();
    }

    @Override
    public void setVelocity(double angularRate) {
        dcMotorEx.setVelocity(angularRate);
    }

    @Override
    public void setVelocity(double angularRate, AngleUnit unit) {
        dcMotorEx.setVelocity(angularRate, unit);
    }

    @Override
    public double getVelocity() {
        return dcMotorEx.getVelocity();
    }

    @Override
    public double getVelocity(AngleUnit unit) {
        return dcMotorEx.getVelocity(unit);
    }

    @Override
    public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {
        dcMotorEx.setPIDCoefficients(mode,pidCoefficients);
    }

    @Override
    public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {
        dcMotorEx.setPIDFCoefficients(mode,pidfCoefficients);

    }

    @Override
    public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
        dcMotorEx.setVelocityPIDFCoefficients(p,i,d,f);
    }

    @Override
    public void setPositionPIDFCoefficients(double p) {
        dcMotorEx.setPositionPIDFCoefficients(p);
    }

    @Override
    public PIDCoefficients getPIDCoefficients(RunMode mode) {
        return dcMotorEx.getPIDCoefficients(mode);
    }

    @Override
    public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
        return dcMotorEx.getPIDFCoefficients(mode);
    }

    @Override
    public void setTargetPositionTolerance(int tolerance) {
        dcMotorEx.setTargetPositionTolerance(tolerance);
    }

    @Override
    public int getTargetPositionTolerance() {
        return dcMotorEx.getTargetPositionTolerance();
    }

    @Override
    public double getCurrent(CurrentUnit unit) {
        return dcMotorEx.getCurrent(unit);
    }

    @Override
    public double getCurrentAlert(CurrentUnit unit) {
        return dcMotorEx.getCurrentAlert(unit);
    }

    @Override
    public void setCurrentAlert(double current, CurrentUnit unit) {
        dcMotorEx.setCurrentAlert(current, unit);
    }

    @Override
    public boolean isOverCurrent() {
        return dcMotorEx.isOverCurrent();
    }

    @Override
    public void setMotorEnable() {
        dcMotorEx.setMotorEnable();
    }

    @Override
    public void setMotorDisable() {
        dcMotorEx.setMotorDisable();
    }

    @Override
    public boolean isMotorEnabled() {
        return dcMotorEx.isMotorEnabled();
    }

    @Override
    public Manufacturer getManufacturer() {
        return dcMotorEx.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return dcMotorEx.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return dcMotorEx.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return dcMotorEx.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        dcMotorEx.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        dcMotorEx.close();
    }
}
