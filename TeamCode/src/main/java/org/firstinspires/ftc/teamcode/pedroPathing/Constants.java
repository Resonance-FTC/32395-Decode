package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .forwardZeroPowerAcceleration(-46.271831826131304)
            .lateralZeroPowerAcceleration(-73.7860458335007)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.05, 0.0, 0.002, 0.02))
            .headingPIDFCoefficients(new PIDFCoefficients(0.9, 0.0, 0.002, 0.025))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.013, 0.0, 0.00001, 0.6, 0.015))
            .centripetalScaling(0.003)
            .mass(org.firstinspires.ftc.teamcode.util.Constants.robotConstants.mass)
            .automaticHoldEnd(true);
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .xVelocity(77.04257033941315)
            .yVelocity(58.15063380444144)
            .rightFrontMotorName(org.firstinspires.ftc.teamcode.util.Constants.DriveConstants.FRMotorID)
            .rightRearMotorName(org.firstinspires.ftc.teamcode.util.Constants.DriveConstants.BRMotorID)
            .leftRearMotorName(org.firstinspires.ftc.teamcode.util.Constants.DriveConstants.BLMotorID)
            .leftFrontMotorName(org.firstinspires.ftc.teamcode.util.Constants.DriveConstants.FLMotorID)
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);


    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .build();
    }
    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(3.5)
            .strafePodX(-5.5)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName(org.firstinspires.ftc.teamcode.util.Constants.electronicsConstants.odoID)
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);
}
