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

    @Override public void onInit() { }
    @Override public void onWaitForStart() { Drivetrain.INSTANCE.odo.resetPosAndIMU(); }
    @Override public void onStartButtonPressed() { }
    @Override public void onUpdate() {
        telemetry.addData("Left Stick X:", Gamepads.gamepad1().leftStickX());
        telemetry.addData("Left Stick Y:", Gamepads.gamepad1().leftStickY());
        telemetry.addData("Right Stick X:", Gamepads.gamepad1().rightStickX());


    }
    @Override public void onStop() { }
}