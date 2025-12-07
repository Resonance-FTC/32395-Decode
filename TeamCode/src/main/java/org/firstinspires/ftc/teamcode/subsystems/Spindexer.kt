package org.firstinspires.ftc.teamcode.subsystems


import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.NormalizedRGBA
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx
import dev.frozenmilk.dairy.mercurial.continuations.Actors
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import org.firstinspires.ftc.teamcode.util.AnalogCrServo
import org.firstinspires.ftc.teamcode.util.Constants

class Spindexer(hardwareMap: HardwareMap) {
    private val spindexerServo: AnalogCrServo = AnalogCrServo(hardwareMap,
        Constants.spindexerConstants.spindexerServo,
        Constants.spindexerConstants.spindexerAnalog,
        Constants.spindexerConstants.coefficients)

    private val colorSensor: NormalizedColorSensor = hardwareMap.get(NormalizedColorSensor::class.java, "colorSensor")


    enum class Slot {
        FIRST,
        SECOND,
        THIRD
    }
    enum class Actions {
        FIRSTSLOT,
        SECONDSLOT,
        THIRDSLOT
    }

    val spin = Actors.Actor<Slot, Actions>(
        initializer = { Slot.FIRST },
        messageHandler = { _, message ->
            when (message) {
                Actions.FIRSTSLOT -> {
                    Slot.FIRST
                }

                Actions.SECONDSLOT -> {
                    Slot.SECOND
                }

                Actions.THIRDSLOT -> {
                    Slot.THIRD
                }
            }
        },
        automata = { stateRegister ->
            val state by stateRegister
            Continuations.exec {
                spindexerServo.updateServo()
                val color:NormalizedRGBA = colorSensor.normalizedColors
                if (color.green > Constants.spindexerConstants.greenColorThreshold) {
                    when(state) {
                        Slot.FIRST -> {

                        }

                        Slot.SECOND -> {

                        }

                        Slot.THIRD -> {

                        }
                    }
                }
                when (state) {
                    Slot.FIRST -> {
                        spindexerServo.setPosition(Constants.spindexerConstants.firstSlotPos)
                    }

                    Slot.SECOND -> {
                        spindexerServo.setPosition(Constants.spindexerConstants.secondSlotPos)
                    }

                    Slot.THIRD -> {
                        spindexerServo.setPosition(Constants.spindexerConstants.thirdSlotPos)
                    }
                }
            }
        },
        name = "intake"
    )
}