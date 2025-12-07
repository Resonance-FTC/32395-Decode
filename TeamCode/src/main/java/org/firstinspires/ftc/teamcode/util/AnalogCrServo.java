package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.control.feedback.FeedbackElement;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDController;

public class AnalogCrServo {
    public CRServo servo;
    public AnalogInput analogInput;
    public ControlSystem pidController;
    public double target = 0;
    public enum Mode {
        MOTOR,
        SERVO
    }
    public Mode currentMode = Mode.SERVO;
    public AnalogCrServo(HardwareMap hardwareMap, String servoName, String analogInputName, PIDCoefficients coefficients){
        this.servo = hardwareMap.get(CRServo.class, servoName);
        this.analogInput = hardwareMap.get(AnalogInput.class, analogInputName);
        pidController = ControlSystem.builder()
                .angular(AngleType.DEGREES,
                        feedback -> feedback.posPid(coefficients))
                .build();

    }
    public void setPower(Double power){
        currentMode = Mode.MOTOR;
        servo.setPower(power);
    }
    public void setPosition(int Position) {
        currentMode = Mode.SERVO;
        if (Position != target) {
            target = Position;
            pidController.setGoal(new KineticState(Position));
        }
    }

    public void updateServo() {
        if (currentMode == Mode.SERVO) {
            setPower(pidController.calculate(new KineticState(getPosition())));
        }
    }
    public void setDirection(DcMotorSimple.Direction direction){
        servo.setDirection(direction);
    }
    public double getPosition(){
        return analogInput.getVoltage() / 3.3 * 360;
    }

    public CRServo getServo(){
        return servo;
    }
    public AnalogInput getAnalogInput() {
        return analogInput;
    }
    public double getTarget(){
        return(target);
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

}
