package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Constants.DriveConstants;

import dev.frozenmilk.dairy.mercurial.continuations.Actors.Actor;
import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations;

public class Drivetrain {
    Gamepad gamepad;
    private DcMotorEx fL;
    private DcMotorEx fR;
    private DcMotorEx bL;
    private DcMotorEx bR;

    public Drivetrain(HardwareMap hardwareMap, Gamepad gamepad) {
        this.gamepad = gamepad;

        this.fL = hardwareMap.get(DcMotorEx.class, Constants.DriveConstants.FLMotorID);
        this.fR = hardwareMap.get(DcMotorEx.class, Constants.DriveConstants.FRMotorID);
        this.bL = hardwareMap.get(DcMotorEx.class, Constants.DriveConstants.BLMotorID);
        this.bR = hardwareMap.get(DcMotorEx.class, Constants.DriveConstants.BRMotorID);

        bR.setDirection(DcMotorSimple.Direction.REVERSE);

    }
    Closure driveClosure = Continuations.exec(() -> {
        double y = -gamepad.left_stick_y;
        double x = gamepad.left_stick_x;
        double h = -gamepad.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(h), 1.0);
        double frontLeftPower = (y + x + h) / denominator;
        double backLeftPower = (y - x + h) / denominator;
        double frontRightPower = (y - x - h) / denominator;
        double backRightPower = (y + x - h) / denominator;

        fL.setPower(frontLeftPower);
        bL.setPower(backLeftPower);
        fR.setPower(frontRightPower);
        bR.setPower(backRightPower);
    });
}
