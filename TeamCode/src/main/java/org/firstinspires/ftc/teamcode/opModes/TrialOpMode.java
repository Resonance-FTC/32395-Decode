package org.firstinspires.ftc.teamcode.opModes;

import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec;
import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop;
import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence;
import static dev.frozenmilk.dairy.mercurial.continuations.Continuations.waitUntil;


import dev.frozenmilk.dairy.mercurial.ftc.Mercurial;

@SuppressWarnings("unused")
public class TrialOpMode {
    private static class State {
        double throttle = 1.0;
    }
    public static final Mercurial.RegisterableProgram myFirstMercurialTeleOp = Mercurial.teleop(ctx -> {
        // make sure we have some new state
        State state = new State();
        Drive drive = new Drive(ctx.gamepad1(), ctx.hardwareMap());
        Flywheel flywheel = new Flywheel(ctx.hardwareMap());
        // POV drive
        ctx.schedule(
                sequence(
                        // wait can also take a boolean supplier,
                        // we'll start this process now,
                        // but it will wait until we press play to actually start running
                        waitUntil(ctx::inLoop),
                        loop(
                            drive.driveClosure
                        )
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

        ctx.bindSpawn(
                ctx.risingEdge(() -> ctx.gamepad2().right_bumper ),
                exec(() -> flywheel.handleAction(Flywheel.Actions.SPIN))
        );

        ctx.dropToScheduler();
    });
}