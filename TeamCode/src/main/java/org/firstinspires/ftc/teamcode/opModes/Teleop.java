package org.firstinspires.ftc.teamcode.opModes;

import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Spindexer;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.util.Constants;


import dev.frozenmilk.dairy.mercurial.ftc.Mercurial;

@SuppressWarnings("unused")
public class Teleop {
    private static final Pose startPose = new Pose(18.608, 119.523, Math.toRadians(-35));
    
    public static final Mercurial.RegisterableProgram myFirstMercurialTeleOp = Mercurial.teleop(ctx -> {

        Drivetrain drivetrain = new Drivetrain(ctx.hardwareMap(), ctx.gamepad1(), startPose);
        Follower follower = drivetrain.follower;

        Turret turret = new Turret(ctx.hardwareMap(), Constants.AllianceColors.BLUE, follower);
        Spindexer spindexer = new Spindexer(ctx.hardwareMap());
        Flywheel flywheel = new Flywheel(ctx.hardwareMap(), spindexer);
        Intake intake = new Intake(ctx.hardwareMap());

        PathChain pathChain = follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(30, 0))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(45), 0.8))
                .build();
        
        ctx.schedule(
                sequence(
                        waitUntil(ctx::inLoop),
                        exec(follower::startTeleopDrive),
                        parallel(
                                loop(
                                        turret.targetLockClosure
                                )
                        )

                )
        );

        ctx.bindExec(() -> ctx.gamepad1().aWasPressed(),
                sequence(
                        drivetrain.followPath(pathChain),
                        drivetrain.drive()
                )
        );

        ctx.bindSpawn(ctx.risingEdge( ()-> ctx.gamepad1().right_bumper),
                exec(()->drivetrain.setSpeed(.2))
        );

        ctx.bindSpawn(ctx.risingEdge( ()-> !ctx.gamepad1().right_bumper),
                exec(()->drivetrain.setSpeed(1))
        );




        ctx.dropToScheduler();
    });
}
