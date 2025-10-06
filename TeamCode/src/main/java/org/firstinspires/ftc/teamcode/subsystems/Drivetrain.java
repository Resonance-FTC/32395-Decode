package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.util.constants.DriveConstants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Drivetrain implements Subsystem {
    // put hardware, commands, etc here

    MotorEx frontLeft = new MotorEx(DriveConstants.frontLeftMotorID);
    MotorEx frontRight = new MotorEx(DriveConstants.frontRightMotorID);
    MotorEx backLeft = new MotorEx(DriveConstants.backLeftMotorID);
    MotorEx backRight = new MotorEx(DriveConstants.backRightMotorID);

    @Override
    public void initialize() {
        // initialization logic (runs on init)
    }

    @Override
    public void periodic() {
        // periodic logic (runs every loop)
    }

    Command drive = new LambdaCommand()
            .setStart(() -> {
                // Runs on start
            })
            .setUpdate(() -> {
                // Runs on update
            })
            .setStop(interrupted -> {
                // Runs on stop
            })
            .setIsDone(() -> true) // Returns if the command has finished
            .requires(/* subsystems the command implements */)
            .setInterruptible(true)
            .named("My Command"); // sets the name of the command; optional
}