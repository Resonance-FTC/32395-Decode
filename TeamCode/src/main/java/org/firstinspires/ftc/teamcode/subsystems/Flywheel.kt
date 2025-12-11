package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import dev.nextftc.control.ControlSystem
import dev.nextftc.control.KineticState
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.util.Constants

class Flywheel(hardwareMap: HardwareMap, spindexer: Spindexer) {
    private val shooterMotor: CachingDcMotorEx = hardwareMap.get(CachingDcMotorEx::class.java, Constants.shooterConstants.shooterMotorID)

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
        name = "shooting"
    )
    fun updateShooterPower(): Double {
        return pidController.calculate(KineticState(shooterMotor.getVelocity(AngleUnit.DEGREES)))
    }

    fun setTargetVel(target:Double) {
        pidController.goal = KineticState(target)
    }
}