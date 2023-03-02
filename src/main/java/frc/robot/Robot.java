// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
//Using VictorSP Motor Controllers

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj. PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.hal.DigitalGlitchFilterJNI;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
// NOT IN BUFORD import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.drive.DifferentialDrive;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends TimedRobot {
  private final Timer m_Timer = new Timer();
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final PWMVictorSPX leftDrive = new PWMVictorSPX(1);
  private final PWMVictorSPX rightDrive = new PWMVictorSPX(0);
  private final PWMVictorSPX gearDrive = new PWMVictorSPX(2);
  private final PWMVictorSPX extenderDrive = new PWMVictorSPX(3);
  private final DifferentialDrive myDrive = new DifferentialDrive(leftDrive, rightDrive);
  private Joystick myJoystick = new Joystick(0);
  DigitalInput toplimitSwitch = new DigitalInput(0); 
  DigitalInput bottomlimitSwitch = new DigitalInput(1);
  private Joystick yourJoystick = new Joystick(1);
  private final DoubleSolenoid doublesolenoid1 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,0,7 );
  /** // private final XboxController m_controller = new XboxController(0);
 //private final Timer m_timer = new Timer();
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
 m_chooser.setDefaultOption("DefaultAuto", kDefaultAuto);
 m_chooser.addOption("My Auto", kCustomAuto);
 SmartDashboard.putData("Auto choices", m_chooser);
 CameraServer .startAutomaticCapture();
  }
  
  
  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("AutoSelected:"+ m_autoSelected);
    m_Timer.reset();
    m_Timer.start();
  }

 
  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic(){
    switch (m_autoSelected) {
      case kDefaultAuto:
     
    //  if(m_Timer.get()> .5 & m_Timer.get()< 6)
    //  {leftDrive.stopMotor(); rightDrive.stopMotor();}
if(m_Timer.get()>0 & m_Timer.get()<.7)
{doublesolenoid1.set(DoubleSolenoid.Value.kReverse);}

     if(m_Timer.get()>1 & m_Timer.get()<4) 
    {extenderDrive.set(1); gearDrive.set(-.6);}
    if(m_Timer.get()>2.5 )
    {gearDrive.stopMotor();}
    if(m_Timer.get()>4)
    {extenderDrive.stopMotor();}

    if(m_Timer.get()>6.5 & m_Timer.get()<7)
    {doublesolenoid1.set(DoubleSolenoid.Value.kForward);}

    if(m_Timer.get()>7 & m_Timer.get()<9)
    {extenderDrive.set(-1);}

    if(m_Timer.get()>9 & m_Timer.get()<15)
    {extenderDrive.stopMotor();}

      if(m_Timer.get()>9 & m_Timer.get()<12)
      {leftDrive.set(-.9);}


    if(m_Timer.get()>9 & m_Timer.get()<12)
    {rightDrive.set(-.9);}
      
    if(m_Timer.get()>12)
      {leftDrive.stopMotor();}
    if(m_Timer.get()>12)
    {leftDrive.stopMotor();}

  
        break;
         case kCustomAuto:
      default:
        }
      }
          
   // if (m_Timer.get() < 3)
    //{extenderDrive.set(1); gearDrive.set(.7);}
   // else{extenderDrive.stopMotor(); gearDrive.stopMotor();}
   
   // if (m_Timer.get() > 3.5) {doublesolenoid1.set(DoubleSolenoid.Value.kForward);
   // }
 
   // if (m_Timer.get()> 6) { 
   // leftDrive.set(.8);
   // rightDrive.set(.8); }

 // if (m_Timer.get()> 10) {
    //leftDrive.set(0);
   // rightDrive.set(0); }

     //   break;}}


  
  

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
    myDrive.arcadeDrive(myJoystick.getRawAxis(0), myJoystick.getRawAxis(1));


    //Pivot Arm
    if(yourJoystick.getRawButton(5)) //this is l trigger
        //Upper Limit Switch
        if (toplimitSwitch.get()) {
          gearDrive.set(0);
      }
      else {
        gearDrive.set(.8);
      }
    
    else if (yourJoystick.getRawButton(6))//this is r trigger
    //Lower Limit Switch
        if (bottomlimitSwitch.get()) {
          gearDrive.set(0);
      }
      else {
        gearDrive.set(-.8);
      }

    //Extender Arm
if(yourJoystick.getRawButton(1)) //this is left trigger
    {extenderDrive.set(.8);}
    else if (yourJoystick.getRawButton(2))//this is right trigger
    {extenderDrive.set(-.8);}
    else {extenderDrive.set(0);}
    //could you please only extend for 3 seconds and not past 4 ft
    
    //Grabber
    if(myJoystick.getRawButton(2)) //A
    {doublesolenoid1.set(DoubleSolenoid.Value.kForward);
    }
    else if(myJoystick.getRawButton(1)) //B
    {doublesolenoid1.set(DoubleSolenoid.Value.kReverse);}
    }

//     //Upper Limit Switch
//     public void setGearDrive(double speed) {
//     if(speed > 0) {
//       if (toplimitSwitch.get()) {
//         gearDrive.set(0);
//       }
//       else {
//         gearDrive.set(.8);
//       }
//     }
//     else {
//       if (bottomlimitSwitch.get()) {
//         gearDrive.set(0);
//       }
//       else {
//         gearDrive.set(-.8);
//       }
//     }
  }

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
