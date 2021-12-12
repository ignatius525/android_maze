package edu.wm.cs.cs301.IgnatMiagkov.gui;

import android.os.Handler;

import edu.wm.cs.cs301.IgnatMiagkov.PlayAnimationFragment;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Maze;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Robot.Direction;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Robot.Turn;

/**
 * Responsibilities: interact with current mazeConfig and retrieve neighbor closer to exit,
 * tell Robot to perform a specific operation based on situation in maze ||
 * Collaborators: Robot (specifically ReliableRobot), Maze (specifically MazeConfig)
 * 
 * @author Ignat Miagkov
 *
 */
public class Wizard implements RobotDriver {

	Runnable driving;
	private int speed;
	private PlayAnimationFragment controller;
	Handler handler = new Handler();
	private Robot robot;
	private Maze maze;
	private static final int INITIAL_ENERGY = 3600;


	public Wizard() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Assign specific robot for this Wizard to "drive". Uses sensors and position of robot set.
	 */
	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		this.robot = r;
	}

	/**
	 * Assign specific maze for this Wizard. The wizard relies on the maze for information to exit the maze.
	 */
	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		this.maze = maze;

	}

	/**
	 * Using the maze set previously, the wizard uses drives to the exit by using the maze information. 
	 * Once the robot reaches the exit position and its forward direction faces the exit, method terminates and returns true.
	 * Throws an exception if the robot runs into the wall or runs out of energy.
	 * 
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		// try{
		//		while(drive1Step2Exit()){
		//		}
		//		return true;
		//	}
		// catch Exception e{
		//		throw e
		// }
		
		robot.toggleMapAndSolution();
//		try {
//			while(this.drive1Step2Exit()) {
//				if (getPathLength() > (this.maze.getHeight() * this.maze.getWidth())){
//					return false;
//				}
//			}
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}

		 driving = new Runnable() {
			@Override
			public void run() {
				try{
					if (drive1Step2Exit()){
						handler.postDelayed(this, speed);
					}
					else{
						handler.removeCallbacks(this);
					}
				} catch (Exception e){
					e.printStackTrace();
					System.exit(1);
				}
			}
		};
		handler.post(driving);
		return true;

	}

	/**
	 * Using the maze set previously, drives one step closer to the exit. Return true if the robot successfully moved to an adjacent cell. 
	 * At the exit position, turns the robot to face the exit direction and returns false. 
	 * Throws an exception if runs into a wall or runs out of energy.
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		//
		//	get current position of robot
		//	check for neighbor closer to exit based on maze
		//	if (current position is exit position)
		//		rotate to look directly at exit
		//		return false;
		//	if (enough energy and there is no wall in between)
		//  	move robot to that position
		//	else
		//		throw exception
		//	pathlength ++
		// energy consumed += how much it was
		//
		//
		controller.bat.setText("BATTERY LEVEL: " + robot.getBatteryLevel());
		if (robot.isAtExit()) { // this checks when the robot is at exit. Idk why this logic works, but for some reason I need to turn the opposite direction of where I detected eternity
			if (robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT)) {
				robot.rotate(Turn.LEFT);
			}
			if (robot.canSeeThroughTheExitIntoEternity(Direction.LEFT)) {
				robot.rotate(Turn.RIGHT);
			}
			robot.move(1);
//			robot.stopFailureAndRepairProcess(Direction.FORWARD);
//			robot.stopFailureAndRepairProcess(Direction.LEFT);
//			robot.stopFailureAndRepairProcess(Direction.RIGHT);
//			robot.stopFailureAndRepairProcess(Direction.BACKWARD);
			return false;
		}
		
		int[] coords = robot.getCurrentPosition();
		int[] neighbor = maze.getNeighborCloserToExit(coords[0], coords[1]);
		// all the different cases based on which direction teh robot is currently facing and where the next best location is
		if (coords[0] == neighbor[0] && coords[1] == neighbor[1] - 1) {
			switch(robot.getCurrentDirection()) {
			case South:
				break;
			case North:
				robot.rotate(Turn.AROUND);
				break;
			case East:
				robot.rotate(Turn.LEFT);
				break;
			case West:
				robot.rotate(Turn.RIGHT);
				break;
			}	
		}
		
		else if (coords[0] == neighbor[0] && coords[1] == neighbor[1] + 1) {
			switch(robot.getCurrentDirection()) {
			case South:
				robot.rotate(Turn.AROUND);
				break;
			case North:
				break;
			case East:
				robot.rotate(Turn.RIGHT);
				break;
			case West:
				robot.rotate(Turn.LEFT);
				break;
			}
		}
		
		else if (coords[1] == neighbor[1] && coords[0] == neighbor[0] + 1) {
			switch(robot.getCurrentDirection()) {
			case South:
				robot.rotate(Turn.LEFT);
				break;
			case North:
				robot.rotate(Turn.RIGHT);
				break;
			case East:
				robot.rotate(Turn.AROUND);
				break;
			case West:
				break;
			}
		}
		
		else {
			switch(robot.getCurrentDirection()) {
			case South:
				robot.rotate(Turn.RIGHT);
				break;
			case North:
				robot.rotate(Turn.LEFT);
				break;
			case East:
				break;
			case West:
				robot.rotate(Turn.AROUND);
				break;
			}
		}
		//if the next best location is across a wall, jumo over. This should never happen since bestNeighbor is never separated by wall
		if (maze.hasWall(coords[0], coords[1], robot.getCurrentDirection()))
			robot.jump();
		else
			robot.move(1);
		return true;
	}

	/**
	 * Returns the total energy consumption of the journey (final - initial). Used to measure efficiency of wizard. 
	 */
	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		// store with some instance variable
		return INITIAL_ENERGY - robot.getBatteryLevel();
	}

	/**
	 * Return total path length of journey via cells traversed. 
	 */
	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		// store with some instance variable
		return robot.getOdometerReading();
	}

	@Override
	public void stopHandler(){
		handler.removeCallbacks(driving);
	}

	@Override
	public void setSpeed(int time) {
		speed = 80 * time + 20;
	}

	@Override
	public void setController(PlayAnimationFragment frag) {
		this.controller = frag;
	}
}
