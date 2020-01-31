/*
    # # # # # # # # # # # # # # # # # # 
    # Masters School Robotics         #
    # Written by Matthew Nappo,       #
    #            Zach Battleman       #
    # GitHub: @xoreo, @Zanolon        #
    #                                 #
    # Class OctoBot                   #
    # # # # # # # # # # # # # # # # # # 
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.*;

@TeleOp(name="Comp: OctoBot", group="Comp")
public class OctoBot extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // The 4 wheels
    private DcMotor w0;
    private DcMotor w1;
    private DcMotor w2;
    private DcMotor w3;
    
    // The two VEX393 motors for the lift
    private CRServo lift0 = null;
    private CRServo lift1 = null;
    
    // The two servo motors for the front shovel
    private Servo shovel0 = null;
    private Servo shovel1 = null;
    
    private ColorSensor colorSensor = null;
    
    // The potentiometer for determining driver controls
    private AnalogInput driverpoten = null;
    
    // Various constants for motors and such
    private double MAX_SPEED;
    private double TURN_BUFFER;
    private double SERVO_BUFFER;
    private double STOP_VAL;

    // Initialize code for different drivers
    private enum Driver {
        ELLA,
        MATT
    };

    private Driver currentDriver;

    // init_motor initializes a motor given its hardware address.
    private DcMotor init_motor(String id) {
        DcMotor m = null;
        m = hardwareMap.get(DcMotor.class, id);
        m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m.setDirection(DcMotor.Direction.REVERSE);
        return m;
    }

    // init_servo initializes a servo given its hardware address.
    private Servo init_servo(String id) {
        Servo s = null;
        s = hardwareMap.get(Servo.class, id);
        s.setDirection(Servo.Direction.FORWARD);
        return s;
    }
    
    // init_CRServo initializes a continuous rotation servo given its
    // hardware address.
    private CRServo init_CRservo(String id, boolean forward) {
        CRServo s = null;
        s = hardwareMap.get(CRServo.class, id);
        if (forward) {
            s.setDirection(CRServo.Direction.FORWARD);
        } else {
            s.setDirection(CRServo.Direction.REVERSE);
        }
        return s;
    }
    
    // init_CRServo initializes a color sensor given its hardware address.
    private ColorSensor init_colorSensor(String id) {
        ColorSensor s = null;
        s = hardwareMap.get(ColorSensor.class, id);
        return s;
    }

    @Override
    public void init() {
        MAX_SPEED = 0.85;
        TURN_BUFFER = 0.38;
        SERVO_BUFFER = 1;
        // SERVO_BUFFER = 0.47;
        
        STOP_VAL = 0;

        // Initialize the wheels
        w0 = init_motor("w0");
        w1 = init_motor("w1");
        w2 = init_motor("w2");
        w3 = init_motor("w3");
        
        // Initialize the lift motors
        lift0 = init_CRservo("lift0", true);
        lift1 = init_CRservo("lift1", true);
        
        // Initialize the shovel motors
        shovel0 = init_servo("shovel0");
        shovel1 = init_servo("shovel1");
        
        // Initialize the color sensors
        colorSensor = init_colorSensor("color_sensor");

        // Initialize other sensors
        driverpoten = hardwareMap.get(AnalogInput.class, "driverpoten");
    }

    double[] move() {
        double x_left_joy;
        double y_left_joy;
        // if (currentDriver == Driver.MATT) {
        //     x_left_joy = -gamepad1.left_stick_x;
        //     y_left_joy = gamepad1.left_stick_y;
        // } else {
        // }

        x_left_joy = -gamepad1.right_stick_x;
        y_left_joy = gamepad1.right_stick_y;
        
        double phi_joy = Math.atan2(y_left_joy, x_left_joy);
        
        double x_left_joy_sq = Math.pow(x_left_joy, 2);
        double y_left_joy_sq = Math.pow(y_left_joy, 2);
        
        double r_joy = Math.sqrt(x_left_joy_sq + y_left_joy_sq);
        
        double speed = MAX_SPEED * r_joy;
        
        double alpha_1 = Math.PI / 4;
        double alpha_2 = 3 * Math.PI / 4;
        double alpha_3 = 5 * Math.PI / 4;
        double alpha_4 = 7 * Math.PI / 4;
        
        double theta_1 = alpha_1 - phi_joy;
        double theta_2 = alpha_2 - phi_joy;
        double theta_3 = alpha_3 - phi_joy;
        double theta_4 = alpha_4 - phi_joy;
        
        double w0_power = -speed * Math.sin(theta_1);
        double w1_power = -speed * Math.sin(theta_2);
        double w2_power = -speed * Math.sin(theta_3);
        double w3_power = -speed * Math.sin(theta_4);
        
        telemetry.addData(" -- POWERS IN CALCULATION --", "");
        telemetry.addData("w0_power", w0_power);
        telemetry.addData("w1_power", w1_power);
        telemetry.addData("w2_power", w2_power);
        telemetry.addData("w3_power", w3_power);
        telemetry.addData(" -- END --", "");
        
        double[] speeds = {
            w0_power,
            w1_power,
            w2_power,
            w3_power
        };

        return speeds;
    }

    // lift powers the 4 bar lift.
    double lift() {
        // Get the input from the gamepad
        boolean right_bumper = gamepad1.left_bumper;
        boolean left_bumper  = gamepad1.right_bumper;

        // Power the motors accordingly
        double power = 0.85;
        if (right_bumper) {
            lift0.setPower(power); // down I think
            lift1.setPower(power);
        }
        else if (left_bumper) { // up i think
            lift0.setPower(-power);
            lift1.setPower(-power);
        }
        else if (!left_bumper && !right_bumper) {
            lift0.setPower(0);
            lift1.setPower(0);
        }
        
        return 0;
    }
    
    // shovel powers thes shovel.
    void shovel() {
        double rt = gamepad1.right_trigger;
        double lt = gamepad1.left_trigger;
        
        if (rt > 0) { // If the right trigger is being pressed
            shovel0.setPosition(-.5);
            shovel1.setPosition(-.5);
        } else if (lt > 0) {
            shovel0.setPosition(0.5);
            shovel1.setPosition(0.5);
        } else if (lt == 0 && rt == 0) {
            shovel0.setPosition(0);
            shovel1.setPosition(0);
        }
        
        telemetry.addData("right trigger", rt);
        telemetry.addData("left trigger", lt);
    }
    
    // turn calculates the turning speed.
    double turn() {
        double x_right_joy;
        // if (currentDriver == Driver.MATT) {
        //     x_right_joy = gamepad1.right_stick_x;
        // } else {
        // }
        x_right_joy = gamepad1.left_stick_x;
        telemetry.addData("INNER TURN", x_right_joy);
        
        double speed = Range.clip(x_right_joy, -1.0, 1.0) * TURN_BUFFER * MAX_SPEED;

        return speed;
    }
    
    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        // Set the current driver
        currentDriver = (driverpoten.getVoltage() <= 1) ? Driver.MATT : Driver.ELLA;
        
        // Calculate moving and turning speeds
        double[] move = move();
        double turn = turn();

        double w0_vel = move[0] + turn;
        double w1_vel = move[1] + turn;
        double w2_vel = move[2] + turn;
        double w3_vel = move[3] + turn;
        
        // Power the motors with the appropriate speeds
        w0.setPower(w0_vel);
        w1.setPower(w1_vel);
        w2.setPower(w2_vel);
        w3.setPower(w3_vel);
        
        // Some debugging info
        telemetry.addData("driverpoten", driverpoten.getVoltage());
        telemetry.addData("turn", turn);

        // Power the shovel and the lift
        lift();
        shovel();
    }

    @Override
    public void stop() {
        // Forcefully stop all of the motors
        w0 = null;
        w1 = null;
        w2 = null;
        w3 = null;

        lift0 = null;
        lift1 = null;
        shovel0 = null;
        shovel1 = null;
    }
}