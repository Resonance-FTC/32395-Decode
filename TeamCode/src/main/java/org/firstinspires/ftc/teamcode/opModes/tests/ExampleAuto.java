package org.firstinspires.ftc.teamcode.opModes.tests; // make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.util.Constants;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;

@Autonomous(name = "Example Auto", group = "Examples")
public class ExampleAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    public CachingDcMotorEx intakeMotor;
    private int pathState;
    private PathChain goTopickupFirstLine, shootPreload, pickupFirstLine, shootFirstLine, goToPickupSecondLine, pickupSecondLine, scoreSecondLine, goToPickupThirdLine, pickupThirdLine, scoreThirdLine;
    private Pose startPose = null;
    Constants.AutoOptions choice = Constants.AutoOptions.TOPFULL;
    boolean ChoiceMade = false;
    private final Pose scorePose = new Pose(50, 100);

    private final Pose pickup1Pose = new Pose(29, 100);
    private final Pose goToPickup2Pose = new Pose(48, 75);
    private final Pose pickup2Pose = new Pose(29, 75);
    private final Pose goToPickup3Pose = new Pose(50, 50);

    private final Pose pickup3Pose = new Pose(29, 50);

    public void buildTopFullPaths() {
        shootPreload = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(startPose, scorePose)
                )
                .setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(180), 0.8)
                .build();

        pickupFirstLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(scorePose, pickup1Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        shootFirstLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(pickup1Pose, scorePose)
                )
                .setVelocityConstraint(.025)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        goToPickupSecondLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(scorePose, goToPickup2Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        pickupSecondLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(goToPickup2Pose, pickup2Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        scoreSecondLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(pickup2Pose, scorePose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        goToPickupThirdLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(scorePose, goToPickup3Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        pickupThirdLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(goToPickup3Pose, pickup3Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        scoreThirdLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(pickup3Pose,scorePose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        startPose = new Pose(18.608, 119.523, Math.toRadians(-35));

    }
    public void buildTopTwoPaths() {

        shootPreload = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(startPose, scorePose)
                )
                .setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(180))
                .build();

        pickupFirstLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(scorePose, pickup1Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        shootFirstLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(pickup1Pose, scorePose)
                )
                .setVelocityConstraint(.025)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        goToPickupSecondLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(scorePose, goToPickup2Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        pickupSecondLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(goToPickup2Pose, pickup2Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        scoreSecondLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(pickup2Pose, scorePose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        startPose = new Pose(18.608, 119.523, Math.toRadians(-35));

    }
    public void buildBottomTwoPaths() {

        goTopickupFirstLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(startPose, goToPickup3Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        pickupFirstLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(goToPickup3Pose, pickup3Pose)
                )
                .setVelocityConstraint(.025)
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        shootFirstLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(pickup3Pose, startPose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        goToPickupSecondLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(startPose, goToPickup2Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        pickupSecondLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(goToPickup2Pose, pickup2Pose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
        scoreSecondLine = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(pickup2Pose, startPose)
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        startPose = new Pose(56.5, 8.8, Math.toRadians(90));

    }
    public void autonomousTopFullPathUpdate() {
        if (opmodeTimer.getElapsedTimeSeconds()>=27) {
            telemetry.addData("AutoPark was activated at ", opmodeTimer.getElapsedTimeSeconds());
            PathChain parkPath = follower.pathBuilder() //Lazy Curve Generation
                    .addPath(new Path(new BezierLine(follower::getPose, new Pose(40, 75))))
                    .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(45), 0.8))
                    .build();
            setPathState(-1);
            return;
        }
        switch (pathState) {
            case 0:
                follower.followPath(shootPreload, true);
                setPathState(1);
                break;
            case 1:

            /* You could check for
            - Follower State: "if(!follower.isBusy()) {}"
            - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
            - Robot Position: "if(follower.getPose().getX() > 36) {}"
            */

                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    intakeMotor.setPower(-1);
                    follower.setMaxPower(0.5);
                    /* Score Preload */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(pickupFirstLine,true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(shootFirstLine,true);
                    setPathState(3);
                }
                break;
            case 3:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(goToPickupSecondLine,true);
                    setPathState(4);
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(pickupSecondLine,true);
                    setPathState(5);
                }
                break;
            case 5:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */
                    follower.setMaxPower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(scoreSecondLine,true);
                    setPathState(6);
                }
                break;
            case 6:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */
                    follower.setMaxPower(1);

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(goToPickupThirdLine,true);
                    setPathState(7);
                }
                break;
            case 7:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */
                    follower.setMaxPower(0.5);

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(pickupThirdLine,true);
                    setPathState(8);
                }
                break;
            case 8:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */
                    follower.setMaxPower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(scoreThirdLine,true);
                    setPathState(-1);
                }
                break;
        }
    }
    public void autonomousTopTwoPathUpdate() {
        if (opmodeTimer.getElapsedTimeSeconds()>=27) {
            telemetry.addData("AutoPark was activated at ", opmodeTimer.getElapsedTimeSeconds());
            PathChain parkPath = follower.pathBuilder() //Lazy Curve Generation
                    .addPath(new Path(new BezierLine(follower::getPose, new Pose(40, 75))))
                    .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(45), 0.8))
                    .build();
            setPathState(-1);
            return;
        }
        switch (pathState) {
            case 0:
                follower.followPath(shootPreload, true);
                setPathState(1);
                break;
            case 1:

            /* You could check for
            - Follower State: "if(!follower.isBusy()) {}"
            - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
            - Robot Position: "if(follower.getPose().getX() > 36) {}"
            */

                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {
                    intakeMotor.setPower(-1);
                    follower.setMaxPower(0.5);
                    /* Score Preload */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(pickupFirstLine, true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(shootFirstLine, true);
                    setPathState(3);
                }
                break;
            case 3:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(goToPickupSecondLine, true);
                    setPathState(4);
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(pickupSecondLine, true);
                    setPathState(5);
                }
                break;
            case 5:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */
                    follower.setMaxPower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(scoreSecondLine, true);
                    setPathState(-1);
                }
                break;
        }
    }
    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void autonomousBottomTwoPathUpdate() {
        if (opmodeTimer.getElapsedTimeSeconds()>=27) {
            telemetry.addData("AutoPark was activated at ", opmodeTimer.getElapsedTimeSeconds());
            PathChain parkPath = follower.pathBuilder() //Lazy Curve Generation
                    .addPath(new Path(new BezierLine(follower::getPose, new Pose(40, 75))))
                    .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(45), 0.8))
                    .build();
            setPathState(-1);
            return;
        }
        switch (pathState) {
            case 0:
                setPathState(1);
                break;
            case 1:

            /* You could check for
            - Follower State: "if(!follower.isBusy()) {}"
            - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
            - Robot Position: "if(follower.getPose().getX() > 36) {}"
            */

                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {
                    intakeMotor.setPower(-1);
                    follower.setMaxPower(0.5);
                    /* Score Preload */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(goTopickupFirstLine, true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(pickupFirstLine, true);
                    setPathState(3);
                }
                break;
            case 3:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(shootFirstLine, true);
                    setPathState(4);
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(goToPickupSecondLine, true);
                    setPathState(5);
                }
                break;
            case 5:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */
                    follower.setMaxPower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(pickupSecondLine, true);
                    setPathState(6);
                }
                break;
            case 6:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */
                    follower.setMaxPower(1);
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(scoreSecondLine, true);
                    setPathState(-1);
                }
                break;
        }
    }
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        switch (choice) {
            case TOPFULL:
                autonomousTopFullPathUpdate();
            case TOPTWOONLY:
                autonomousTopTwoPathUpdate();
            case BOTTOMTWOONLY:
                autonomousBottomTwoPathUpdate();
        }

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        intakeMotor = new CachingDcMotorEx(hardwareMap.get(DcMotorEx.class, org.firstinspires.ftc.teamcode.util.Constants.intakeConstatnts.intakeMotorID));
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        follower = org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower(hardwareMap);

    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {
        autoSelectorLoop();
    }

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);

    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {}


    public void autoSelectorLoop() {
        if (!ChoiceMade) {
            if (gamepad1.circleWasPressed()) {
                choice = Constants.AutoOptions.TOPFULL;

            }
            if (gamepad1.squareWasPressed()) {
                choice = Constants.AutoOptions.TOPTWOONLY;
            }
            telemetry.addData("Current Choice -> ", choice.toString());
            telemetry.addData("Press Circle For Top Full, or ", "square for Top Two");
            telemetry.addData("Press Right Dpad To Select ", "Auto");
            telemetry.update();
            if (gamepad1.dpadRightWasPressed()) {
                ChoiceMade = true;
                switch (choice) {
                    case TOPFULL:
                        buildTopFullPaths();
                        follower.setStartingPose(startPose);
                    case TOPTWOONLY:
                        buildTopTwoPaths();
                        follower.setStartingPose(startPose);
                }
            }
        }
    }

}

