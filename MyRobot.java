/*
 *        Name: Arshan Alam, Ryan Williams, Karim Guirguis, Ian Gregor, John Brunelle
 * 
 *        Date: Thursday, October 25, 2012
 * 
 * Description: This program Demonstrates the use of motors,
 * 				light sensors, and basic turning.
 * 
 * */

import lejos.nxt.*;	// Import of entire package

public class MyRobot
{

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


		int rightColor = rightSensor.getLightValue();	// getting initial tracks light values
		int leftColor = leftSensor.getLightValue();	// getting initial tracks light values
		final int REVERSE_SPEED = 50; // Speed of robot when reversing
		final int MAX_SPEED = 100;	// Maximum speed percentage Constant
		final int SENSOR_RANGE = 4;	// Range the sensors will detect the tracks colors
		final int TURNING_DIVISOR = 3;	// Divisor for the dividend MAX_SPEED, used when turning


		// Infinite While loop for the program
		while(true)
		{

			// Method called to reset all motor values to default
			//-----------------------------------------------------------//
			resetDefaultMotors(leftMotor, middleMotor, rightMotor, MAX_SPEED);
			//-----------------------------------------------------------//

			// Selective structure if the right light sensor is off track
			//-----------------------------------------------------------//
			if (isSensorOff(rightSensor, rightColor, SENSOR_RANGE))
			{
				// Conditional while loop to turn robot back onto track
				while(isSensorOff(rightSensor, rightColor, SENSOR_RANGE))
				{
					leftMotor.setPower(MAX_SPEED/TURNING_DIVISOR);	// Slow down the Left Motor
					leftMotor.backward();	// Reverse the direction of rotation of the motor

					rightMotor.setPower(MAX_SPEED);	// set Right Motor to Max Speed
					rightMotor.forward();	// Set Right Motor Forward
				}	// end of conditional while loop

			}	// end of right sensor off selective structure
			//-----------------------------------------------------------//

			// Selective structure if the left light sensor is off track
			//-----------------------------------------------------------//
			if (isSensorOff(leftSensor, leftColor, SENSOR_RANGE))
			{

				// Conditional while loop to turn robot back onto track
				while(isSensorOff(leftSensor, leftColor, SENSOR_RANGE))
				{
					rightMotor.setPower(MAX_SPEED/TURNING_DIVISOR);	// Slow down the Right Motor
					rightMotor.backward();	// Reverse the direction of rotation of the motor

					leftMotor.setPower(MAX_SPEED);	// set Left Motor to Max Speed
					leftMotor.forward();	// set Left Motor Forward
				} // end of conditional while loop

			}	// end of left sensor off selective structure
			//-----------------------------------------------------------//

			
			// Selective structure if the both light sensor are off track
			//-----------------------------------------------------------//
			if (robotOffTrack(leftSensor, rightSensor, leftColor, rightColor, SENSOR_RANGE))
			{
				// Conditional while loop for robot off track
				while(robotOffTrack(leftSensor, rightSensor, leftColor, rightColor, SENSOR_RANGE))
				{
					leftMotor.setPower(REVERSE_SPEED);
					rightMotor.setPower(REVERSE_SPEED);
					
					leftMotor.backward();
					rightMotor.backward();
				}	// end of conditional while loop for robot off track
				
			}	// End of robot off track selective structure
			
		}	// end of Applications Infinite While Loop

	}	// end of main method
	// ------------------------------------------------------------------------------------------------------------//

}	// end of class