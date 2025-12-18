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
import dev.nextftc.hardware.impl.ServoEx
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain
import org.firstinspires.ftc.teamcode.subsystems.Intake
import org.firstinspires.ftc.teamcode.util.Constants

@Suppress("unused")
object TestingIntake {

    val intakeTesting: RegisterableProgram = Mercurial.teleop {
        waitUntil(this::inLoop)
        val startPose = Pose(18.608, 119.523, Math.toRadians(0.0))
        var position = 0.5
        val drivetrain = Drivetrain(this.hardwareMap, this.gamepad1, Pose(18.608, 119.523, Math.toRadians(-35.0)))
        val follower = drivetrain.follower
        val transferServo: ServoEx = ServoEx(this.hardwareMap.get(com.qualcomm.robotcore.hardware.Servo::class.java,
            Constants.spindexerConstants.spindexerServo))

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
            risingEdge { gamepad2.square },
            Channels.send({ Intake.Actions.FORWARD }, { intake.spin.tx })
        )

        bindSpawn(
            risingEdge { !gamepad2.square },
            Channels.send({ Intake.Actions.RELEASE }, { intake.spin.tx })
        )

        bindSpawn(
            risingEdge { gamepad2.circle },
            Channels.send({ Intake.Actions.BACK }, { intake.spin.tx })
        )
        bindSpawn(
                risingEdge { gamepad2.dpad_right },
                exec { transferServo.position = Constants.spindexerConstants.thirdSlotPos }
        )
        bindSpawn(
            risingEdge { gamepad2.dpad_left },
            exec { transferServo.position = Constants.spindexerConstants.firstSlotPos }

        )
        bindSpawn(
            risingEdge { gamepad2.dpad_up },
            exec { transferServo.position = Constants.spindexerConstants.secondSlotPos }

        )



        schedule(intake.spin)

        schedule(loop(exec {
            telemetry.addData("Mercurial", scheduler)
            telemetry.addData("Position", position)
            telemetry.update()

        }))


        waitForStart()
        schedule(dtLoop)

        this.dropToScheduler()
    }
}