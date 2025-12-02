// java
package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.InvertedFTCCoordinates;
import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;
import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;

@Configurable
public class Turret {

    public CachingDcMotorEx turretMotor;
    public Closure targetLockClosure;
    private final Constants.AllianceColors currentAlliance;
    public static double startHeadingOffsetDeg = 0.0;

    private final double ticksPerDegree;
    private final long ticksPerRevolution;
    private final Follower follower;

    private final ControlSystem controlSystem = ControlSystem.builder()
            .posPid(Constants.shooterConstants.turretP, Constants.shooterConstants.turretI, Constants.shooterConstants.turretD)
            .build();

    public Turret(HardwareMap hardwareMap, Constants.AllianceColors alliance, Follower follower) {
        this.turretMotor = new CachingDcMotorEx(hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.turretMotorID));
        this.currentAlliance = alliance;
        this.follower = follower;

        this.ticksPerDegree = Constants.shooterConstants.shooterTicksPerRevolution / 360.0;
        this.ticksPerRevolution = Math.round(Constants.shooterConstants.shooterTicksPerRevolution);
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        targetLockClosure = Continuations.exec(() -> {
            // get robot pose
            Pose pose = follower.getPose();
            if (pose == null) return;

            double robotX = pose.getAsCoordinateSystem(PedroCoordinates.INSTANCE).getX();
            double robotY = pose.getAsCoordinateSystem(PedroCoordinates.INSTANCE).getY();
            double robotHeading = Math.toDegrees(follower.getHeading());

            double goalX = -58.3727;
            double goalY = (currentAlliance == Constants.AllianceColors.RED) ? 55.6425 : -55.6425;

            Pose goalPose = PoseConverter.pose2DToPose(new Pose2D(DistanceUnit.INCH, goalX, goalY, AngleUnit.DEGREES, 30), InvertedFTCCoordinates.INSTANCE);

            double deltaX = goalPose.getAsCoordinateSystem(PedroCoordinates.INSTANCE).getX() - robotX;
            double deltaY = goalPose.getAsCoordinateSystem(PedroCoordinates.INSTANCE).getY() - robotY;
            double angleToGoal = Math.toDegrees(Math.atan2(deltaY, deltaX));

            // corrected turret heading in [-180,180]

            // current turret absolute angle from encoder (may be relative but this maps ticks->deg)
            double currentAngleDeg = turretMotor.getCurrentPosition() / ticksPerDegree;

            // choose the nearest equivalent of correctedAngleToGoal to the current encoder angle
            double targetAngleDeg = chooseNearestEquivalent(angleToGoal, currentAngleDeg);

            double targetTicks = targetAngleDeg * ticksPerDegree;
            controlSystem.setGoal(new KineticState(targetTicks));

            turretMotor.setPower(
                    controlSystem.calculate(
                            new KineticState(turretMotor.getCurrentPosition(), turretMotor.getVelocity())
                    )
            );

        });
    }


    private static double chooseNearestEquivalent(double baseNormalizedDeg, double referenceContinuousDeg) {
        double n = Math.round((referenceContinuousDeg - baseNormalizedDeg) / 360.0);
        return baseNormalizedDeg + n * 360.0;
    }

    public void captureStartHeadingOffset() {
        Pose pose = follower.getPose();
        if (pose != null) {
            startHeadingOffsetDeg = Math.toDegrees(pose.getHeading());
        }
    }

    public double getPosition() {
        return (turretMotor.getCurrentPosition() / ticksPerDegree);
    }
    public void setStartHeadingOffsetDeg(double offsetDeg) {
        startHeadingOffsetDeg = offsetDeg;
    }
}
