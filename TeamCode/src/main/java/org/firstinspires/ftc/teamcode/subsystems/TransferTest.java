package org.firstinspires.ftc.teamcode.subsystems;

import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.*;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import dev.frozenmilk.dairy.mercurial.continuations.Closure;

@Configurable
@TeleOp
public class TransferTest extends LinearOpMode {
    public static double endpos = 1.0;
    public static double begpos = 0.0;
    public Servo transferServo;
    public enum State {
        UP,
        DOWN
    }
    State state = State.DOWN;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                state = State.UP;
            } else if (gamepad1.b) {
                state = State.DOWN;
            }

            switch (state) {
                case UP:
                    transferServo.setPosition(begpos);
                    break;
                case DOWN:
                    transferServo.setPosition(endpos);
                    break;
            }
        }
    }

}