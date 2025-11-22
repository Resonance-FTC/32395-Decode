package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.util.Constants;

import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations;

public class turret {

    public DcMotorEx turretMotor;
    public Closure turretClosure;
    public ServoImplEx hoodServo;

    public turret(HardwareMap hardwareMap) {
        this.turretMotor = hardwareMap.get(DcMotorEx.class, Constants.shooterConstants.turretMotorID);
        Teleoplocalization localization = new Teleoplocalization(hardwareMap);


        turretClosure = Continuations.exec(() -> {
            // Red Goal is at X=-58.3727 Y=55.6425 Z=29.5
            int motorPosition = turretMotor.getCurrentPosition();

        });
    }
}
