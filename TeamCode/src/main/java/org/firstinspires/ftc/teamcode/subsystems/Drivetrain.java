package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

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
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.impl.MotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Drivetrain implements Subsystem {
    public static final Drivetrain INSTANCE = new Drivetrain();

    //start hardware declaration
    public GoBildaPinpointDriver odo ;
    private MotorEx frontLeftMotor;
    private MotorEx frontRightMotor;
    private MotorEx backLeftMotor;
    private MotorEx backRightMotor;


    //end hardware declaration - hello :)
    private Supplier<Command> defaultCommandSupplier;
    //begin hardware initialization


    @Override
    public void initialize() {
        /*frontLeftMotor  = new MotorEx(DriveConstants.FLMotorID);
        frontRightMotor = new MotorEx(DriveConstants.FRMotorID);
        backLeftMotor   = new MotorEx(DriveConstants.BLMotorID);
        backRightMotor  = new MotorEx(DriveConstants.BRMotorID);

        odo = OpModeData.hardwareMap.get(GoBildaPinpointDriver.class, "odo");
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
          *//*

        NOTE: VALUES POTENTIALLY NEED ADJUSTMENT

        Set the direction that each of the two odometry pods count. The X (forward) pod should
        increase when you move the robot forward. And the Y (strafe) pod should increase when
        you move the robot to the left.
         *//*
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);*/
        //odo.setOffsets(0, 0, DistanceUnit.MM); // Have fun with this one :) https://www.gobilda.com/content/user_manuals/3110-0002-0001%20User%20Guide.pdf

    }
    public void setDefaultCommand(Command command){
        defaultCommandSupplier = () -> command;
    }
    @NonNull
    @Override
    public Command getDefaultCommand() {
        return defaultCommandSupplier.get();
    }

    public Command init() {
        return new LambdaCommand()
                .setStart(() -> {
                    frontLeftMotor  = new MotorEx(DriveConstants.FLMotorID);
                    frontRightMotor = new MotorEx(DriveConstants.FRMotorID);
                    backLeftMotor   = new MotorEx(DriveConstants.BLMotorID);
                    backRightMotor  = new MotorEx(DriveConstants.BRMotorID);

                    odo = ActiveOpMode.hardwareMap().get(GoBildaPinpointDriver.class, "odo");
                    odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
          /*

        NOTE: VALUES POTENTIALLY NEED ADJUSTMENT

        Set the direction that each of the two odometry pods count. The X (forward) pod should
        increase when you move the robot forward. And the Y (strafe) pod should increase when
        you move the robot to the left.
         */
                    odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
                })
                .setIsDone(() -> true)
                .setInterruptible(true)
                .named("Drivetrain Init Command")
                .requires(Drivetrain.INSTANCE);
    }
    @Override
    public void periodic() {
        // periodic logic (runs every loop)
        odo.update();
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

                    //double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
                    double botHeading = odo.getHeading(AngleUnit.RADIANS);

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
                    } else {
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
                .named("Drive Command")
                .requires(Drivetrain.INSTANCE);
    }
    //end command declaration
}