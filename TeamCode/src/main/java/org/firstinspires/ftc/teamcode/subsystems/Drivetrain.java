package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.Constants.DriveConstants;

import dev.frozenmilk.dairy.mercurial.continuations.Actors.Actor;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations;

public class Drivetrain {
    interface state {};
    interface action {};

    class Stopped implements state {};
    class Driving implements state {};
    class Drive implements action {};
    class Stop implements action {};
    Gamepad gamepad;
    private DcMotorEx fL;
    private DcMotorEx fR;
    private DcMotorEx bL;
    private DcMotorEx bR;
    public Drivetrain(HardwareMap hardwareMap, Gamepad gamepad) {

        this.gamepad = gamepad;

        fL = hardwareMap.get(DcMotorEx.class, DriveConstants.FLMotorID);
        fR = hardwareMap.get(DcMotorEx.class, DriveConstants.FRMotorID);
        bL = hardwareMap.get(DcMotorEx.class, DriveConstants.BLMotorID);
        bR = hardwareMap.get(DcMotorEx.class, DriveConstants.BRMotorID);

    }

    Actor<state, action> drive = new Actor<state, action>(
            "Drivetrain Actor",
            Stopped::new,
            (currentState, receivedAction) -> {
                    if (receivedAction instanceof Drive) {
                        return new Driving();
                    }
                    else if (receivedAction instanceof Stop) {
                        return new Stopped();
                    }
                    else {
                        return currentState;
                    }
            },
            (stateRegister) -> {
                return Continuations.exec(()-> {
                    state stateValue = stateRegister.get();
                    if (stateValue instanceof Driving) {
                        double y = -gamepad.left_stick_y;
                        double x = gamepad.left_stick_x;
                        double h = -gamepad.right_stick_x;

                        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(h), 1.0);
                        double frontLeftPower  = (y + x + h) / denominator;
                        double backLeftPower   = (y - x + h) / denominator;
                        double frontRightPower = (y - x - h) / denominator;
                        double backRightPower  = (y + x - h) / denominator;

                        fL.setPower(frontLeftPower);
                        bL.setPower(backLeftPower);
                        fR.setPower(frontRightPower);
                        bR.setPower(backRightPower);
                    } else if (stateValue instanceof Stopped) {
                        fL.setPower(0);
                        fR.setPower(0);
                        bL.setPower(0);
                        bR.setPower(0);
                    }
                }
                );
            }
    );
}

