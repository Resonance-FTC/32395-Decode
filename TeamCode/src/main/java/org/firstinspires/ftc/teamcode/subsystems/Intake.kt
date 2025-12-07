package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import org.firstinspires.ftc.teamcode.util.Constants

class Intake(hardwareMap: HardwareMap) {
    private val intakeMotor: CachingDcMotorEx = hardwareMap.get(CachingDcMotorEx::class.java, Constants.intakeConstatnts.intakeMotorID)

    enum class State {
        INTAKING,
        OUTTAKING,
        STOPPED
    }
    enum class Actions {
        FORWARD,
        BACK,
        RELEASE
    }

    val spin = Actors.Actor<State, Actions>(
        initializer = { State.STOPPED },
        messageHandler = { _, message ->
            when (message) {
                Actions.FORWARD -> {
                    State.INTAKING
                }

                Actions.BACK -> {
                    State.OUTTAKING
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
                    State.INTAKING -> {
                        intakeMotor.power = 1.0
                    }

                    State.OUTTAKING -> {
                        intakeMotor.power = -1.0
                    }

                    State.STOPPED -> {
                        intakeMotor.power = 0.0
                    }
                }
            }
        },
        name = "intake"
    )

}