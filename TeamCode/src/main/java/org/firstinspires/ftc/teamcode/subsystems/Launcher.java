package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Launcher {
    private int velocity = 400;
    private boolean activated = true;
    private final DcMotorEx motor;
     public Launcher(HardwareMap hardwareMap){
         motor = hardwareMap.get(DcMotorEx.class, "launcher_motor");
     }
    public Command off() {
         return instant(() -> motor.setVelocity(0));
       // return instant(() -> motor.setPower(0));
    }

    public Command on() {
        return instant(() -> motor.setVelocity(velocity));
    }
    /*
    public Command on() {
        return instant(() -> motor.setPower(.75));
    }

     */

    private void launcherToogle() {
         activated = !activated;
         if (!activated)
             motor.setVelocity(0);
    }
    public CommandBuilder toggled(){
         return Commands.instant(this::launcherToogle);
    }
    public void periodic() {
         if (activated)
             motor.setVelocity(velocity);
    }
    public void increase(){
         velocity = velocity + 10;
    }
    public void decrease(){
        velocity = velocity - 10;
     }

     public double getVelocity(){
         return motor.getVelocity();
     }
    public double getTargetVelocity(){
        return velocity;
    }
}