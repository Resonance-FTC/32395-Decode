package org.firstinspires.ftc.teamcode.opModes

import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.HeadingInterpolator
import com.pedropathing.paths.Path
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.waitUntil
import dev.frozenmilk.dairy.mercurial.continuations.channels.Channels
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial.RegisterableProgram
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain
import org.firstinspires.ftc.teamcode.subsystems.Intake

@Suppress("unused")
object TestingIntake {

    val intakeTesting: RegisterableProgram = Mercurial.teleop {
        waitUntil(this::inLoop)
        val startPose = Pose(18.608, 119.523, Math.toRadians(0.0))

        val drivetrain = Drivetrain(this.hardwareMap, this.gamepad1, Pose(18.608, 119.523, Math.toRadians(-35.0)))
        val follower = drivetrain.follower
        follower.setStartingPose(startPose)

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

        val dtLoop = loop(exec { drivetrain.drive()}).close()


        this.bindExec(
            { this.gamepad1.xWasPressed() },
            sequence(
                drivetrain.followPath(pathChain)
            )
        )


        this.bindSpawn(
            this.risingEdge { this.gamepad1.right_bumper },
            exec{ drivetrain.setSpeed(.2) }
        )

        this.bindSpawn(
            this.risingEdge({ !this.gamepad1.right_bumper }),
            exec { drivetrain.setSpeed(1.0) }
        )

        bindSpawn(
            risingEdge { gamepad2.x },
            Channels.send({ Intake.Actions.FORWARD }, { intake.spin.tx })
        )

        bindSpawn(
            risingEdge { !gamepad2.x },
            Channels.send({ Intake.Actions.RELEASE }, { intake.spin.tx })
        )

        bindSpawn(
            risingEdge { gamepad2.circle },
            Channels.send({ Intake.Actions.BACK }, { intake.spin.tx })
        )

        schedule(intake.spin)

        schedule(loop(exec {
            telemetry.addData("Mercurial", scheduler)
            telemetry.update()
        }))


        waitForStart()
        schedule(dtLoop)

        this.dropToScheduler()
    }
}