package org.firstinspires.ftc.teamcode.opModes.tests;

import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec;
import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop;
import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence;
import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.waitUntil;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.turret;
import org.firstinspires.ftc.teamcode.util.Constants;

import java.util.function.Supplier;

import dev.frozenmilk.dairy.mercurial.ftc.Mercurial;

@SuppressWarnings("unused")
public class OpModeTemplate {
    private static class State {
    }
    private static Follower follower;
    private static Pose startPose = new Pose(18.608, 119.523, Math.toRadians(-35));
    private static boolean automatedDrive;
    private static Supplier<PathChain> pathChain;
    private static TelemetryManager telemetryM;
    private static boolean slowMode = false;
    private static double slowModeMultiplier = 0.5;
    public static final Mercurial.RegisterableProgram trackingMaybe = Mercurial.teleop(ctx -> {

        follower = org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower(ctx.hardwareMap());
        follower.setStartingPose(startPose);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        pathChain = () -> follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(30, 0))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(45), 0.8))
                .build();
        // make sure we have some new state
        State state = new State();
        turret Turret = new turret(ctx.hardwareMap(), Constants.AllianceColors.BLUE, follower);
        //Flywheel flywheel = new Flywheel(ctx.hardwareMap());
        // POV drive
        ctx.schedule(
                sequence(
                        // wait can also take a boolean supplier,
                        // we'll start this process now,
                        // but it will wait until we press play to actually start running
                        waitUntil(ctx::inLoop),
                        sequence(exec(() -> {
                                    follower.startTeleopDrive();
                                }),
                                loop(
                                        sequence(
                                            Turret.targetLockClosure,
                                            exec(() -> {
                                                looping(ctx.gamepad1(), Turret);
                                            })
                                        )
                                )
                                )

                )
        );



        //ctx.bindSpawn(
        //        ctx.risingEdge(() -> ctx.gamepad2().right_bumper ),
        //        exec(() -> flywheel.handleAction(Flywheel.Actions.SPIN))
        //);

        ctx.dropToScheduler();
    });
    public static void looping(Gamepad gamepad1, turret Turret) {
        follower.update();
        telemetryM.update();
        telemetryM.addData("Position: ", Turret.getPosition());
        if (!automatedDrive) {
            //Make the last parameter false for field-centric
            //In case the drivers want to use a "slowMode" you can scale the vectors

            //This is the normal version to use in the TeleOp
            if (!slowMode) follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    false, // Robot Centric
                    Math.PI
            );

                //This is how it looks with slowMode on
            else follower.setTeleOpDrive(
                    -gamepad1.left_stick_y * slowModeMultiplier,
                    -gamepad1.left_stick_x * slowModeMultiplier,
                    -gamepad1.right_stick_x * slowModeMultiplier,
                    false,
                    Math.PI
// Robot Centric
            );
        }

        //Automated PathFollowing
        if (gamepad1.aWasPressed()) {
            follower.followPath(pathChain.get());
            automatedDrive = true;
        }

        //Stop automated following if the follower is done
        if (automatedDrive && (gamepad1.bWasPressed() || !follower.isBusy())) {
            follower.startTeleopDrive();
            automatedDrive = false;
        }

        //Slow Mode
        if (gamepad1.rightBumperWasPressed()) {
            slowMode = !slowMode;
        }

        //Optional way to change slow mode strength
        if (gamepad1.xWasPressed()) {
            slowModeMultiplier += 0.25;
        }

        telemetryM.debug("position", follower.getPose());
        telemetryM.debug("velocity", follower.getVelocity());
        telemetryM.debug("automatedDrive", automatedDrive);
    }
}