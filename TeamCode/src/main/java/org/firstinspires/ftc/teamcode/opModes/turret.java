
        package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;
import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;

public class turret {

    public CachingDcMotorEx turretMotor;
    public Closure targetLockClosure;
    public CachingServo hoodServo;
    private final Constants.AllianceColors currentAlliance;
    private final Teleoplocalization localization;
    private double startHeadingOffsetDeg = 0.0; // offset applied to corrected angle

    // Relative-encoder handling
    private double continuousAngleDeg = 0.0; // accumulated angle from encoder deltas
    private final double ticksPerDegree;
    private final int maxContinuousRevolutions = 2; // configurable limit
    private final double maxContinuousAngleDeg;

    ControlSystem controlSystem = ControlSystem.builder()
            .posPid(Constants.shooterConstants.turretP, Constants.shooterConstants.turretI, Constants.shooterConstants.turretD)
            .build();

    public turret(HardwareMap hardwareMap, Constants.AllianceColors alliance) {
        this.turretMotor = hardwareMap.get(CachingDcMotorEx.class, Constants.shooterConstants.turretMotorID);
        this.hoodServo = hardwareMap.get(CachingServo.class, Constants.shooterConstants.hoodServoID);
        this.localization = new Teleoplocalization(hardwareMap);
        this.currentAlliance = alliance;

        this.ticksPerDegree = Constants.shooterConstants.shooterTicksPerRevolution / 360.0;
        this.maxContinuousAngleDeg = maxContinuousRevolutions * 360.0;

        // initialize encoder tracking
        this.continuousAngleDeg = (turretMotor.getCurrentPosition() / ticksPerDegree); // starting baseline

        targetLockClosure = Continuations.exec(() -> {
            Pose2D pose = localization.getPose();
            double robotX = pose.getX(DistanceUnit.INCH);
            double robotY = pose.getY(DistanceUnit.INCH);
            double robotHeading = pose.getHeading(AngleUnit.DEGREES);

            double goalX = -58.3727;
            double goalY = (currentAlliance == Constants.AllianceColors.RED) ? 55.6425 : -55.6425;

            double deltaX = goalX - robotX;
            double deltaY = goalY - robotY;
            double angleToGoal = Math.toDegrees(Math.atan2(deltaY, deltaX));

            double correctedAngleToGoal = getCorrectedAngleToGoal(angleToGoal, robotHeading, startHeadingOffsetDeg);

            // Update continuous angle from relative encoder deltas
            updateContinuousAngleFromEncoder();

            // Choose the target angle (in continuous space) that is the nearest equivalent to correctedAngleToGoal
            double targetAngleDeg = chooseNearestEquivalent(correctedAngleToGoal, continuousAngleDeg);

            // Enforce maximum continuous rotation allowed; if required motion exceeds the limit, pull it back by 360-degree steps
            targetAngleDeg = enforceMaxContinuousRotation(targetAngleDeg, continuousAngleDeg);

            // Convert degrees to motor ticks
            double targetTicks = targetAngleDeg * ticksPerDegree;

            controlSystem.setGoal(new KineticState(targetTicks));

            // Current state: use raw encoder ticks and motor velocity (ticks and ticks/sec)
            turretMotor.setPower(
                    controlSystem.calculate(
                            new KineticState(turretMotor.getCurrentPosition(), turretMotor.getVelocity())
                    )
            );
        });
    }

    private void updateContinuousAngleFromEncoder() {
        long currentTicks = turretMotor.getCurrentPosition();
        continuousAngleDeg = currentTicks / ticksPerDegree;
    }

    /**
     * Pick the nearest equivalent angle to the current continuous angle by adding/subtracting 360*n
     */
    private double chooseNearestEquivalent(double baseNormalizedDeg, double referenceContinuousDeg) {
        // baseNormalizedDeg is in [-180, 180]
        double n = Math.round((referenceContinuousDeg - baseNormalizedDeg) / 360.0);
        return baseNormalizedDeg + n * 360.0;
    }

    /**
     * Ensure the turret won't be commanded to rotate more than configured continuous revolutions.
     * If the delta is larger than allowed, step the target by +/-360 until within limit.
     */
    private double enforceMaxContinuousRotation(double targetDeg, double currentDeg) {
        if (Math.abs(targetDeg) <= maxContinuousAngleDeg) return targetDeg;
        double delta = targetDeg - currentDeg;
        // If delta too large, reduce by whole 360-degree steps toward the current angle until within limit
        double ceil = Math.ceil((Math.abs(delta) - maxContinuousAngleDeg) / 360.0);
        if (delta > 0) {
            targetDeg -= ceil * 360.0;
        } else {
            targetDeg += ceil * 360.0;
        }
        return targetDeg;
    }

    /**
     * Capture the robot heading at the start of the opmode and use it as the turret offset.
     * Call this from opmode init after localization is valid.
     */
    public void captureStartHeadingOffset() {
        Pose2D pose = localization.getPose();
        if (pose != null) {
            this.startHeadingOffsetDeg = normalizeAngle(pose.getHeading(AngleUnit.DEGREES));
        }
    }

    /**
     * Manually set a start heading offset in degrees. Useful if you want to override the captured value.
     */
    public void setStartHeadingOffsetDeg(double offsetDeg) {
        this.startHeadingOffsetDeg = normalizeAngle(offsetDeg);
    }

    private static double getCorrectedAngleToGoal(double angleToGoal, double robotHeading, double startOffsetDeg) {
        double corrected = angleToGoal - robotHeading - startOffsetDeg;
        return normalizeAngle(corrected);
    }

    private static double normalizeAngle(double angleDeg) {
        double a = angleDeg;
        while (a > 180.0) a -= 360.0;
        while (a <= -180.0) a += 360.0;
        return a;
    }

}
