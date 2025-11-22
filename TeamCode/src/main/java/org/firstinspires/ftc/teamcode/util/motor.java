package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import dev.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "motorForward")
public class motor extends NextFTCOpMode {
    public DcMotorEx motor;

    @Override public void onStartButtonPressed() {
         motor = hardwareMap.get(DcMotorEx.class, "motor");

        motor.setPower(1);
    }
}