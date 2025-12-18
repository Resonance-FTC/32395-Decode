package org.firstinspires.ftc.teamcode.subsystems

import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import dev.nextftc.control.ControlSystem
import dev.nextftc.control.KineticState
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.util.Constants
import kotlin.math.pow
import kotlin.math.sqrt

class Flywheel(hardwareMap: HardwareMap, spindexer: Spindexer, follower: Follower, allianceColors: Constants.AllianceColors, ) {
    private val shooterMotor: CachingDcMotorEx = CachingDcMotorEx(hardwareMap.get(DcMotorEx::class.java, Constants.shooterConstants.shooterMotorID))
    val goalX: Double = if (allianceColors == Constants.AllianceColors.BLUE) (72 - 58).toDouble() else (72 + 58).toDouble()

    val pidController: ControlSystem = ControlSystem.builder().velPid(1.0,0.0,0.0).basicFF(1.0,0.0,0.0).build()

    fun init() {
        shooterMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
    }

    enum class State {
        SHOOTING,
        IDLEING,
        STOPPED
    }

    enum class Actions {
        SHOOT,
        IDLE,
        RELEASE
    }

    val spin = Actors.Actor<State, Actions>(
        initializer = { State.STOPPED },
        messageHandler = { _, message ->
            when (message) {
                Actions.SHOOT -> {
                    State.SHOOTING
                }

                Actions.IDLE -> {
                    State.IDLEING
                }

                Actions.RELEASE -> {
                    State.STOPPED
                }
            }
        },
        automata = { stateRegister ->
            val state by stateRegister
            Continuations.exec {
                when (state) {
                    State.SHOOTING -> {
                        spindexer.setShooting(true)
                        val dist: Double = sqrt((goalX-follower.pose.x).pow(2) + (135-follower.pose.y).pow(2)) // Current distance from the goal
                        val values: DoubleArray = Aimbot.getValues(dist)
                        setTargetVel(values[1])
                        shooterMotor.power = updateShooterPower()
                    }

                    State.IDLEING -> {
                        spindexer.setShooting(false)
                        pidController.goal = KineticState(2500.0)
                        shooterMotor.power = updateShooterPower()
                    }

                    State.STOPPED -> {
                        spindexer.setShooting(false)
                        shooterMotor.power = 0.0
                    }
                }
            }
        },
    )
    fun updateShooterPower(): Double {
        return pidController.calculate(KineticState(shooterMotor.getVelocity(AngleUnit.DEGREES)))
    }

    fun setTargetVel(target:Double) {
        pidController.goal = KineticState(target)
    }

    fun calculateShooterPower(): Double {
        return pidController.calculate(KineticState(shooterMotor.getVelocity(AngleUnit.DEGREES)))
    }
}