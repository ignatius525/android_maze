package edu.wm.cs.cs301.IgnatMiagkov.gui;

import android.view.View;

import edu.wm.cs.cs301.IgnatMiagkov.PlayAnimationFragment;
import edu.wm.cs.cs301.IgnatMiagkov.R;
import edu.wm.cs.cs301.IgnatMiagkov.generation.CardinalDirection;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Maze;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Constants.UserInput;
import edu.wm.cs.cs301.IgnatMiagkov.MazeHolder;

/**
 * Responsibilities: perform next move within directly based on input from RobotDriver (Wizard for now), obtain information
 * for absence or presence of walls in any given direction using its sensors, interact with all 4 of its sensors ||
 * Collaborators: RobotDriver (Wizard, doesn't really interact with RobotDriver, but Driver definitely interacts with Robot), 
 * DistanceSensor (ReliableSensor), Controller 
 * 
 * @author Ignat Miagkov
 *
 */
public class ReliableRobot implements Robot {

	private PlayAnimationFragment controller;
	private Maze maze;
	protected DistanceSensor forward;
	protected DistanceSensor backward;
	protected DistanceSensor left;
	protected DistanceSensor right;
	private int odometer;
	protected static float[] battery = new float[1];
	private boolean hasStopped;
	
	private static final int INITIAL_BATTERY = 3600;
	private static final int BATTERY_ROTATION = 3;
	private static final int BATTERY_MOVE = 6;
	private static final int BATTERY_JUMP = 40;
//	private static final CardinalDirection[] directions = {CardinalDirection.South, CardinalDirection.East, CardinalDirection.North, CardinalDirection.West};

	public ReliableRobot(){

	}

	public ReliableRobot(PlayAnimationFragment controller) {
		setController(controller);
		setBatteryLevel(INITIAL_BATTERY);
		odometer = 0;
		hasStopped = false;
	}
	
	public ReliableRobot(PlayAnimationFragment fragment, String sensors) {
		// TODO Auto-generated constructor stub
		setController(fragment);
		setBatteryLevel(INITIAL_BATTERY);
		odometer = 0;
		forward = new ReliableSensor();
		backward = new ReliableSensor();
		left = new ReliableSensor();
		right = new ReliableSensor();
		this.addDistanceSensor(backward, Direction.BACKWARD);
		this.addDistanceSensor(forward, Direction.FORWARD);
		this.addDistanceSensor(left, Direction.LEFT);
		this.addDistanceSensor(right, Direction.RIGHT);
		hasStopped = false;
	}

	@Override
	public void setController(PlayAnimationFragment controller) {
		// TODO Auto-generated method stub
		this.controller = controller;
	}

	@Override
	public void setMaze(Maze mazeConfig){
		this.maze = mazeConfig;
	}

	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		// TODO Auto-generated method stub
		// probably store 4 different instance variable for each sensor and then add as needed
		// since the max possible is 4
		
//		switch(mountedDirection) {
//		case FORWARD:
//			this.forward = (ReliableSensor) sensor;
//			this.forward.setSensorDirection(mountedDirection);
//			break;
//		case BACKWARD:
//			this.backward = (ReliableSensor) sensor;
//			this.backward.setSensorDirection(mountedDirection);
//			break;
//		case LEFT:
//			this.left = (ReliableSensor) sensor;
//			this.left.setSensorDirection(mountedDirection);
//			break;
//		default:
//			this.right = (ReliableSensor) sensor;
//			this.right.setSensorDirection(mountedDirection);
//			break;
//		}
		sensor.setMaze(controller.getMazeConfiguration());
		sensor.setSensorDirection(mountedDirection);
		

	}

	@Override
	public int[] getCurrentPosition() throws Exception {
		// TODO Auto-generated method stub
		int[] currentPosition = controller.getCurrentPosition();
		if (controller.getMazeConfiguration().isValidPosition(currentPosition[0], currentPosition[1]))
			return controller.getCurrentPosition();
		else
			throw new Exception("Position outside maze");
		// store a coord pair for the robots current position in the maze. 
	}

	@Override
	public CardinalDirection getCurrentDirection() {
		// TODO Auto-generated method stub
		return controller.getCurrentDirection();
		// store direction as well
	}

	@Override
	public float getBatteryLevel() {
		// TODO Auto-generated method stub
		return battery[0];
	}

	@Override
	public void setBatteryLevel(float level) {
		// TODO Auto-generated method stub
		battery[0] = level;
	}

	@Override
	public float getEnergyForFullRotation() {
		// TODO Auto-generated method stub
		// store this as a constant value and return when needed
		return 4 * BATTERY_ROTATION;
	}

	@Override
	public float getEnergyForStepForward() {
		// TODO Auto-generated method stub
		return BATTERY_MOVE;
	}

	@Override
	public int getOdometerReading() {
		// TODO Auto-generated method stub
		return odometer;
	}

	@Override
	public void resetOdometer() {
		// TODO Auto-generated method stub
		this.odometer = 0;

	}

	@Override
	public void rotate(Turn turn) {
		// TODO Auto-generated method stub
		//based on the turn passed, change the current direction
		// if (!hasStopped)
		// if (right)
		// 		change to direction point right from current
		// elif (left)
		//		change to point left from current
		// else 
		// 		flip around 180 degrees
		// subtract energy from current energy
		System.out.println("Rotate");
		setBatteryLevel(getBatteryLevel() - BATTERY_ROTATION);
		assert(!hasStopped()):
			"Out of Battery";
//		for (int i = 0; i < directions.length; i++) {
//			if (getCurrentDirection() == directions[i])
//				index = i;
//		}
//		switch(turn) {
//		case AROUND:
//			index  = (index + 2) % 4;
//			break;
//		case LEFT:
//			index = (index + 5) % 4;
//			break;
//		case RIGHT:
//			index = (index + 3) % 4;
//			break;
//		}
//		this.currentDirection = directions[index];
		switch(turn) {
		case AROUND:
//			controller.keyDown(UserInput.LEFT, 1);
			controller.getView().findViewById(R.id.leftButton3).performClick();
			controller.getView().findViewById(R.id.leftButton3).performClick();
//			controller.keyDown(UserInput.LEFT, 1);
			break;
		case LEFT:
			controller.getView().findViewById(R.id.leftButton3).performClick();
//			controller.keyDown(UserInput.LEFT, 1);
			break;
		case RIGHT:
			controller.getView().findViewById(R.id.rightButton3).performClick();
//			controller.keyDown(UserInput.RIGHT, 1);
			break;
		}
	}

	@Override
	public void move(int distance) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		// update current position based on direction facing
		// even better, check what the distance to the wall in front is for my move, and only move forward that many units then break
		// while (!hasStopped())
		// move one unit at a time, checking where the wall in front is
		// if there is still distance to travel forward, but there is a wall in front at distance one, then stop 
		System.out.println("move");
		if(distance < 0)
			throw new IllegalArgumentException("Distance less than 0");
		this.setBatteryLevel(getBatteryLevel() - BATTERY_MOVE);//temporary fix since all my moves are just one cell
		assert(!hasStopped()):
			"Out of Battery";
		while(!hasStopped() && distance > 0) {
			
//			battery[0] = battery[0] - BATTERY_MOVE; this breaks my code and I still do not quite understand why (specifically with WallFollower)
			assert(!hasStopped()):
				"Out of Battery";
			if(this.isOperational(Direction.FORWARD)) {
				if (this.distanceToObstacle(Direction.FORWARD) == 1) {
					this.hasStopped = true;
					assert(!hasStopped()):
						"Crashed into wall";
				}
				else {
//					controller.keyDown(UserInput.UP, 1);
					controller.getView().findViewById(R.id.upButton3).performClick();
					this.odometer++;
					distance--;
				}
			}
			
		}
	}

	@Override
	public void jump() {
		// TODO Auto-generated method stub
		// check if enough energy to complete the jump
		// first check if not stopped
		// do not check for walls or sensors, just move the position of the robot over by 1 unit in the direction its facing
		System.out.println("Jump");
		setBatteryLevel(getBatteryLevel() - BATTERY_JUMP);
		assert(!hasStopped()):
			"Out of battery";
//		switch(this.currentDirection) {
//		case North:
//			assert(controller.getMazeConfiguration().isValidPosition(currentPosition[0], currentPosition[1] - 1)):
//				"Robot landed outside maze, Invalid";
//			controller.keyDown(UserInput.JUMP, 1);
//		case South:
//			assert(controller.getMazeConfiguration().isValidPosition(currentPosition[0], currentPosition[1] + 1)):
//				"Robot landed outside maze, Invalid";
//			currentPosition[1]++;
//		case East:
//			assert(controller.getMazeConfiguration().isValidPosition(currentPosition[0] + 1, currentPosition[1])):
//				"Robot landed outside maze, Invalid";
//			currentPosition[0]++;
//		case West:
//			assert(controller.getMazeConfiguration().isValidPosition(currentPosition[0] - 1, currentPosition[1])):
//				"Robot landed outside maze, Invalid";
//			currentPosition[1]--;
//		}
//		controller.keyDown(UserInput.JUMP, 1);
		this.odometer++;

	}

	public boolean isAtStart() {
		try {
			int[] curr = getCurrentPosition();
			int[] start = controller.getMazeConfiguration().getStartingPosition();
			if (curr[0] == start[0] && curr[1] == start[1]) {
				System.out.println("AT START AND IN ROOM");
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean isAtExit() {
		// TODO Auto-generated method stub
		// check based on current position if the robot is at the exit
		// compare with controller exit position
		// return true if there; otherwise false
		try {
			int[] curr = getCurrentPosition();
			int[] exit = controller.getMazeConfiguration().getExitPosition();
			if (curr[0] == exit[0] && curr[1] == exit[1]) {
				System.out.println("TRUE");
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean isInsideRoom() {
		// TODO Auto-generated method stub
		// based on current position, call on isInRoom for the maze stored in controller
		// return result of isInRoom method call
		try {
			int[] coords = getCurrentPosition();
			if (controller.getMazeConfiguration().isInRoom(coords[0], coords[1])) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public boolean hasStopped() {
		// TODO Auto-generated method stub
		// first check if energy ran out
		// 		return true if energy has indeed ran out
		if (getBatteryLevel() < 0)
			this.hasStopped = true;
		
		return hasStopped;
	}
	
	/**
	 * Checks whether sensor exists/currently operational in a specific direction
	 * @param direction
	 * @return true/false if sensor is currently avaliable in direction
	 */
	public boolean isOperational(Direction direction) {
		try {
			distanceToObstacle(direction);
			return true;
		}catch (UnsupportedOperationException e) {
			return false;
		}
	}

	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		// If the sensor in the specified direction respective to the robots current direction does not exist, throw the exception
		// else, call distanceToObstacle for the correct sensor (check which combo it should be)
		// ex. if the robot is facing south and the param is west, use the east sensor
		ReliableSensor sensorToUse = null;
		
		switch(direction) { // checks which sensor to use based on direction
		case FORWARD:
			sensorToUse = (ReliableSensor)this.forward;
			break;
		case BACKWARD:
			sensorToUse = (ReliableSensor)this.backward;
			break;
		case LEFT:
			sensorToUse = (ReliableSensor)this.left;
			break;
		case RIGHT:
			sensorToUse = (ReliableSensor)this.right;
			break;
		}
		
		if (sensorToUse == null)
			throw new UnsupportedOperationException("No sensor in this direction.");
		if (!sensorToUse.isOperational())
			throw new UnsupportedOperationException("Sensor not currently operational in this direction");
		try {
			return sensorToUse.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), battery);
		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
			throw new UnsupportedOperationException(e);
		}
	}

	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		// call distanceToObstacle for the specified direction (the one directly above this one)
		// if the return value is Integer.MAX_VALUE, return true
		// else return false
		if (distanceToObstacle(direction) == Integer.MAX_VALUE) //return true if sensor says max value
			return true;
		return false;
	}
	
	/**
	 *Toggles the full map and solution path in the gui when wizard is activated.
	 */
	public void toggleMapAndSolution() {
//		controller.keyDown(UserInput.TOGGLEFULLMAP, 1);
//		controller.keyDown(UserInput.TOGGLESOLUTION, 1);
//		controller.keyDown(UserInput.TOGGLELOCALMAP, 1);
		controller.getView().findViewById(R.id.toggleButton3).performClick();
		controller.getView().findViewById(R.id.wallToggle3).performClick();
		controller.getView().findViewById(R.id.solutionSwitch3).performClick();
		controller.getView().findViewById(R.id.toggleButton3).setVisibility(View.GONE);
		controller.getView().findViewById(R.id.wallToggle3).setVisibility(View.GONE);
		controller.getView().findViewById(R.id.solutionSwitch3).setVisibility(View.GONE);
	}

	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
