// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
//Using VictorSPX Motor Controllers

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj. PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.CANCoderFaults;
import com.ctre.phoenix.sensors.CANCoderStickyFaults;
import com.ctre.phoenix.sensors.MagnetFieldStrength;
import com.ctre.phoenix.sensors.WPI_CANCoder;
//import javax.print.attribute.standard.Sides;

import edu.wpi.first.cameraserver.CameraServer;
//import edu.wpi.first.hal.DigitalGlitchFilterJNI;
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
  private static final String kDefaultAuto = "Sides";
  private static final String kCustomAuto = "Middle";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final PWMVictorSPX leftDrive = new PWMVictorSPX(1);
  private final PWMVictorSPX rightDrive = new PWMVictorSPX(0);
  private final PWMVictorSPX gearDrive = new PWMVictorSPX(2);
  private final PWMVictorSPX extenderDrive = new PWMVictorSPX(3);
  private final DifferentialDrive myDrive = new DifferentialDrive(leftDrive, rightDrive);
  private Joystick myJoystick = new Joystick(0);
  DigitalInput toplimitSwitch = new DigitalInput (0); 
  DigitalInput bottomlimitSwitch = new DigitalInput(1);
  private Joystick yourJoystick = new Joystick(1);
  private final DoubleSolenoid doublesolenoid1 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,0,7 );
  /** // private final XboxController m_controller = new XboxController(0);
 //private final Timer m_timer = new Timer();
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

 //CTRE CANcoder sample code from:
  //github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/master/Java%20General/CANCoder/src/main/java/frc/robot/Robot.java
  final int PRINTOUT_DELAY = 100; // in Milliseconds
  WPI_CANCoder _CANCoder1 = new WPI_CANCoder(1, "rio");
  WPI_CANCoder _CANCoder2 = new WPI_CANCoder(2, "rio");
  CANCoderConfiguration _canCoderConfiguration = new CANCoderConfiguration();
  private ErrorCode setPosition;

    /**  
   * Doing lots of printing in Java creates a large overhead 
   * This Instrument class is designed to put that printing in a seperate thread
   * That way we can prevent loop overrun messages from occurring
   */
  class Instrument extends Thread {
    void printFaults(CANCoderFaults faults) {
      System.out.printf("Hardware fault: %s\t    Under Voltage fault: %s\t    Reset During Enable fault: %s\t    API Error fault: %s%n", 
        faults.HardwareFault ? "True " : "False",
        faults.UnderVoltage ? "True " : "False",
        faults.ResetDuringEn ? "True " : "False",
        faults.APIError ? "True " : "False");
    }
    void printFaults(CANCoderStickyFaults faults) {
      System.out.printf("Hardware fault: %s\t    Under Voltage fault: %s\t    Reset During Enable fault: %s\t     API Error fault: %s%n", 
        faults.HardwareFault ? "True " : "False",
        faults.UnderVoltage ? "True " : "False",
        faults.ResetDuringEn ? "True " : "False",
        faults.APIError ? "True " : "False");
    }
    void printValue(double val, String units, double timestamp) {
      System.out.printf("%20f %-20s @ %f%n", val, units, timestamp);
    }
    void printValue(MagnetFieldStrength val, String units, double timestamp) {
      System.out.printf("%20s %-20s @ %f%n", val.toString(), units, timestamp);
    }

    public void run() {
      /* Report position, absolute position, velocity, battery voltage gearDrive ENCODER*/
      double posValue = _CANCoder1.getPosition();
      String posUnits = _CANCoder1.getLastUnitString();
      double posTstmp = _CANCoder1.getLastTimestamp();
      
      double absValue = _CANCoder1.getAbsolutePosition();
      String absUnits = _CANCoder1.getLastUnitString();
      double absTstmp = _CANCoder1.getLastTimestamp();
      
      double velValue = _CANCoder1.getVelocity();
      String velUnits = _CANCoder1.getLastUnitString();
      double velTstmp = _CANCoder1.getLastTimestamp();
      
      double batValue = _CANCoder1.getBusVoltage();
      String batUnits = _CANCoder1.getLastUnitString();
      double batTstmp = _CANCoder1.getLastTimestamp();

      /* Report miscellaneous attributes about the CANCoder */
      MagnetFieldStrength magnetStrength = _CANCoder1.getMagnetFieldStrength();
      String magnetStrengthUnits = _CANCoder1.getLastUnitString();
      double magnetStrengthTstmp = _CANCoder1.getLastTimestamp();

      System.out.print("Position: ");
      printValue(posValue, posUnits, posTstmp);
      System.out.print("Abs Pos : ");
      printValue(absValue, absUnits, absTstmp);
      System.out.print("Velocity: ");
      printValue(velValue, velUnits, velTstmp);
      System.out.print("Battery : ");
      printValue(batValue, batUnits, batTstmp);
      System.out.print("Strength: ");
      printValue(magnetStrength, magnetStrengthUnits, magnetStrengthTstmp);

     /* Report position, absolute position, velocity, battery voltage */
     double posValue2 = _CANCoder2.getPosition();
     String posUnits2 = _CANCoder2.getLastUnitString();
     double posTstmp2 = _CANCoder2.getLastTimestamp();
     
     double absValue2 = _CANCoder2.getAbsolutePosition();
     String absUnits2 = _CANCoder2.getLastUnitString();
     double absTstmp2 = _CANCoder2.getLastTimestamp();
     
     double velValue2 = _CANCoder2.getVelocity();
     String velUnits2 = _CANCoder2.getLastUnitString();
     double velTstmp2 = _CANCoder2.getLastTimestamp();
     
     double batValue2 = _CANCoder2.getBusVoltage();
     String batUnits2 = _CANCoder2.getLastUnitString();
     double batTstmp2 = _CANCoder2.getLastTimestamp();

     /* Report miscellaneous attributes about the CANCoder */
     MagnetFieldStrength magnetStrength2 = _CANCoder2.getMagnetFieldStrength();
     String magnetStrengthUnits2 = _CANCoder2.getLastUnitString();
     double magnetStrengthTstmp2 = _CANCoder2.getLastTimestamp();

     System.out.print("Position: ");
     printValue(posValue2, posUnits2, posTstmp2);
     System.out.print("Abs Pos : ");
     printValue(absValue2, absUnits2, absTstmp2);
     System.out.print("Velocity: ");
     printValue(velValue2, velUnits2, velTstmp2);
     System.out.print("Battery : ");
     printValue(batValue2, batUnits2, batTstmp2);
     System.out.print("Strength: ");
     printValue(magnetStrength2, magnetStrengthUnits2, magnetStrengthTstmp2);

      /* Fault reporting */
      CANCoderFaults faults = new CANCoderFaults();
      _CANCoder1.getFaults(faults);
      _CANCoder2.getFaults(faults);
      CANCoderStickyFaults stickyFaults = new CANCoderStickyFaults();
      _CANCoder1.getStickyFaults(stickyFaults);
      _CANCoder2.getStickyFaults(stickyFaults);

      System.out.println("Faults:");
      printFaults(faults);
      System.out.println("Sticky Faults:");
      printFaults(stickyFaults);

      System.out.println();
      System.out.println();
    }
  }

  @Override
  public void robotInit() {
 m_chooser.setDefaultOption("Sides", kDefaultAuto);
 m_chooser.addOption("Middle", kCustomAuto);
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
      case kCustomAuto: 
      
      if(m_Timer.get()>0 & m_Timer.get()<4)
      {leftDrive.set(-.6);}

      if(m_Timer.get()>0 & m_Timer.get()<4)
      {rightDrive.set(.7);}

      if(m_Timer.get()>4 & m_Timer.get()<4.5)
      {leftDrive.stopMotor();}
      if(m_Timer.get()>4 & m_Timer.get()<4.5)
      {rightDrive.stopMotor();}

      if(m_Timer.get()>4.5 & m_Timer.get()<8.5)
      {leftDrive.set(.6);}

      if(m_Timer.get()>4.5 & m_Timer.get()<8.5)
      {rightDrive.set(-.7);}

      if(m_Timer.get()>8)
      {leftDrive.stopMotor();}
      if(m_Timer.get()>8)
      {rightDrive.stopMotor();}
     
    break;
      case kDefaultAuto:
      default:

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

      if(m_Timer.get()>9 & m_Timer.get()<11.5)
      {leftDrive.set(-.6);}


    if(m_Timer.get()>9 & m_Timer.get()<11.5)
    {rightDrive.set(.7);}
      
    if(m_Timer.get()>11.5)
      {leftDrive.stopMotor();}
    if(m_Timer.get()>11.5)
    {leftDrive.stopMotor();}
    break;
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

    // @Override
    // Public void setMotorSpeed(double speed) {

    //Pivot Arm
if (toplimitSwitch.get()) {ErrorCode setPosition = _CANCoder1.setPosition(0);} 
if (yourJoystick.getRawButton(5)) //this is l trigger
if (toplimitSwitch.get()) {gearDrive.set(0);}
      else {gearDrive.set(1);}
      // if (_CANCoder1 Position <200 && _CANCoder2 get.Position <200)
      else {gearDrive.set(0);}

   if (yourJoystick.getRawButton(6))//this is r trigger
      if (bottomlimitSwitch.get()) {gearDrive.set(0);}
      else {gearDrive.set(-1);}
      else {gearDrive.set(0);}

    //Extender Arm
if(yourJoystick.getRawButton(1)) //this is left trigger
    {extenderDrive.set(1);}
    else if (yourJoystick.getRawButton(2))//this is right trigger
    {extenderDrive.set(-1);}
    else {extenderDrive.set(0);}
    //could you please only extend for 3 seconds and not past 4 ft
    
    //Grabber
    if(myJoystick.getRawButton(2)) //A
    {doublesolenoid1.set(DoubleSolenoid.Value.kForward);
    }
    else if(myJoystick.getRawButton(1)) //B
    {doublesolenoid1.set(DoubleSolenoid.Value.kReverse);}
    }

    //Upper Limit Switch
    public void setGearDrive(double speed) {
    if(speed > 0) {
      if (toplimitSwitch.get()) {
        gearDrive.set(0);
      }
      else {
        gearDrive.set(.8);
      }
    }
    else {
      if (bottomlimitSwitch.get()) {
        gearDrive.set(0);
      }
      else {
        gearDrive.set(-.8);
      }
    }
    }
  

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}}
  
