package org.firstinspires.ftc.teamcode.opModes

import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.HeadingInterpolator
import com.pedropathing.paths.Path
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Actors.actor
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.waitUntil
import dev.frozenmilk.dairy.mercurial.continuations.channels.Channels
import dev.frozenmilk.dairy.mercurial.continuations.channels.Sender
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial.RegisterableProgram
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain
import org.firstinspires.ftc.teamcode.subsystems.Intake
import java.util.function.BooleanSupplier
import java.util.function.Supplier

@Suppress("unused")
object TestingIntake {

    val intakeTesting: RegisterableProgram = Mercurial.teleop {
        waitUntil(this::inLoop)

        val drivetrain = Drivetrain(this.hardwareMap, this.gamepad1, Pose(18.608, 119.523, Math.toRadians(-35.0)))
        val follower = drivetrain.follower

        val intake = Intake(this.hardwareMap)

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

        this.schedule(
            sequence(
                waitUntil(this::inLoop),
                exec { follower.startTeleopDrive() }

            )
        )

        this.bindExec(
            { this.gamepad1.xWasPressed() },
            sequence(
                drivetrain.followPath(pathChain),
                drivetrain.drive()
            )
        )

        this.bindSpawn(
            this.risingEdge { this.gamepad1.right_bumper },
               exec { drivetrain.setSpeed(.2) }
            )


        this.bindSpawn(
            this.risingEdge({ !this.gamepad1.right_bumper && this::inLoop.get()}) ,
            exec { drivetrain.setSpeed(1.0) }
        )


        this.bindSpawn(
            this.risingEdge({ this.gamepad2.x }),
            Channels.send({Intake.Actions.FORWARD}, {intake.spin.tx} )        )

        this.bindSpawn(
            this.risingEdge({ !this.gamepad2.x && this::inLoop.get()}),
            Channels.send({Intake.Actions.RELEASE}, {intake.spin.tx} )

        )
        this.bindSpawn(
            this.risingEdge({ this.gamepad2.circle }),
            Channels.send({Intake.Actions.BACK}, {intake.spin.tx} )
        )



        this.dropToScheduler()
    }
}