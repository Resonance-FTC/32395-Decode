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
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial
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
class FullTeleop {
    private val startPose = Pose(18.608, 119.523, Math.toRadians(-35.0))

    val fullTeleopTest: RegisterableProgram = Mercurial.teleop {
        val drivetrain = Drivetrain(this.hardwareMap, this.gamepad1, startPose)
        val follower = drivetrain.follower

        val turret = Turret(this.hardwareMap, Constants.AllianceColors.BLUE, follower)
        val spindexer = Spindexer(this.hardwareMap)
        val flywheel = Flywheel(this.hardwareMap, spindexer)
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
                exec { flywheel.spin.tx.send(Flywheel.Actions.IDLE) },
                exec { follower.startTeleopDrive() },
                parallel(
                    loop(
                        turret.targetLockClosure
                    )
                )

            )
        )

        this.bindExec(
            { this.gamepad1.aWasPressed() },
            sequence(
                drivetrain.followPath(pathChain),
                drivetrain.drive()
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


        this.bindSpawn(
            this.risingEdge({ this.gamepad2.a }),
            exec { intake.spin.tx.send(Intake.Actions.FORWARD) }
        )

        this.bindSpawn(
            this.risingEdge({ !this.gamepad2.a }),
            exec { intake.spin.tx.send(Intake.Actions.RELEASE) }
        )
        this.bindSpawn(
            this.risingEdge({ this.gamepad2.b }),
            exec { intake.spin.tx.send(Intake.Actions.BACK) }
        )

        this.bindSpawn(
            this.risingEdge({ this.gamepad2.right_bumper }),
            exec { flywheel.spin.tx.send(Flywheel.Actions.SHOOT) }
        )

        this.bindSpawn(
            this.risingEdge { !this.gamepad2.right_bumper },
            exec { flywheel.spin.tx.send(Flywheel.Actions.IDLE) }
        )

        this.bindSpawn(
            this.risingEdge { !this.gamepad2.dpad_left },
            exec {
                spindexer.getPurpleAction()?.let { spindexer.spin.tx.send(it) }
            })

        this.bindSpawn(
            this.risingEdge { !this.gamepad2.dpad_right },
            exec {
                spindexer.getGreenAction()?.let { spindexer.spin.tx.send(it) }
            })


        this.dropToScheduler()
    }
}