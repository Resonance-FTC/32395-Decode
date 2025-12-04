package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class AnalogCrServo {
    public CRServo servo;
    public AnalogInput analogInput;

    public AnalogCrServo(HardwareMap hardwareMap, String servoName, String analogInputName){
        this.servo = hardwareMap.get(CRServo.class, servoName);
        this.analogInput = hardwareMap.get(AnalogInput.class, analogInputName);
    }

    public void setPower(Double power){
        servo.setPower(power);
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
}
