package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.ftc.NextFTCOpMode;

@TeleOp(name = "GamepadTesting")
public class opModes extends NextFTCOpMode {
    {
        addComponents(Drivetrain.INSTANCE, BindingsComponent.INSTANCE);
    }

    @Override public void onInit() { }
    @Override public void onWaitForStart() { }
    @Override public void onStartButtonPressed() {Drivetrain.INSTANCE.drive(gamepad1, true).schedule();}
    @Override public void onUpdate() {
        telemetry.addData("Caption 1", gamepad1.left_stick_y);
        telemetry.update();
    }
    @Override public void onStop() { }
}