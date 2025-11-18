package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Drive Test")
public class DriveTest extends NextFTCOpMode {
    public DriveTest() {
        addComponents(
                new SubsystemComponent(Drivetrain.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );

    }
    @Override public void onInit() {

    }
    @Override public void onWaitForStart() {

    }
    @Override public void onStartButtonPressed() {
        Drivetrain.INSTANCE.drive(gamepad1, false).schedule();
    }
    @Override public void onUpdate() {
        telemetry.addData("Left Stick X:", gamepad1.left_stick_x);
        telemetry.addData("Left Stick Y:", gamepad1.left_stick_y);
        telemetry.addData("Right Stick X:",gamepad1.right_stick_x);
        telemetry.update();

    }
    @Override public void onStop() { }
}