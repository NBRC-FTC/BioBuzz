package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TubeMotor {
    private final DcMotorEx motor;
     public TubeMotor(HardwareMap hardwareMap){
         motor = hardwareMap.get(DcMotorEx.class, "tube_motor");
     }
    public Command off() {
         return instant(() -> motor.setPower(0));
    }
    public Command on() {
        return instant(() -> motor.setPower(1));
    }
}
