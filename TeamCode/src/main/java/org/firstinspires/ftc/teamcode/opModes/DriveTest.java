package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "Drive Test")
public class DriveTest extends NextFTCOpMode {
    {
        addComponents(
                new SubsystemComponent(Drivetrain.INSTANCE)
        );
    }

    @Override public void onInit() {
        Drivetrain.INSTANCE.setDefaultCommand(Drivetrain.INSTANCE.drive(gamepad1, false));
    }
    @Override public void onWaitForStart() { Drivetrain.INSTANCE.odo.resetPosAndIMU(); }
    @Override public void onStartButtonPressed() { }
    @Override public void onUpdate() {
        telemetry.addData("Left Stick X:", gamepad1.left_stick_x);
        telemetry.addData("Left Stick Y:", gamepad1.left_stick_y);
        telemetry.addData("Right Stick X:",gamepad1.right_stick_x);
        telemetry.update();


    }
    @Override public void onStop() { }
}