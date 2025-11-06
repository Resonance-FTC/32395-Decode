package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.util.Constants.robotConstants.*;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(org.firstinspires.ftc.teamcode.util.Constants.robotConstants.mass);
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
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
                .pathConstraints(pathConstraints)
                .build();
    }
}
