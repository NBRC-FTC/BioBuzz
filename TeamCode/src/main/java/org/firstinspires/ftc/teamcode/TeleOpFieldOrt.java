package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.TubeMotor;
import org.firstinspires.ftc.teamcode.subsystems.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.util.Range;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp -Field Ort")
public class TeleOpFieldOrt extends OpMode {
    final double DESIRED_DISTANCE = 30.0; //  this is how close the camera should get to the target (inches)

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN  =  0.2; //0.02  ;   //  Forward Speed Control "Gain". e.g. Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.1; //0.015 ;   //  Strafe Speed Control "Gain".  e.g. Ramp up to 37% power at a 25 degree Yaw error.   (0.375 / 25.0)
    final double TURN_GAIN   =  0.15; //0.01  ;   //  Turn Control "Gain".  e.g. Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.5;    //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 0.5;    //  Clip the strafing speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN  = 0.3;    //  Clip the turn speed to this max value (adjust for your robot)
    private String alliance = "RED";
    private Follower follower;
    double speed;
    final double ROBOT_FAST = 1;
    final double ROBOT_SLOW = 0.2;
    final double ROBOT_NORMAL = 0.6;

    public Intake intake;
    public TubeMotor tubemotor;
    public Launcher launcher;
    public AprilTagWebcam apriltag;
    private boolean targetFound  = false;    // Set to true when an AprilTag/Cluster target is detected
    private String targetName    = "none";
    //private int    targetID      = 0;
    private double targetRange   = 0;

    private double targetRoll   = 0;
    private double targetBearing = 0;
    private double targetYaw     = 0;
    private int targetClustersFound = 0;
    //DrivePowers powers = 0;

    double  drive           = 0;        // Desired forward power/speed (-1 to +1)
    double  strafe          = 0;        // Desired strafe power/speed (-1 to +1)
    double  turn            = 0;        // Desired turning power/speed (-1 to +1)

    @Override
    public void init() {

        follower = Constants.create(hardwareMap);
        intake = new Intake(hardwareMap);
        tubemotor = new TubeMotor(hardwareMap);
        launcher = new Launcher(hardwareMap);
        apriltag = new AprilTagWebcam(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        targetFound = false;

        apriltag.update();
        //apriltag.telemetryAprilTag();

        if (gamepad1.a){
            //intake on
            intake.on().schedule();
        } else {
            //intake off
            intake.off().schedule();
        }

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
            alliance = "BLUE"; //blue
            gamepad1.setLedColor(0,0,255,-1);
        } else if (gamepad1.shareWasPressed()) {
            alliance = "RED"; //red
            gamepad1.setLedColor(255,0,0,-1);
        }

        if (gamepad1.right_bumper) {
            speed = ROBOT_FAST;
        }else if (gamepad1.left_bumper) {
            speed = ROBOT_SLOW;
        } else {
            speed = ROBOT_NORMAL;
        }

        AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) apriltag.getGoodTag(alliance);

        if (clusterDet != null)  {
            // Yes, we want to use this tag.
            targetName    = clusterDet.metadata.shortName;
            targetClustersFound = clusterDet.percentClusterFound;
            targetRoll      = clusterDet.ftcPose.roll;
            targetRange   = clusterDet.ftcPose.range;
            targetBearing = clusterDet.ftcPose.bearing;
            targetYaw     = clusterDet.ftcPose.yaw;
            targetFound = true;
            //break;  // don't look any further.
        }

        // Tell the driver what we see, and what to do.
        if (targetFound) {
            telemetry.addData("\n>","HOLD Left-Bumper to Drive to Target\n");
            telemetry.addData("Found", "%s", targetName);
            telemetry.addLine(String.format("Percent tags found: %d", targetClustersFound));
            telemetry.addData("Roll",  "%5.1f degrees", targetRoll);
            telemetry.addData("Range",  "%5.1f inches", targetRange);
            telemetry.addData("Bearing","%3.0f degrees", targetBearing);
            telemetry.addData("Yaw","%3.0f degrees", targetYaw);

            gamepad1.rumble(0, .2, 100);
        } else {
            telemetry.addData("\n>","Drive using joysticks to find valid target\n");
        }

        // If Left Bumper is being pressed, AND we have found the desired target, Drive to target Automatically .
        if (gamepad1.left_bumper && targetFound) {

            // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
            double  rangeError      = targetRange - DESIRED_DISTANCE;
            double  headingError    = targetBearing;
            double  yawError        = targetYaw;

            // Use the speed and turn "gains" to calculate how we want the robot to move.
            drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
            turn   = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
            strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);
/*
            DrivePowers powers = ManualDrive.fieldCentric(
                    drive,
                    strafe,
                    turn,
                    follower.pose().heading()
            );

            follower.manual(powers);
*/
            telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
        } else {
            // drive using manual POV Joystick mode.  Slow things down to make the robot more controlable.

            drive = -gamepad1.left_stick_y  * speed;
            turn   = -gamepad1.right_stick_x  * speed;
            strafe = -gamepad1.left_stick_x  * speed;
/*
            DrivePowers powers = ManualDrive.fieldCentric(
                    -gamepad1.left_stick_y  * speed,
                    -gamepad1.left_stick_x  * speed,
                    -gamepad1.right_stick_x  * speed,
                    follower.pose().heading()
            );

            follower.manual(powers);
 */
            //drive  = -gamepad1.left_stick_y  / 2.0;  // Reduce drive rate to 50%.
          //  strafe = -gamepad1.left_stick_x  / 2.0;  // Reduce strafe rate to 50%.
//turn   = -gamepad1.right_stick_x / 3.0;  // Reduce turn rate to 33%.
            // telemetry.addData("Manual","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
        }

        DrivePowers powers = ManualDrive.fieldCentric(
                drive,
                strafe,
                turn,
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
        telemetry.addData("Alliance", alliance);
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("launcher actual velocity", launcher.getVelocity());
        telemetry.addData("launcher target velocity", launcher.getTargetVelocity());
    }
}