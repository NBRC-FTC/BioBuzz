package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.TubeMotor;
import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp -Field Ort")
public class TeleOpFieldOrt extends OpMode {

    private String alliance = "red";
    private Follower follower;
    double speed;
    final double ROBOT_FAST = 1;
    final double ROBOT_SLOW = 0.2;
    final double ROBOT_NORMAL = 0.6;

    public Intake intake;
    public TubeMotor tubemotor;
    public Launcher launcher;
    public AprilTagWebcam apriltag;

    @Override
    public void init() {

        follower = Constants.create(hardwareMap);
        intake = new Intake(hardwareMap);
        tubemotor = new TubeMotor(hardwareMap);
        launcher = new Launcher(hardwareMap);
        apriltag.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        apriltag.update();
        apriltag.telemetryAprilTag();

        if (gamepad1.a){
            //intake on
            intake.on().schedule();
        } else {
            //intake off
            intake.off().schedule();
        }
        /*
        if (gamepad1.dpad_left){
            //shooter on
            launcher.on().schedule();
        } else {
            //shooter off
            launcher.off().schedule();
        }

         */

        if (gamepad1.dpadRightWasPressed()){
            launcher.toggled().schedule();
        }

        if (gamepad1.dpadUpWasPressed()){
            launcher.increase();
        }

        if (gamepad1.dpadDownWasPressed()){
            launcher.decrease();
        }

        if (gamepad1.b){
            //tubemotor on
            tubemotor.on().schedule();
        } else {
            //tubemotor off
            tubemotor.off().schedule();
        }
        if (gamepad1.optionsWasPressed()){
            alliance = "blue"; //blue
            gamepad1.setLedColor(0,0,255,-1);
        } else if (gamepad1.shareWasPressed()) {
            alliance = "red"; //red
            gamepad1.setLedColor(255,0,0,-1);
        }

        if (gamepad1.right_bumper) {
            speed = ROBOT_FAST;
        }else if (gamepad1.left_bumper) {
            speed = ROBOT_SLOW;
        } else {
            speed = ROBOT_NORMAL;
        }
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y  * speed,
                -gamepad1.left_stick_x  * speed,
                -gamepad1.right_stick_x  * speed,
                follower.pose().heading()
        );
        follower.manual(powers);

        // relocalise button
        if (gamepad1.startWasPressed()){
            Pose cornerPose = new Pose(10.5, 10.5,Math.toRadians(0));
            follower.setPose(cornerPose);//overides our pose
        }
        follower.update();
        launcher.periodic();

        Pose robotPose = follower.pose();// returns a pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("launcher actual velocity", launcher.getVelocity());
        telemetry.addData("launcher target velocity", launcher.getTargetVelocity());
    }
/*
    private void AutomaticMovement() {
        // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
        double  rangeError      = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
        double  headingError    = desiredTag.ftcPose.bearing;
        double  yawError        = desiredTag.ftcPose.yaw;

        // Use the speed and turn "gains" to calculate how we want the robot to move.
        drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
        turn   = Range.clip(-headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
        //strafe = Range.clip(yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);
        strafe = gamepad1.left_stick_x  * power;  // Reduce strafe rate to 50%.

        // Apply desired axes motions to the drivetrain.
        mecanum.moveRobot(drive, strafe, turn);

        telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
    }
    */
}