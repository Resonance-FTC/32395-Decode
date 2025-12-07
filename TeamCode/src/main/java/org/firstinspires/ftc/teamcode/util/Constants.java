package org.firstinspires.ftc.teamcode.util;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import dev.nextftc.control.feedback.PIDCoefficients;


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
        public static String hoodServoID = "hoodServo";
        public static double shooterTicksPerRevolution = 384.5;
        public static double turretP = 1;
        public static double turretI = 0;
        public static double turretD = 0;
    }
    public static class intakeConstatnts {
        public static String intakeMotorID = "intakeMotor";
    }

    public static class spindexerConstants {
        public static String spindexerServo = "spindexerServo";
        public static String spindexerAnalog = "spindexerAnalog";
        public static PIDCoefficients coefficients = new PIDCoefficients(1,0,0);
        public static int firstSlotPos = 0;
        public static int secondSlotPos = 120;
        public static int thirdSlotPos = 240;
        public static double greenColorThreshold = 0.5;
        public static double redColorThreshold = 0.5;
        public static double blueColorThreshold = 0.5;

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
        public static double mass = 6;
    }
    public enum AutoOptions {
        BOTTOMFULL,
        TOPFULL,
        PARKONLY,
        BOTTOMTWOONLY,
        TOPTWOONLY
    }

    public enum BallColors {
        GREEN,
        PURPLE,
        NONE
    }
}
