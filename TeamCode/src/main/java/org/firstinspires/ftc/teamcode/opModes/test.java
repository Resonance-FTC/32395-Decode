package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.rowanmcalpin.nextftc.ftc.OpModeData;

import org.firstinspires.ftc.teamcode.util.Constants;

import dev.nextftc.hardware.impl.MotorEx;

public class test extends LinearOpMode {



    @Override
    public void runOpMode() throws InterruptedException {
        MotorEx frontLeftMotor  = OpModeData.hardwareMap.get(MotorEx .class, Constants.DriveConstants.FLMotorID);
        MotorEx frontRightMotor = OpModeData.hardwareMap.get(MotorEx.class, Constants.DriveConstants.FRMotorID);
        MotorEx backLeftMotor   = OpModeData.hardwareMap.get(MotorEx.class, Constants.DriveConstants.BLMotorID);
        MotorEx backRightMotor  = OpModeData.hardwareMap.get(MotorEx.class, Constants.DriveConstants.BRMotorID);
        waitForStart();
    }
}
