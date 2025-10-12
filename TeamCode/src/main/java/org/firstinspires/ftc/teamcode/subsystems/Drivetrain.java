package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import java.util.function.Supplier;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.components.Component;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.Constants.DriveConstants;
public class Drivetrain implements Component {
    public static final Drivetrain INSTANCE = new Drivetrain();
    //start hardware declaration
    private final GoBildaPinpointDriver imu = ActiveOpMode.hardwareMap().get(GoBildaPinpointDriver.class, "odo"); // create IMU instance
    private final MotorEx frontLeftMotor = new MotorEx(DriveConstants.FLMotorID);
    private final MotorEx frontRightMotor = new MotorEx(DriveConstants.FRMotorID);
    private final MotorEx backLeftMotor = new MotorEx(DriveConstants.BLMotorID);
    private final MotorEx backRightMotor = new MotorEx(DriveConstants.BRMotorID);


    //end hardware declaration

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