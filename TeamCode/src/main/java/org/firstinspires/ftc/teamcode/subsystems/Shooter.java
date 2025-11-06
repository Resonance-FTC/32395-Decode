package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.control.KalmanFilter;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.util.Constants;

import java.util.List;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.components.Component;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;

public class Shooter implements Component {
    public static final Shooter INSTANCE = new Shooter();
    //start hardware declaration
    private final MotorEx shooterMotor = ActiveOpMode.hardwareMap().get(MotorEx.class, Constants.shooterConstants.shooterMotorID);
    private final MotorEx turretMotor = ActiveOpMode.hardwareMap().get(MotorEx.class, Constants.shooterConstants.turretMotorID);
    private final Limelight3A limelight = ActiveOpMode.hardwareMap().get(Limelight3A.class,Constants.electronicsConstants.LimelightID);
    private Constants.AllianceColors Alliance = null;

    private final ControlSystem turretErrorPID = ControlSystem.builder()
            .angular(AngleType.DEGREES,
                    feedback -> feedback.posPid(Constants.shooterConstants.turretP, Constants.shooterConstants.turretI, Constants.shooterConstants.turretD)
            )
            .build();


    public Command track(double error) {
        return new LambdaCommand()
                .setRequirements("Shooter")
                .setUpdate(() -> {
            LLResult result = limelight.getLatestResult();
            if (result != null) {
                if (result.isValid()) {
                    List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                    for (LLResultTypes.FiducialResult fiducial : fiducials) {
                        double x = fiducial.getTargetXDegrees(); // Where it is (left-right)

                        if (Alliance == Constants.AllianceColors.BLUE) {
                            if (fiducial.getFiducialId() == Constants.fieldConstants.BLUEAPRILTAGID) {// The ID number of the fiducial
                                turretErrorPID.setGoal(new KineticState(x));

                                turretMotor.setPower(
                                        turretErrorPID.calculate(
                                                new KineticState(turretMotor.getCurrentPosition(), turretMotor.getVelocity())
                                        )
                                );

                            }

                        } else {
                            if (fiducial.getFiducialId() == Constants.fieldConstants.REDAPRILTAGID) {
                            }
                        }


                    }
                }
            }
        })
        .setIsDone(() -> false)
                .setInterruptible(true)
                .named("Drive Command");

    }

    public void setAlliance(Constants.AllianceColors alliance) {
        Alliance = alliance;
    }
}
