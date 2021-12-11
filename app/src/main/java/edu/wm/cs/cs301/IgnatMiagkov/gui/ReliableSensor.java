package edu.wm.cs.cs301.IgnatMiagkov.gui;

import edu.wm.cs.cs301.IgnatMiagkov.generation.CardinalDirection;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Maze;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Robot.Direction;

/**
 * Responsibilities: Provide distance to closest wall in direction mounted, translate distance between wall into global direction
 * (not just the direction it is mounted in) || 
 * Collaborators: Robot (RobotDriver, receives calls from Robot), Maze (use Floorplan of Maze) 
 * @author Ignat Miagkov
 *
 */
public class ReliableSensor implements DistanceSensor {
	
	private Maze maze;
	protected Direction direction;
	protected static boolean isOperational;
	
	private static final CardinalDirection[] directions = {CardinalDirection.South, CardinalDirection.East, CardinalDirection.North, CardinalDirection.West};
	private static final int BATTERY_SCAN = 1;
	
	public ReliableSensor() {
		// TODO Auto-generated constructor stub
		isOperational = true;
	}

	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		// TODO Auto-generated method stub
		
		//check parameters first , such as if current position is valid, and enough power to scan 
		// based on which direction the robot is currently looking, check until hit a wallboard facing the same direction
		// ex) if facing north, keep looking until north wallboard is found
		// if at any point, the distance of the scan from the current position is larger than the distance to the border
		// 		return max_value
		// update powersupply based on successful scan
		if (!maze.isValidPosition(currentPosition[0], currentPosition[1]))
			throw new IllegalArgumentException("Invalid Position");
		if (currentPosition == null || currentDirection == null || powersupply == null)
			throw new IllegalArgumentException("Null parameters");
		if ((!isOperational))
			throw new Exception("SensorFailure");
		if (powersupply[0] < 0)
			throw new IndexOutOfBoundsException("No power");
		if (powersupply[0] < getEnergyConsumptionForSensing())
			throw new Exception("PowerFailure");
		
		
		int index = 0;
		int count = 0;
		for (int i = 0; i < directions.length; i++) {
			if (currentDirection == directions[i])
				index = i;
		}
		switch(this.direction) {
		case BACKWARD:
			index = (index + 2) % 4;
			break;
		case LEFT:
			index = (index + 5) % 4;
			break;
		case RIGHT:
			index = (index + 3) % 4;
			break;
		case FORWARD:
			break;
		}
		
		// based on which CardinalDirection we are currently facing, scan in that direction
		switch(directions[index]) {
		case South:
			count = scanSouth(currentPosition);
			break;
		case North:
			count = scanNorth(currentPosition);
			break;
		case East:
			count = scanEast(currentPosition);
			break;
		case West:
			count = scanWest(currentPosition);
			break;
		}
		return count;
	}
	
	/**
	 * Sensor scans south to find the next closest obstacle. First checks if the position we scan are valid, then
	 * if there is an obstacle on the south side of the current location.
	 * @param currentPosition
	 * @return distance to next obstacle. If looking at exit, returns Integer.MAX_VALUE
	 */
	private int scanSouth(int[] currentPosition) {
		int count = 1;
		int iter = 0;
		while(maze.isValidPosition(currentPosition[0], currentPosition[1] + iter)) {
			if (maze.hasWall(currentPosition[0], currentPosition[1] + iter, CardinalDirection.South))
				return count;
			count++;
			iter++;
		}
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Sensor scans north to find the next closest obstacle. First checks if the position we scan are valid, then
	 * if there is an obstacle on the north side of the current location.
	 * @param currentPosition
	 * @return distance to next obstacle. If looking at exit, returns Integer.MAX_VALUE
	 */
	private int scanNorth(int[] currentPosition) {
		int count = 1;
		int iter = 0;
		while(maze.isValidPosition(currentPosition[0], currentPosition[1] + iter)) {
			if (maze.hasWall(currentPosition[0], currentPosition[1] + iter, CardinalDirection.North))
				return count;
			count++;
			iter--;
		}
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Sensor scans west to find the next closest obstacle. First checks if the position we scan are valid, then
	 * if there is an obstacle on the west side of the current location.
	 * @param currentPosition
	 * @return distance to next obstacle. If looking at exit, returns Integer.MAX_VALUE
	 */
	private int scanWest(int[] currentPosition) {
		
		int count = 1;
		int iter = 0;
		while(maze.isValidPosition(currentPosition[0] + iter, currentPosition[1])) {
			if (maze.hasWall(currentPosition[0] + iter, currentPosition[1], CardinalDirection.West))
				return count;
			count++;
			iter--;
		}
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Sensor scans east to find the next closest obstacle. First checks if the position we scan are valid, then
	 * if there is an obstacle on the east side of the current location.
	 * @param currentPosition
	 * @return distance to next obstacle. If looking at exit, returns Integer.MAX_VALUE
	 */
	private int scanEast(int[] currentPosition) {
		int count = 1;
		int iter = 0;
		while(maze.isValidPosition(currentPosition[0] + iter, currentPosition[1])) {
			if (maze.hasWall(currentPosition[0] + iter, currentPosition[1], CardinalDirection.East))
				return count;
			count++;
			iter++;
		}
		return Integer.MAX_VALUE;
	}
	
	/**
	 * Check whether the current sensor is operational or not.
	 * @return sensor operation status. True for yes. false for no.
	 */
	public boolean isOperational() {
		return isOperational;
	}

	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		this.maze = maze;

	}

	@Override
	public void setSensorDirection(Direction mountedDirection) {
		// TODO Auto-generated method stub
		this.direction = mountedDirection;

	}

	@Override
	public float getEnergyConsumptionForSensing() {
		// TODO Auto-generated method stub
		// return 1 lol
		return BATTERY_SCAN;
	}

	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
