package org.firstinspires.ftc.teamcode.util;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import dev.nextftc.ftc.ActiveOpMode;

public class Constants {
    public static class DriveConstants{
        public static String FLMotorID = "motor-fleft";
        public static String FRMotorID = "motor-fr";
        public static String BLMotorID = "motor-bl";
        public static String BRMotorID = "motor-br";
    }

    public static class electronicsConstants{
        public static String odoID = "odo";
        public static String LimelightID = "limelight";
    }
    @Configurable
    public static class shooterConstants{
        public static String shooterMotorID = "shooterMotor";
        public static String turretMotorID = "turretMotor";
        public static double turretP = 1;
        public static double turretI = 0;
        public static double turretD = 0;
    }

    public enum AllianceColors {
        BLUE,
        RED
    }
    public static class fieldConstants {
        public static int BLUEAPRILTAGID = 21;

        public static int REDAPRILTAGID = 22;
    }

    public static class robotConstants {
        //Must be in KG
        public static double mass = 20;
    }
}
