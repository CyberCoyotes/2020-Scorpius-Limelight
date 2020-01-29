/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
    CANSparkMax left1 = new CANSparkMax(1, MotorType.kBrushless);
    CANSparkMax left2 = new CANSparkMax(2, MotorType.kBrushless);
    CANSparkMax right1 = new CANSparkMax(3, MotorType.kBrushless);
    CANSparkMax right2 = new CANSparkMax(4, MotorType.kBrushless);

    SpeedControllerGroup left = new SpeedControllerGroup(left1, left2);
    SpeedControllerGroup right = new SpeedControllerGroup(right1, right2);

    DifferentialDrive mainDrive = new DifferentialDrive(left, right);

    Limelight vision = new Limelight();

    Joystick driver = new Joystick(0);

    @Override
    public void robotInit() {

    }

    @Override
    public void teleopPeriodic() {


        /** If the measured width is between, the target is in optimal range, outside will make the robot adjust its distance from the target */
        double minWidth = 4; //**** Increase this number to make the robot stay closer, descrease to make the robot stay farther */
        double maxWidth = 20; //**** Increase this number to make the robot stay closer, descrease to make the robot stay farther */

        double x = driver.getRawAxis(1)/2;
        double y = driver.getRawAxis(2)/2;
        if(!driver.getRawButton(1) && (Math.abs(x) >= 0.05 || Math.abs(y) >= 0.05)) {
            mainDrive.arcadeDrive(x, -y);
        } else if(driver.getRawButton(1) && vision.hasValidTarget()) {
            double distance = vision.getWidth();
            if(distance > maxWidth) { // 
              y=0.25;
            } else if(distance < minWidth) {
              y=-0.25;
            } else {
                y = 0.0;
            }
            x = vision.getX()/10.0;

            mainDrive.arcadeDrive(x/2, y/2);
        } else {
            mainDrive.arcadeDrive(0, 0);
        }

        SmartDashboard.putBoolean("Sees Target?", vision.hasValidTarget());
        SmartDashboard.putNumber("X-angle", vision.getX());
        SmartDashboard.putNumber("Width in pixels", vision.getWidth());
    }
}