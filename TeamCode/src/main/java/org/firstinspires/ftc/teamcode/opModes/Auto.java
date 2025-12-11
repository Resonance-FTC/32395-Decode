package org.firstinspires.ftc.teamcode.opModes;

import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.util.Constants;


import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial;
import dev.frozenmilk.dairy.mercurial.ftc.State;

@SuppressWarnings("unused")
public class Auto {
    private static Pose startPose;
    private static PathChain ScorePreload, pickupGPP, intakeGPP, scoreGPP,pickupPGP, intakePGP, scorePGP, intakePPG, pickupPPG, scorePPG;
    private static Follower follower;
    //Pose Definitions

    private static final Pose scorePose = new Pose(50, 100);

    //
    private static final Pose goToPickupGPP = new Pose(44, 83.5);
    private static final Pose intakeGPPPose = new Pose(20, 83.5);

    //
    private static final Pose goToPickupPGP = new Pose(44, 59.5);
    private static final Pose intakePGPPose = new Pose(44, 83.5);

    //
    private static final Pose goToPickupPPG = new Pose(44, 35.5);
    private static final Pose intakePPGPose = new Pose(44, 83.5);


    //Subsystems
    public static Drivetrain drivetrain;
    public static Turret turret;
    public static Intake intake;
    //Auto Options
    public static Constants.AutoOptions autoChoice = null;
    public static Closure bottomFull, topFull, parkOnly, bottomTwoOnly, topTwoOnly;
    public static void buildAutoOptions() {
        bottomFull =
                sequence(
                    exec(() ->startPose = new Pose(19, 120, Math.toRadians(325))),

        parallel(
                loop(
                        sequence(
                        turret.targetLockClosure
                                //exec(() -> spindexer.rotationlogic())
                        )
                ),
                sequence(
                        drivetrain.followPath(ScorePreload),
                        drivetrain.followPath(pickupGPP),
                        exec(() -> intake.getSpin().getTx().send(Intake.Actions.FORWARD)),
                        drivetrain.followPath(intakeGPP),
                        exec(() -> intake.getSpin().getTx().send(Intake.Actions.RELEASE)),
                        drivetrain.followPath(scoreGPP)

                )
                )
        );

        topFull = parallel(
                loop(
                        turret.targetLockClosure
                ),
                sequence(


                )
        );

        parkOnly = parallel(
                loop(
                        turret.targetLockClosure
                ),
                sequence(


                )
        );

        bottomTwoOnly = parallel(
                loop(
                        turret.targetLockClosure
                ),
                sequence(


                )
        );

        topTwoOnly = parallel(
                loop(
                        turret.targetLockClosure
                ),
                sequence(


                )
        );
    }

    //Auto Program
    public static final Mercurial.RegisterableProgram Auto = Mercurial.autonomous(ctx -> {

        drivetrain = new Drivetrain(ctx.hardwareMap(), ctx.gamepad1(), startPose);
        follower = drivetrain.follower;
        turret = new Turret(ctx.hardwareMap(), Constants.AllianceColors.BLUE, follower);
        intake = new Intake(ctx.hardwareMap());
        ctx.schedule(
                sequence(

                        waitUntil(ctx::inLoop),

                        sequence(
                                ifHuh(() -> autoChoice == Constants.AutoOptions.BOTTOMFULL, topFull),
                                ifHuh(()-> autoChoice == Constants.AutoOptions.TOPFULL, topFull),
                                ifHuh(() -> autoChoice == Constants.AutoOptions.PARKONLY, topFull),
                                ifHuh(() -> autoChoice == Constants.AutoOptions.BOTTOMTWOONLY, topFull),
                                ifHuh(() -> autoChoice == Constants.AutoOptions.TOPTWOONLY, topFull)


                                )

                )
        );

    });

    //Path Building
    public static void buildTopTwoOnly() {
        ScorePreload = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(startPose, scorePose)
                )
                .setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(180), 0.8)
                .build();

        pickupGPP = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(scorePose, goToPickupGPP)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        intakeGPP = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(goToPickupGPP, intakeGPPPose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        scoreGPP = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(intakeGPPPose, scorePose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        pickupPGP = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(scorePose, goToPickupPGP)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        intakePGP = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(goToPickupPGP, intakePGPPose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        scorePGP = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(intakePGPPose, scorePose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();


    }
}
