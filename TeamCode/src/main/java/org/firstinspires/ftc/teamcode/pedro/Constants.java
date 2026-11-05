package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.OTOSConfig;
import com.pedropathing.revhub.localizers.OTOSLocalizer;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("front_left");
        c.frontRightName.set("front_right");
        c.backLeftName.set("back_left");
        c.backRightName.set("back_right");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });
    public static OTOSConfig localizerConfig = new OTOSConfig(c -> {
        c.name.set("SparkFunOTOS");
        //c.linearScalar.set(.922158);
        //c.angularScalar.set(0.989);
        c.linearScalar.set(1.2);
        c.angularScalar.set(1.0);
        c.offset.set(new Pose(-4.2, 0.6,Math.toRadians(90)));
        c.linearUnit.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.21643388107930342);
                Controller secondaryTranslationalForward = Controller.proportional(0.07996653583898908);
                Controller primaryTranslationalLateral = Controller.proportional(0.2090541048103808);
                Controller secondaryTranslationalLateral = Controller.proportional(0.07723990569887582);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.018326220669577807));
                c.brake.set(Controller.proportionalFeedforward(0.015577287569141136));

                c.headingFeedback.set(Controller.proportional(2.4275543908688917));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06932485374056542, 0.0024117046414213516));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06566874385454861, 0.06804011988128572));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0017619447431695967, 0.0010088082449558014));

                c.maxAchievableForwardVelocity.set(59.09556144430985);
                c.maxAchievableStrafeVelocity.set(46.301969968481856);
                c.naturalForwardDeceleration.set(29.88484541299498);
                c.naturalStrafeDeceleration.set(72.7278701506202);
            }
    );

    public static Follower create(HardwareMap h) {

        return new Follower(
                new OTOSLocalizer(h, Constants.localizerConfig),
                new Mecanum(h, Constants.drivetrainConfig),
                new Foresight(Constants.foresightConfig)
        );
    }
}