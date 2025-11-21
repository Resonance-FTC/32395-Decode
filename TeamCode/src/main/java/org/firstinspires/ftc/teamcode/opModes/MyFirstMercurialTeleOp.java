package org.firstinspires.ftc.teamcode.opModes;

import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec;
import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop;
import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence;
import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.waitUntil;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

import dev.frozenmilk.dairy.mercurial.ftc.Mercurial;

@SuppressWarnings("unused")
public class MyFirstMercurialTeleOp {
    private static class State {
        double throttle = 1.0;
    }
    public static final Mercurial.RegisterableProgram myFirstMercurialTeleOp = Mercurial.teleop(ctx -> {
        // make sure we have some new state
        State state = new State();
        Drivetrain drivetrain = new Drivetrain(ctx.gamepad1(), ctx.hardwareMap());

        DcMotorEx fl = ctx.hardwareMap().get(DcMotorEx.class, "fl");
        DcMotorEx bl = ctx.hardwareMap().get(DcMotorEx.class, "bl");
        DcMotorEx br = ctx.hardwareMap().get(DcMotorEx.class, "br");
        DcMotorEx fr = ctx.hardwareMap().get(DcMotorEx.class, "fr");

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);

        // POV drive
        ctx.schedule(
                sequence(
                        // wait can also take a boolean supplier,
                        // we'll start this process now,
                        // but it will wait until we press play to actually start running
                        waitUntil(ctx::inLoop),
                        loop(exec(() -> {
                            double drive = -ctx.gamepad1().left_stick_y;
                            double turn = ctx.gamepad1().right_stick_x;

                            fl.setPower((drive + turn) * state.throttle);
                            bl.setPower((drive + turn) * state.throttle);
                            br.setPower((drive - turn) * state.throttle);
                            fr.setPower((drive - turn) * state.throttle);
                        }))
                )
        );

        // throttle controls
        ctx.bindSpawn(
                ctx.risingEdge(() -> ctx.gamepad1().right_bumper),
                exec(() -> state.throttle = 0.5)
        );

        ctx.bindSpawn(
                // inverting the condition will convert our rising edge detector to a falling edge detector!
                ctx.risingEdge(() -> !ctx.gamepad1().right_bumper),
                exec(() -> state.throttle = 1.0)
        );

        // TODO: i'd love to increase the size of this example

        ctx.dropToScheduler();
    });
}