package org.firstinspires.ftc.teamcode.opModes

import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.FuturePose
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.HeadingInterpolator
import com.pedropathing.paths.HeadingInterpolator.FutureDouble
import com.pedropathing.paths.Path
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.parallel
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.waitUntil
import dev.frozenmilk.dairy.mercurial.ftc.Context
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial.Program
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial.RegisterableProgram
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial.teleop
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain
import org.firstinspires.ftc.teamcode.subsystems.Flywheel
import org.firstinspires.ftc.teamcode.subsystems.Intake
import org.firstinspires.ftc.teamcode.subsystems.Spindexer
import org.firstinspires.ftc.teamcode.subsystems.Turret
import org.firstinspires.ftc.teamcode.util.Constants
import java.util.function.BooleanSupplier

@Suppress("unused")
class TestingIntake {
    private val startPose = Pose(18.608, 119.523, Math.toRadians(-35.0))

    val myFirstMercurialTeleOp: RegisterableProgram = teleop { ctx: Context? ->
        val drivetrain = Drivetrain(ctx!!.hardwareMap, ctx.gamepad1, startPose)
        val follower = drivetrain.follower

        val intake = Intake(ctx.hardwareMap)

        val pathChain = follower.pathBuilder() //Lazy Curve Generation
            .addPath(Path(BezierLine({ follower.pose }, Pose(30.0, 0.0))))
            .setHeadingInterpolation(
                HeadingInterpolator.linearFromPoint(
                    { follower.heading },
                    Math.toRadians(45.0),
                    0.8
                )
            )
            .build()

        ctx.schedule(
            sequence(
                waitUntil(ctx::inLoop),
                exec { follower.startTeleopDrive() }

            )
        )

        ctx.bindExec(
            { ctx.gamepad1.aWasPressed() },
            sequence(
                drivetrain.followPath(pathChain),
                drivetrain.drive()
            )
        )

        ctx.bindSpawn(
            ctx.risingEdge { ctx.gamepad1.right_bumper },
            exec{ drivetrain.setSpeed(.2) }
        )

        ctx.bindSpawn(
            ctx.risingEdge({ !ctx.gamepad1.right_bumper }),
            exec { drivetrain.setSpeed(1.0) }
        )


        ctx.bindSpawn(
            ctx.risingEdge({ ctx.gamepad2.a }),
            exec { intake.spin.tx.send(Intake.Actions.FORWARD) }
        )

        ctx.bindSpawn(
            ctx.risingEdge({ !ctx.gamepad2.a }),
            exec { intake.spin.tx.send(Intake.Actions.RELEASE) }
        )
        ctx.bindSpawn(
            ctx.risingEdge({ ctx.gamepad2.b }),
            exec { intake.spin.tx.send(Intake.Actions.BACK) }
        )



        ctx.dropToScheduler()
    }
}