package org.firstinspires.ftc.teamcode.opModes;

import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.frozenmilk.dairy.mercurial.ftc.Mercurial;

@SuppressWarnings("unused")
public class Teleop {
    private static Pose startPose = new Pose(18.608, 119.523, Math.toRadians(-35));

    public static final Mercurial.RegisterableProgram myFirstMercurialTeleOp = Mercurial.teleop(ctx -> {

        Drivetrain drivetrain = new Drivetrain(ctx.hardwareMap(), ctx.gamepad1(), startPose);
        Turret turret = new Turret(ctx.hardwareMap(), Constants.AllianceColors.BLUE, drivetrain.getFollower());

        ctx.schedule(
                sequence(
                        waitUntil(ctx::inLoop),
                        exec(()-> drivetrain.getFollower().startTeleopDrive()),
                        parallel(
                                loop(
                                        turret.targetLockClosure
                                )
                        )

                )
        );
        ctx.dropToScheduler();
    });
}
