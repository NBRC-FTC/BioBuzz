package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Motor Test")
public class Intake extends LinearOpMode {

    private DcMotor testMotor;

    @Override
    public void runOpMode() {
        // Must match the exact name given in the Robot Controller configuration file
        testMotor = hardwareMap.get(DcMotor.class, "intake_motor");

        // Optional: Set zero power behavior (BRAKE stops quickly, FLOAT coasts)
        testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Optional: Run without encoder for raw power testing
        testMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized. Press Start.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Run the motor at 30% power (safe test speed)

            if (gamepad1.triangle) {
                testMotor.setPower(0.5);
            } else {
                testMotor.setPower(0.0);
            }

            telemetry.addData("Motor Power", testMotor.getPower());
            telemetry.update();
        }
    }
}
