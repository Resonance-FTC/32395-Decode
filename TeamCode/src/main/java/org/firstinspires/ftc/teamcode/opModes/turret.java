package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.CachingDcMotorEx;
import org.firstinspires.ftc.teamcode.util.CachingServo;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;

public class turret {

    public CachingDcMotorEx turretMotor;
    public Closure targetLockClosure;
    public CachingServo hoodServo;
    private final Constants.AllianceColors currentAlliance;
    ControlSystem controlSystem = ControlSystem.builder()
            .posPid(Constants.shooterConstants.turretP, Constants.shooterConstants.turretI, Constants.shooterConstants.turretD)
            .build();
    public turret(HardwareMap hardwareMap, Constants.AllianceColors alliance) {
        this.turretMotor = hardwareMap.get(CachingDcMotorEx.class, Constants.shooterConstants.turretMotorID);
        this.hoodServo = hardwareMap.get(CachingServo.class, Constants.shooterConstants.hoodServoID);
        Teleoplocalization localization = new Teleoplocalization(hardwareMap);
        this.currentAlliance = alliance;
        targetLockClosure = Continuations.exec(() -> {
            // Red Goal is at X=-58.3727 Y=55.6425 Z=29.5
            // Blue Goal is at X=-58.3727 Y=-55.6425 Z=29.5

                // Get the current position of the robot from localization
                double robotX = localization.getPose().getX(DistanceUnit.INCH);
                double robotY = localization.getPose().getY(DistanceUnit.INCH);
                double robotHeading = localization.getPose().getHeading(AngleUnit.DEGREES);

                // Determine the goal position based on the alliance color
                double goalX = -58.3727;
                double goalY = (currentAlliance == Constants.AllianceColors.RED) ? 55.6425 : -55.6425;

                // Calculate the angle to the goal
                double deltaX = goalX - robotX;
                double deltaY = goalY - robotY;
                double angleToGoal = Math.toDegrees(Math.atan2(deltaY, deltaX));

                // Define the maximum allowable rotation in degrees
            double correctedAngleToGoal = getCorrectedAngleToGoal(angleToGoal, robotHeading);

            // Convert the corrected angle to motor ticks (assuming a specific conversion factor)
                int targetPosition = (int) (correctedAngleToGoal * Constants.shooterConstants.shooterTicksPerRevolution);

                controlSystem.setGoal(new KineticState(targetPosition));

                // Set the turret motor to the target position
                turretMotor.setPower(
                    controlSystem.calculate(
                            new KineticState(turretMotor.getCurrentPosition() * Constants.shooterConstants.shooterTicksPerRevolution, turretMotor.getVelocity())
                    )
                );
        });
    }

    private static double getCorrectedAngleToGoal(double angleToGoal, double robotHeading) {
        final double MAX_ROTATION = 360; // Adjust this value as needed

        // Correct the angle to account for the robot's heading
        double correctedAngleToGoal = angleToGoal - robotHeading;

        // Clamp the corrected angle between -180 and 180 degrees
        if (correctedAngleToGoal > 180) {
            correctedAngleToGoal -= 360;
        } else if (correctedAngleToGoal < -180) {
            correctedAngleToGoal += 360;
        }

        // Ensure the turret does not rotate beyond the maximum allowable rotation
        if (correctedAngleToGoal > MAX_ROTATION) {
            correctedAngleToGoal -= 360;
        } else if (correctedAngleToGoal < -MAX_ROTATION) {
            correctedAngleToGoal += 360;
        }
        return correctedAngleToGoal;
    }


}
