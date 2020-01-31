/*
    # # # # # # # # # # # # # # # # # # 
    # Masters School Robotics         #
    # Written by Matthew Nappo,       #
    #            Zach Battleman       #
    # GitHub: @xoreo, @Zanolon        #
    #                                 #
    # Class Autonomous                #
    # # # # # # # # # # # # # # # # # # 
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class Autonomous extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor w0 = null;
    private DcMotor w1 = null;
    private DcMotor w2 = null;
    private DcMotor w3 = null;

    private CRServo lift0 = null;
    private CRServo lift1 = null;

    private AnalogInput poten = null;
    private AnalogInput typePoten = null;

    private double max_speed;

    void allreverse() {
        w0.setDirection(DcMotor.Direction.REVERSE);
        w1.setDirection(DcMotor.Direction.REVERSE);
        w2.setDirection(DcMotor.Direction.REVERSE);
        w3.setDirection(DcMotor.Direction.REVERSE);
    }

    void regulardirection() {
        if (poten.getVoltage() <= 1) {
            w0.setDirection(DcMotor.Direction.FORWARD);
            w1.setDirection(DcMotor.Direction.FORWARD);
            w2.setDirection(DcMotor.Direction.REVERSE);
            w3.setDirection(DcMotor.Direction.REVERSE);
        } else {
            w0.setDirection(DcMotor.Direction.REVERSE);
            w1.setDirection(DcMotor.Direction.REVERSE);
            w2.setDirection(DcMotor.Direction.FORWARD);
            w3.setDirection(DcMotor.Direction.FORWARD);
        }
    }

    private DcMotor init_motor(String id) {
        DcMotor m = null;
        m = hardwareMap.get(DcMotor.class, id);
        m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m.setDirection(DcMotor.Direction.REVERSE);
        return m;
    }

    private DcMotor init_motorFORWARD(String id) {
        DcMotor m = null;
        m = hardwareMap.get(DcMotor.class, id);
        m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m.setDirection(DcMotor.Direction.FORWARD);
        return m;
    }

    private Servo init_servo(String id) {
        Servo s = null;
        s = hardwareMap.get(Servo.class, id);
        s.setDirection(Servo.Direction.FORWARD);
        return s;
    }

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


    void my_init() {
        poten = hardwareMap.get(AnalogInput.class, "driverpoten");
        typePoten = hardwareMap.get(AnalogInput.class, "typepoten");

        max_speed = 0.3;
        // max_speed = 0.125;
        if (poten.getVoltage() <= 1) {
            w0 = init_motorFORWARD("w0");
            w1 = init_motorFORWARD("w1");
            w2 = init_motor("w2");
            w3 = init_motor("w3");
        } else {
            w0 = init_motor("w0");
            w1 = init_motor("w1");
            w2 = init_motorFORWARD("w2");
            w3 = init_motorFORWARD("w3");
        }

        lift0 = init_CRservo("lift0", true);
        lift1 = init_CRservo("lift1", true);

        telemetry.addData("Status", "Initialized");

        // reset encoders
        w1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        w3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        w1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        w3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void drive_sideways() {
        try {
            long t = System.currentTimeMillis();
            long end = t + 750;
            // long end = t+1000;
            while (System.currentTimeMillis() < end) {
                w1.setPower(1);
                w3.setPower(1);
                // pause to avoid churning
                Thread.sleep(2);
            }

        } catch (Exception e) {

        }
        w1.setPower(0);
        w3.setPower(0);

    }

    void drive_othersideways() {
        try {
            long t = System.currentTimeMillis();
            long end = t + 1700;
            while (System.currentTimeMillis() < end) {
                w1.setPower(-1);
                w3.setPower(-1);
                // pause to avoid churning
                Thread.sleep(2);
            }

        } catch (Exception e) {

        }
        w1.setPower(0);
        w3.setPower(0);

    }


    void drive_forward() {

        w0.setPower(1);
        w2.setPower(1);

    }

    void drive_backward() {
        w0.setPower(-1);
        w2.setPower(-1);

    }


    void turn() {
        allreverse();
        w0.setPower(0.15);
        w1.setPower(0.15);
        w2.setPower(0.15);
        w3.setPower(0.15);
        regulardirection();
    }

    void otherturn() {
        allreverse();
        w0.setPower(-0.15);
        w1.setPower(-0.15);
        w2.setPower(-0.15);
        w3.setPower(-0.15);
        regulardirection();
    }

    void stopthemnow() {
        w0.setPower(0);
        w1.setPower(0);
        w2.setPower(0);
        w3.setPower(0);
    }

    void lift_down() {
        double power = 0.85;
        lift0.setPower(power);
        lift1.setPower(power);
        sleep(1000);
        lift0.setPower(0);
        lift1.setPower(0);
    }

    void lift_up() {
        double power = 0.85;
        lift0.setPower(-power);
        lift1.setPower(-power);
        sleep(2000);
        lift0.setPower(0);
        lift1.setPower(0);
    }

    public static void my_sleep(long sleepTime) {
        long wakeupTime = System.currentTimeMillis() + sleepTime;

        while (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {}
            sleepTime = wakeupTime - System.currentTimeMillis();
        }
    }

    @Override
    public void runOpMode() {
        my_init();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        if (opModeIsActive()) {

            drive_sideways();
            sleep(3000);
            long turnms = 600;
            if (typePoten.getVoltage() <= 1) {

                drive_forward();
                sleep(1000);
                stopthemnow();

                otherturn();
                sleep(turnms);
                stopthemnow();

                lift_up();
                
                turn();
                sleep(turnms);
                stopthemnow();

                drive_backward();
                sleep(1000);
                stopthemnow();

                lift_down(); // actuapp up
                sleep(1000);
                stopthemnow();

                lift_up(); // actuapp down
                drive_othersideways();
                sleep(3000);
                stopthemnow();

            }
        }
    }
}