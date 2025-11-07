package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.opmodecontrol.OpModeDetails;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.util.Constants.DriveConstants;
import java.util.function.Supplier;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.components.Component;
import dev.nextftc.core.subsystems.Subsystem;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Drivetrain implements Subsystem {
    public static final Drivetrain INSTANCE = new Drivetrain();

    //start hardware declaration
    private GoBildaPinpointDriver imu ;
    private MotorEx frontLeftMotor;
    private MotorEx frontRightMotor;
    private MotorEx backLeftMotor;
    private MotorEx backRightMotor;


    //end hardware declaration - hello :)

    //begin hardware initialization


    @Override
    public void initialize() {
        frontLeftMotor  = OpModeData.hardwareMap.get(MotorEx.class, DriveConstants.FLMotorID);
        frontRightMotor = OpModeData.hardwareMap.get(MotorEx.class, DriveConstants.FRMotorID);
        backLeftMotor   = OpModeData.hardwareMap.get(MotorEx.class, DriveConstants.BLMotorID);
        backRightMotor  = OpModeData.hardwareMap.get(MotorEx.class, DriveConstants.BRMotorID);

        imu = OpModeData.hardwareMap.get(GoBildaPinpointDriver.class, "odo");
    }

    public Drivetrain getInstance(){
        return INSTANCE;
    }

    //start command declaration
    public Command drive(Gamepad gamepad1, boolean isFieldCentric) {
        return new LambdaCommand()
            .setUpdate(() -> {

                double y = gamepad1.left_stick_y;
                double x = gamepad1.left_stick_x;
                double rx = gamepad1.right_stick_x;

                if (gamepad1.aWasReleased()) {
                    imu.recalibrateIMU(); //recalibrates the IMU without resetting position
                }

                //double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
                double botHeading = imu.getHeading(AngleUnit.RADIANS);

                // Rotate the movement direction counter to the bot's rotation
                double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
                double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
                rotX *= 1.1;


                if (isFieldCentric) {
                    double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
                    double frontLeftPower = (rotY + rotX + rx) / denominator;
                    double backLeftPower = (rotY - rotX + rx) / denominator;
                    double frontRightPower = (rotY - rotX - rx) / denominator;
                    double backRightPower = (rotY + rotX - rx) / denominator;

                    frontLeftMotor.setPower(frontLeftPower);
                    backLeftMotor.setPower(backLeftPower);
                    frontRightMotor.setPower(frontRightPower);
                    backRightMotor.setPower(backRightPower);
                }
                else {
                    double denominator2 = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                    double frontLeftPower2 = (y + x + rx) / denominator2;
                    double backLeftPower2 = (y - x + rx) / denominator2;
                    double frontRightPower2 = (y - x - rx) / denominator2;
                    double backRightPower2 = (y + x - rx) / denominator2;

                    frontLeftMotor.setPower(frontLeftPower2);
                    backLeftMotor.setPower(backLeftPower2);
                    frontRightMotor.setPower(frontRightPower2);
                    backRightMotor.setPower(backRightPower2);
                }

            })
            .setIsDone(() -> false)
            .setInterruptible(true)
            .named("Drive Command");
    //end command declaration
}}