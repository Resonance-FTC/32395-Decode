package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import dev.frozenmilk.dairy.cachinghardware.CachingServo
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import org.firstinspires.ftc.teamcode.util.Constants

class Transfer(hardwareMap: HardwareMap) {
private val transferServo: CachingServo = CachingServo(hardwareMap.get(com.qualcomm.robotcore.hardware.Servo::class.java, Constants.transferConstants.transferServoID))
    enum class State {
        CLOSED,
        OPEN,

    }

    var endpos: Double = 1.0

    var begpos: Double = 0.0
    enum class Actions {
        CLOSE,
        OPEN
    }

    val spin = Actors.Actor<State, Actions>(
        initializer = { State.CLOSED },
        messageHandler = { _, message ->
            when (message) {
                Actions.CLOSE -> {
                    State.CLOSED
                }

                Actions.OPEN -> {
                    State.OPEN
                }
            }
        },
        automata = { stateRegister ->
            val state by stateRegister
            Continuations.exec {
                when (state) {
                    State.CLOSED -> {
                        transferServo.position = 1.0
                    }

                    State.OPEN -> {
                        transferServo.position = 0.0
                    }

                }
            }
        },
    )

}