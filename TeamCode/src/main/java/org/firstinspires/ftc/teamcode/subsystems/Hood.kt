package org.firstinspires.ftc.teamcode.subsystems

import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx
import dev.frozenmilk.dairy.cachinghardware.CachingServo
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import dev.nextftc.control.ControlSystem
import dev.nextftc.control.KineticState
import dev.nextftc.hardware.impl.ServoEx
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.util.Constants
import kotlin.math.pow
import kotlin.math.sqrt

class Hood(hardwareMap: HardwareMap, spindexer: Spindexer, follower: Follower, allianceColors: Constants.AllianceColors) {
    private val hoodServo: CachingServo = CachingServo(hardwareMap.get(Servo::class.java, Constants.shooterConstants.shooterMotorID))
    var targetingPosition: Double = 0.0
    enum class State {
        SHOOTING,
        STOPPED
    }

    enum class Actions {
        SHOOT,
        RELEASE
    }

    val spin = Actors.Actor<State, Actions>(
        initializer = { State.STOPPED },
        messageHandler = { _, message ->
            when (message) {
                Actions.SHOOT -> {
                    State.SHOOTING
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
                        hoodServo.position = targetingPosition
                    }
                    State.STOPPED -> {
                        spindexer.setShooting(false)
                        hoodServo.position = 0.0
                    }
                }
            }
        },
    )

}