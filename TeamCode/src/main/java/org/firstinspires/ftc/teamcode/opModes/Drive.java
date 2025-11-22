package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.CachingDcMotorEx;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.continuations.Continuation;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations;
import dev.frozenmilk.dairy.mercurial.continuations.Fiber;

public class Drive {
    private final CachingDcMotorEx fL;
    private final CachingDcMotorEx fR;
    private final CachingDcMotorEx bL;
    private final CachingDcMotorEx bR;
    public Closure driveClosure;

    public Drive(Gamepad gamepad, HardwareMap hardwareMap) {
        this.fL = hardwareMap.get(CachingDcMotorEx.class, Constants.DriveConstants.FLMotorID);
        this.fR = hardwareMap.get(CachingDcMotorEx.class, Constants.DriveConstants.FRMotorID);
        this.bL = hardwareMap.get(CachingDcMotorEx.class, Constants.DriveConstants.BLMotorID);
        this.bR = hardwareMap.get(CachingDcMotorEx.class, Constants.DriveConstants.BRMotorID);

        driveClosure = Continuations.exec(() -> {
            double y = -gamepad.left_stick_y;
            double x = gamepad.left_stick_x;
            double h = gamepad.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(h), 1.0);
            double frontLeftPower  = (y + x + h) / denominator;
            double backLeftPower   = (y - x + h) / denominator;
            double frontRightPower = (y - x - h) / denominator;
            double backRightPower  = (y + x - h) / denominator;

            fL.setPower(frontLeftPower);
            bL.setPower(backLeftPower);
            fR.setPower(frontRightPower);
            bR.setPower(backRightPower);
        });
    }
}
