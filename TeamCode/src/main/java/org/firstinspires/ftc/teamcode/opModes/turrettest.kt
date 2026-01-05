package org.firstinspires.ftc.teamcode.opModes

import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.parallel
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.waitUntil
import dev.frozenmilk.dairy.mercurial.continuations.channels.Channels
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial.RegisterableProgram
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial.teleop
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.koltinTurret
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain
import org.firstinspires.ftc.teamcode.subsystems.Flywheel
import org.firstinspires.ftc.teamcode.subsystems.Intake
import org.firstinspires.ftc.teamcode.subsystems.Turret
import org.firstinspires.ftc.teamcode.util.Constants

@Suppress("unused")
object turrettest {
    val startPose = Pose(18.608, 119.523, Math.toRadians(-35.0));

    var turrettest: RegisterableProgram = teleop {
        val drivetrain = Drivetrain(this.hardwareMap, this.gamepad1, startPose)
        val follower = drivetrain.follower
        val turret = koltinTurret(this.hardwareMap, Constants.AllianceColors.BLUE, follower)


        waitForStart()
        bindSpawn(
            risingEdge { gamepad1.dpad_left },
            Channels.send({ koltinTurret.Actions.SPIN }, { turret.spin.tx })
        )
        schedule(turret.spin)
        schedule(
            sequence(waitUntil { inLoop }, loop(parallel(exec { drivetrain.drive() }))))
        this.dropToScheduler()
    }
}