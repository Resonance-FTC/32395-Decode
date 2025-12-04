package org.firstinspires.ftc.teamcode.subsystems;

import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.command;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Constants.DriveConstants;

import java.util.function.Supplier;

import dev.frozenmilk.dairy.mercurial.continuations.Actors.Actor;
import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.Command;

public class Drivetrain {

    private double speed = 1.0;
    private final Gamepad gamepad;
    public Follower follower;

    public Drivetrain(HardwareMap hardwareMap, Gamepad gamepad, Pose startPose) {
        this.gamepad = gamepad;

        follower = org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        follower.update();
    }

    public Command followPath(PathChain pathChainSupplier) {
        return command()
                .setInit(() -> {
                    follower.breakFollowing();
                    follower.followPath(pathChainSupplier);
                })
                .setFinished(() -> !follower.isBusy() || gamepad.bWasPressed());
    }
    public Command followPath(Path path) {
        return command()
                .setInit(() -> {
                    follower.breakFollowing();
                    follower.followPath(path);
                })
                .setFinished(() -> !follower.isBusy() || gamepad.bWasPressed());
    }


    public Command drive() {
        return command()
                .setExecute(() -> {
                    follower.setTeleOpDrive(
                            -gamepad.left_stick_y * speed,
                            -gamepad.left_stick_x * speed,
                            -gamepad.right_stick_x * speed,
                            true);
                }).setFinished(() -> gamepad.aWasPressed());
    }

    public void setSpeed(double speed){
        this.speed = speed;
        follower.setTeleOpDrive(
                -gamepad.left_stick_y * speed,
                -gamepad.left_stick_x * speed,
                -gamepad.right_stick_x * speed,
                true);
    }
    public Follower getFollower() {
        return follower;
    }
}
