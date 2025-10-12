package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import dev.nextftc.core.components.Component;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.MotorEx;
public class Drivetrain implements Component {
    public static Component INSTANCE;

    public Drivetrain() {
        INSTANCE =new Drivetrain();
    }
    //start hardware declaration
    private final MotorEx frontLeftMotor = new MotorEx("front_left");
    private final MotorEx frontLeftMotor = new MotorEx("front_left");
    private final MotorEx frontLeftMotor = new MotorEx("front_left");
    private final MotorEx frontLeftMotor = new MotorEx("front_left");


    //end hardware declaration

    //start command declaration

    //end command declaration
}
