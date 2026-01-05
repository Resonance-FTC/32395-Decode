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

import dev.frozenmilk.dairy.mercurial.ftc.Context;
import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;

@Configurable
public class Turret {

    public CachingDcMotorEx turretMotor;
    public Closure targetLockClosure;
    private final Constants.AllianceColors currentAlliance;
    public static double startHeadingOffsetDeg = 0.0;

    private final Follower follower;

    // use a stronger P by default; replace with tuned values from Constants if available
    private final ControlSystem controlSystem = ControlSystem.builder()
            .posPid(0.01, 0, 0)
            .build();

    // explicit conversion factor (ticks per degree)
    private static final double TICKS_PER_DEG = 360/145.1;

    public Turret(HardwareMap hardwareMap, Constants.AllianceColors alliance, Follower follower, Context ctx) {
        this.turretMotor = new CachingDcMotorEx(hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.turretMotorID));
        this.currentAlliance = alliance;
        this.follower = follower;

        // start stopped, BRAKE so it holds position when no power is applied
        turretMotor.setPower(0.0);
        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // reset encoders then enable running with encoder so setPower has effect
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        targetLockClosure = Continuations.exec(() -> {
            follower.update();
            Pose pose = follower.getPose();
            if (pose == null) return;

            double robotX = pose.getAsCoordinateSystem(PedroCoordinates.INSTANCE).getX();
            double robotY = pose.getAsCoordinateSystem(PedroCoordinates.INSTANCE).getY();

            double goalX = (currentAlliance == Constants.AllianceColors.BLUE) ? 72.0 - 58.0 : 72.0 + 58.0;
            double goalY = (currentAlliance == Constants.AllianceColors.RED) ? 55.6425 : -55.6425;

            Pose goalPose = PoseConverter.pose2DToPose(new Pose2D(DistanceUnit.INCH, goalX, goalY, AngleUnit.DEGREES, 30), InvertedFTCCoordinates.INSTANCE);
            ctx.telemetry().addData("GoalPose", goalPose);
            double deltaX = goalPose.getAsCoordinateSystem(PedroCoordinates.INSTANCE).getX() - robotX;
            double deltaY = goalPose.getAsCoordinateSystem(PedroCoordinates.INSTANCE).getY() - robotY;
            double angleToGoal = Math.toDegrees(Math.atan2(deltaY, deltaX));

            // convert encoder ticks <-> degrees using TICKS_PER_DEG
            double currentTicks = turretMotor.getCurrentPosition();
            double currentAngleDeg = currentTicks / TICKS_PER_DEG;

            double targetAngleDeg = angleToGoal;
            double targetTicks = targetAngleDeg * TICKS_PER_DEG;

            controlSystem.setGoal(new KineticState(targetTicks));

            double controllerOutput = controlSystem.calculate(
                    new KineticState(turretMotor.getCurrentPosition(), turretMotor.getVelocity())
            );

            // clamp to valid motor power range
            double appliedPower = Math.max(-1.0, Math.min(1.0, controllerOutput));

            ctx.telemetry().addData("AngleToGoal", angleToGoal);
            ctx.telemetry().addData("CurrentAngleDeg", currentAngleDeg);
            ctx.telemetry().addData("TargetAngleDeg", targetAngleDeg);
            ctx.telemetry().addData("ControllerRaw", controllerOutput);
            ctx.telemetry().addData("AppliedPower", appliedPower);
            ctx.telemetry().addData("EncoderPos", turretMotor.getCurrentPosition());
            ctx.telemetry().update();

            turretMotor.setPower(appliedPower);
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
        return turretMotor.getCurrentPosition() / TICKS_PER_DEG;
    }

    public void setStartHeadingOffsetDeg(double offsetDeg) {
        startHeadingOffsetDeg = offsetDeg;
    }
}
