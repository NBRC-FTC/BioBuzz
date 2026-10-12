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
        c.linearScalar.set(.922158);
        c.angularScalar.set(0.989);
        c.offset.set(new Pose(-0.0, -0.0,90));
        c.linearUnit.set(DistanceUnit.INCH);
    });
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.15272020149389098);
                Controller secondaryTranslationalForward = Controller.proportional(0.056426033693052394);
                Controller primaryTranslationalLateral = Controller.proportional(0.2190405978711324);
                Controller secondaryTranslationalLateral = Controller.proportional(0.08092964804081441);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.023017861238167518));
                c.brake.set(Controller.proportionalFeedforward(0.01956518205244239));

                c.headingFeedback.set(Controller.proportional(1.8836081984102375));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.10344415360119195, -0.005360221261246551));

                c.linearBrakeCoefficients.set(Matrix.diag(0.11397701051356925, 0.08231837611118271));
                c.quadraticBrakeCoefficients.set(Matrix.diag(6.679538115715404E-4, 0.003505075547914712));

                c.maxAchievableForwardVelocity.set(36.17629209529679);
                c.maxAchievableStrafeVelocity.set(27.660227139207638);
                c.naturalForwardDeceleration.set(11.24094215853306);
                c.naturalStrafeDeceleration.set(20.669744328922256);
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