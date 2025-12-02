package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Constants.DriveConstants;

import dev.frozenmilk.dairy.mercurial.continuations.Actors.Actor;
import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations;

public class Drivetrain {



    public Follower follower;
    public Drivetrain(HardwareMap hardwareMap, Gamepad gamepad, Pose startPose) {

        follower = org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        follower.update();
    }
    public Follower getFollower() {
        return follower;
    }
}
