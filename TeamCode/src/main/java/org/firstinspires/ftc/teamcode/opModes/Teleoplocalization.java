package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.util.Constants;

import dev.frozenmilk.dairy.mercurial.continuations.Closure;
import dev.frozenmilk.dairy.mercurial.continuations.Continuations;

public class Teleoplocalization {

    public Limelight3A limelight;
    public GoBildaPinpointDriver pinpoint;
    public Closure LocalizationClosure;
    public Teleoplocalization(HardwareMap hardwareMap) {
        this.limelight = hardwareMap.get(Limelight3A.class, Constants.electronicsConstants.LimelightID);
        this.pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Constants.electronicsConstants.odoID);

        limelight.pipelineSwitch(0);
        limelight.start();

        LocalizationClosure = Continuations.exec(() -> {
            Pose3D limelightPose = calculateLimelightPose();
            Pose2D pinpointPose = pinpoint.getPosition();


        });
    }

    public Pose3D calculateLimelightPose() {
        limelight.updateRobotOrientation(pinpoint.getHeading(AngleUnit.DEGREES));
        pinpoint.update();
        pinpoint.getPosition();
        LLResult result = limelight.getLatestResult();
        if (result != null){
            if (result.isValid()) {
                return result.getBotpose_MT2();
            }
        }
        return(null);
    }
    public Pose2D getPose(){
        return pinpoint.getPosition();
    }
}
