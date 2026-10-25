package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.TubeMotor;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp -Robot Ort")
public class TeleOpRobotOrt extends OpMode {

    private Follower follower;
    double speed;
    final double ROBOT_FAST = 1;
    final double ROBOT_SLOW = 0.2;
    final double ROBOT_NORMAL = 0.6;

    public Intake intake;
    public TubeMotor tubemotor;
    public Launcher launcher;

    @Override
    public void init() {

        follower = Constants.create(hardwareMap);
        intake = new Intake(hardwareMap);
        tubemotor = new TubeMotor(hardwareMap);
        launcher = new Launcher(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.a){
            //intake on
            intake.on().schedule();
        } else {
            //intake off
            intake.off().schedule();
        }
        if (gamepad1.dpad_left){
            //shooter on
            launcher.on().schedule();
        } else {
            //shooter off
            launcher.off().schedule();
        }
        if (gamepad1.b){
            //tubemotor on
            tubemotor.on().schedule();
        } else {
            //tubemotor off
            tubemotor.off().schedule();
        }
        if (gamepad1.right_bumper) {
            speed = ROBOT_FAST;
        }else if (gamepad1.left_bumper) {
            speed = ROBOT_SLOW;
        } else {
            speed = ROBOT_NORMAL;
        }
        follower.manual(
                -gamepad1.left_stick_y * speed,
                -gamepad1.left_stick_x * speed,
                -gamepad1.right_stick_x * speed
        );

        follower.update();
        Pose robotPose = follower.pose();// returns a pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }
}