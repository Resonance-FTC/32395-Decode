package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.continuations.Continuation;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations;
import dev.frozenmilk.dairy.mercurial.continuations.Fiber;

public class Drive {
    private final DcMotorEx fL;
    private final DcMotorEx fR;
    private final DcMotorEx bL;
    private final DcMotorEx bR;
    public Closure driveClosure;

    public Drive(Gamepad gamepad, HardwareMap hardwareMap) {
        this.fL = hardwareMap.get(DcMotorEx.class, "frontLeft");
        this.fR = hardwareMap.get(DcMotorEx.class, "frontRight");
        this.bL = hardwareMap.get(DcMotorEx.class, "backLeft");
        this.bR = hardwareMap.get(DcMotorEx.class, "backRight");

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
