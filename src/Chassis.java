package org.firstinspires.ftc.teamcode;

public class Chassis {
    private HardwareMap hardwareMap;
    private Wheel[] wheels;
    
    Chassis(String ids[], HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        wheels = new Wheel[ids.length];
        for (int i = 0; i < ids.length; i++) {
            Wheel newWheel = new Wheel(id, this.hardwareMap);
            wheels[i] = newWheel;
        }
    }

    private double[] CalculateMovement() {
        
    }
    
    private double[] CalculateTurn() {

    }

    public void Drive() {
        double[] moveSpeeds = CalculateMovement();
        double[] turnSpeeds = CalculateTurn();

        switch (wheels.length) {
            case 4:
                for (int i = 0; i < wheels.length; i++) {

                }
                break;
        }
    }
}