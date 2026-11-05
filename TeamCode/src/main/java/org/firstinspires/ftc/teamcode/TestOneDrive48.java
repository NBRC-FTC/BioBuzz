package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;


import org.firstinspires.ftc.teamcode.pedro.Constants;
@Disabled
@Autonomous(name = "TestOneDrive48", group = "Autonomous")
public class TestOneDrive48 extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose startingPoint = poseFactory.of(0, 0, 90);
    private final Pose endingPoint = poseFactory.of(0, 48, 90);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, startingSpotToEnd()),
                waitMs(5000)
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startingPoint);
        follower.update();
        SparkFunOTOS otos = hardwareMap.get(SparkFunOTOS.class, "SparkFunOTOS");


        telemetry.addData("Offset Heading", otos.getOffset().h);
        telemetry.addData("Offset x", otos.getOffset().x);
        telemetry.addData("Offset y", otos.getOffset().y);
        telemetry.update();
        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            //telemetry.addData("End X", follower.currentPath().endPose().x());
            telemetry.addData("x", follower.pose().x());

            //telemetry.addData("End Y", follower.currentPath().endPose().y());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path startingSpotToEnd() {
        return Paths.line(startingPoint, endingPoint).linear(startingPoint, endingPoint);
    }
}

