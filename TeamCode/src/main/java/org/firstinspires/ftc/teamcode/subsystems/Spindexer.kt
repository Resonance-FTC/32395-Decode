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
    val ballList: MutableList<Constants.BallColors> = mutableListOf(Constants.BallColors.NONE,Constants.BallColors.NONE,Constants.BallColors.NONE)
    var currentSlot: Slot = Slot.FIRST
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
                            ballList[0] = Constants.BallColors.GREEN
                        }

                        Slot.SECOND -> {
                            ballList[1] = Constants.BallColors.GREEN

                        }

                        Slot.THIRD -> {
                            ballList[2] = Constants.BallColors.GREEN

                        }
                    }
                } else if (color.red > Constants.spindexerConstants.redColorThreshold && color.blue > Constants.spindexerConstants.blueColorThreshold) {
                    when(state) {
                        Slot.FIRST -> {
                            ballList[0] = Constants.BallColors.PURPLE
                        }

                        Slot.SECOND -> {
                            ballList[1] = Constants.BallColors.PURPLE

                        }

                        Slot.THIRD -> {
                            ballList[2] = Constants.BallColors.PURPLE

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
    fun removeCurrentBall() {
        when (currentSlot) {
            Slot.FIRST -> {
                ballList[0] = Constants.BallColors.NONE
            }

            Slot.SECOND -> {
                ballList[1] = Constants.BallColors.NONE

            }

            Slot.THIRD -> {
                ballList[2] = Constants.BallColors.NONE

            }
        }
    }
}