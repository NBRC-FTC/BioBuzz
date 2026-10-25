package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;

public class Intake {
    private final DcMotorEx motor;
     public Intake(HardwareMap hardwareMap){
         motor = hardwareMap.get(DcMotorEx.class, "intake_motor");
     }
    public Command off() {
         return instant(() -> motor.setPower(0));
    }
    public Command on() {
        return instant(() -> motor.setPower(0.75));
    }
}
