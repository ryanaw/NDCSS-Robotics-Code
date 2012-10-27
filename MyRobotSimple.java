 /*
 *		  Program: Simplified Robot Controller
 *   
 *        Authors: Arshan Alam, Ryan Williams, Karim Guirguis, Ian Gregor, John Brunelle
 * 
 *        Date: Thursday, October 25, 2012
 * 
 * 		  Description: This program Demonstrates the use of motors,
 * 						light sensors, and basic turning.
 * 
 * */
import lejos.nxt.*;	// Import of entire package

public class MyRobotSimple
{
	// Enumerate statues for robot
	// ------------------------------------------------------------------------------------------------------------//
	public enum NXJStatus {
		NXJStatusOkay, NXJStatusOffTrack, NXJStatusShouldTurnLeft, NXJStatusShouldTurnRight
	}
	// ------------------------------------------------------------------------------------------------------------//
	
	// Method to check if the robot is completely off track
	// ------------------------------------------------------------------------------------------------------------//
	static boolean robotOffTrack(LightSensor leftSensor, LightSensor rightSensor, int leftInitialColor, int rightInitialColor, int range)
	{
		return (
				isSensorOff(leftSensor, leftInitialColor, range) && /* Check to see if left Light Sensor is off track */
				isSensorOff(rightSensor, rightInitialColor, range) /* Check to see if right Light Sensor is off track */
				);
	}	// end of robotOffTrack
	// ------------------------------------------------------------------------------------------------------------//
	
	// Method to check if the sensor are off track
	// ------------------------------------------------------------------------------------------------------------//
	static boolean isSensorOff(LightSensor sensor, int initialColor, int range) 
	{
		return (
				sensor.getLightValue() < (initialColor - range) ||
				sensor.getLightValue() > (initialColor + range)
				);	// end of return of boolean
	}	// end of method isSensorOff();
	// ------------------------------------------------------------------------------------------------------------//
	static NXJStatus updateStatus(LightSensor leftSensor, LightSensor rightSensor, int leftInitialColor, int rightInitialColor, int range){
		NXJStatus returnStatus = NXJStatus.NXJStatusOkay; // Initialize the returned status as okay
		if (robotOffTrack(leftSensor, rightSensor, leftInitialColor, rightInitialColor, range))
			returnStatus = NXJStatus.NXJStatusOffTrack; // The robot is off track
		else if (isSensorOff(rightSensor, rightInitialColor, range))
			returnStatus = NXJStatus.NXJStatusShouldTurnLeft; // The right sensor is off
		else if (isSensorOff(leftSensor, leftInitialColor, range))
			returnStatus = NXJStatus.NXJStatusShouldTurnRight; // The left sensor is off
		return returnStatus;
	}
	// Method to reset all Motor values to default
	// ------------------------------------------------------------------------------------------------------------//
	static void resetDefaultMotors(NXTMotor leftMotor, NXTMotor middleMotor, NXTMotor rightMotor, int defaultSpeed)
	{
		// reset values After Each Iteration
		//-------------------------------------------------//
		rightMotor.setPower(defaultSpeed); 
		leftMotor.setPower(defaultSpeed);
		middleMotor.setPower(defaultSpeed);

		leftMotor.forward();
		rightMotor.forward();
		middleMotor.backward();	// Motor is attached Backwards
		//-------------------------------------------------//
	}
	// ------------------------------------------------------------------------------------------------------------//

	// Main Method
	// ------------------------------------------------------------------------------------------------------------//
	public static void main(String[] args) throws Exception 
	{

		// Declarations
		// ------------------------------------------------------//
		LightSensor rightSensor = new LightSensor(SensorPort.S3);
		LightSensor leftSensor = new LightSensor(SensorPort.S2);

		NXTMotor rightMotor = new NXTMotor(MotorPort.C);
		NXTMotor leftMotor = new NXTMotor(MotorPort.A); 
		NXTMotor middleMotor = new NXTMotor(MotorPort.B);
		// ------------------------------------------------------//

		Thread.sleep(5000);	// Program requirement of delay for 5 seconds

		int rightColor = rightSensor.getLightValue(); // getting initial tracks light values
		int leftColor = leftSensor.getLightValue(); // getting initial tracks light values
		final int REVERSE_SPEED = 50; // Speed of robot when reversing
		final int MAX_SPEED = 100;	// Maximum speed percentage Constant
		final int SENSOR_RANGE = 4;	// Range the sensors will detect the tracks colors
		final int TURNING_DIVISOR = 3;	// Divisor for the dividend MAX_SPEED, used when turning

		// Infinite While loop for the program
		while(true)
		{
			NXJStatus robotStatus = updateStatus(leftSensor, rightSensor, leftColor, rightColor, SENSOR_RANGE);
			switch (robotStatus) {
				case NXJStatusOffTrack:
					while(updateStatus(leftSensor, rightSensor, leftColor, rightColor, SENSOR_RANGE) == NXJStatus.NXJStatusOffTrack)
					{
						leftMotor.setPower(REVERSE_SPEED);
						rightMotor.setPower(REVERSE_SPEED);
										
						leftMotor.backward();
						rightMotor.backward();
					}
					break;
				case NXJStatusShouldTurnLeft:
				case NXJStatusShouldTurnRight:
					while (updateStatus(leftSensor, rightSensor, leftColor, rightColor, SENSOR_RANGE) == NXJStatus.NXJStatusShouldTurnLeft ||
							updateStatus(leftSensor, rightSensor, leftColor, rightColor, SENSOR_RANGE) == NXJStatus.NXJStatusShouldTurnRight) 
					{
						// Ternary operator to choose the motor on the outside of the turn
						NXTMotor primaryTurnMotor = (robotStatus == NXJStatus.NXJStatusShouldTurnLeft) ? rightMotor : leftMotor;
						// Ternary operator to choose the motor on the inside of the turn
						NXTMotor secondaryTurnMotor = (robotStatus == NXJStatus.NXJStatusShouldTurnLeft) ? leftMotor : rightMotor;
						
						secondaryTurnMotor.setPower(MAX_SPEED/TURNING_DIVISOR);
						secondaryTurnMotor.backward();

						primaryTurnMotor.setPower(MAX_SPEED);
						primaryTurnMotor.forward();
					}
					break;
				default:
					// Method called to reset all motor values to default
					//-----------------------------------------------------------//
					resetDefaultMotors(leftMotor, middleMotor, rightMotor, MAX_SPEED);
					//-----------------------------------------------------------//
					break;
			}
		}	// end of Applications Infinite While Loop

	}	// end of main method
	// ------------------------------------------------------------------------------------------------------------//

}	// end of class