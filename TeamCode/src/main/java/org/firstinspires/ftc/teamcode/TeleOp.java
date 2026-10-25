package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.TubeMotor;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp")
public class TeleOp extends OpMode {

    private Follower follower;
    double speed;
    final double ROBOT_FAST = 1;
    final double ROBOT_SLOW = 0.2;
    final double ROBOT_NORMAL = 0.5;

    public Intake intake;
        public TubeMotor tubemotor;
    @Override
    public void init() {

        follower = Constants.create(hardwareMap);
        intake = new Intake(hardwareMap);
        tubemotor = new TubeMotor(hardwareMap);
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

        if (gamepad1.b){
            //intake on
            tubemotor.on().schedule();
        } else {
            //intake off
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
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x
        );

        follower.update();
    }
}