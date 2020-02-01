package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.Direction;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;

public class Wheel {
    private DcMotor motor;

    Wheel(String id, HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, id);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setDirection(DcMotor.Direction.FORWARD);
    }

    public ChangeDirection(Direction direction) {
        motor.setDirection(direction);
    }

    public ChangeRunMode(RunMode runMode) {
        motor.setMode(runMode);
    }

}