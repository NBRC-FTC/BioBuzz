package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.TubeMotor;

@Autonomous(name = "AutoPlay3", group = "Autonomous")
public class AutoPlay3 extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose startingPoint = poseFactory.of(58, 9, 90);
    //private final Pose startingSpot3ToShoot = poseFactory.of(57.9542, 24.1461, 90);
    //private final Pose launchingToLoadingZoneStart = poseFactory.of(57.9542, 24.1461, 90);
    private final Pose loadingZoneB = poseFactory.of(12, 97, 0);
    private final Pose launchingToLoadingZoneControl1 = poseFactory.of(20.456, 23.0935, 0);
    private Launcher launcher;
    private TubeMotor tubeMotor;

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                launcher.on(),
                waitMs(1000),
                tubeMotor.on(),
                waitMs(3000),
                launcher.off(),
                tubeMotor.off(),
                follow(follower, startingSpotToLoadingZoneB())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startingPoint);
        follower.update();
        launcher = new Launcher(hardwareMap);
        tubeMotor = new TubeMotor(hardwareMap);
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

  //  public Path startingSpotToLoadingZoneB() {
       // return Paths.line(startingPoint, startingSpot3ToShoot).linear(startingPoint, startingSpot3ToShoot);
  //  }
    public Path startingSpotToLoadingZoneB() {
        return Paths.curve(startingPoint, launchingToLoadingZoneControl1, loadingZoneB).linear(startingPoint, loadingZoneB);
    }
}

