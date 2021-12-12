package edu.wm.cs.cs301.IgnatMiagkov.gui;

import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;

import edu.wm.cs.cs301.IgnatMiagkov.PlayAnimationFragment;
import edu.wm.cs.cs301.IgnatMiagkov.R;
import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentGeneratingBinding;
import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentPlayAnimationBinding;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Maze;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Robot.Direction;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Robot.Turn;

/**
 * Responsibilities: interacts with Robot to determine next move based on its position, get Maze and use sensors of Robot for this specific algorithm||
 * Decision making for WallFollower: 
 * Collaborators: Maze, Robot (either Reliable or Unreliable) ||
 * Strategy: follow left hand wall until its no longer there. If left sensor goes offline, rotate to another sensor that is currently operational and come back,
 * If no other sensors available, wait until it comes back and proceed
 * @author Ignat Miagkov
 *
 */
public class WallFollower implements RobotDriver {

	PlayAnimationFragment controller;
	Runnable driving;
	private int speed;
	Handler handler = new Handler();
	private Robot robot;
	private Maze maze;
	private static final int INITIAL_ENERGY = 3600;

	public WallFollower(){

	}

	public void setController(PlayAnimationFragment fragment){
		controller = fragment;
	}

	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		this.robot = r;
	}

	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		this.maze = maze;
	}

	@Override
	public boolean drive2Exit() throws Exception {
		// TODO Auto-generated method stub
		robot.toggleMapAndSolution();
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

				}
			}
		};
		handler.post(driving);
		return false;
	}

	@Override
	public boolean drive1Step2Exit() throws Exception {
		// TODO Auto-generated method stub
		// if at exit, rotate in proper direction and move forward to exit
		
		//else, check if left sensor is operational
		//if it is, check for wall on left, and then check if wall in front
		//if there is a wall on the left and there is no wall in front, then move forward,
		//if there is wall on left and wall in front, then rotate right move forward
		//if there is a wall on left, right, and in front, i need to turn around and move forward
		
		//perhaps include a check for whether sensor is operational, and then have a priority queue for which direction i want to tur
		//in to detect wall
		
		//if (isOperational(left)){
		//	do the check
		//}
		//else{
		//	if (isOperational(forward)){
		//		do the check
		// etc.
		// probably dont need to do more than 3, due to gaps of 1.3 seconds between sensors and constant downtimes for all of them
//		setSensorsOnView();
//		System.out.println(robot.getBatteryLevel());
		controller.bat.setText("BATTERY LEVEL: " + robot.getBatteryLevel());
		if (robot.isInsideRoom() && ((ReliableRobot)robot).isAtStart()) {
			if(getOutOfRoom()) {
				return true;
			}
		}
		
		if (robot.isAtExit()) {
			if(getOutOfMazeAtExit()) {
				return false;
			}
		}
		if (((ReliableRobot)robot).isOperational(Direction.RIGHT)) { // is my left sensor operational
			if(robot.distanceToObstacle(Direction.RIGHT) != 1) { // if there is no wall, rotate left and move
				robot.rotate(Turn.LEFT);
				robot.move(1);
			}
			else { //however, if my left sensor is down
				if(((ReliableRobot)robot).isOperational(Direction.FORWARD)) { //check if forward is available
					if(robot.distanceToObstacle(Direction.FORWARD) != 1) { //if no wall forward, move forward
						robot.move(1);
					} 
					else { // however, if both my left and forward are down, check if right is up 
//						if(((ReliableRobot)robot).isOperational(Direction.LEFT)) {
//							if(robot.distanceToObstacle(Direction.LEFT) != 1) { //rotate right if no wall to right and move
//								robot.rotate(Turn.RIGHT);
//								robot.move(1);
//							}
//							else {
//								robot.rotate(Turn.AROUND); //otherwise, in a dead end and turn around
//								robot.move(1);
//							}
//						}
//						else { //if right is down, wait until it comes back up, same logic as previous
							while(!((ReliableRobot)robot).isOperational(Direction.LEFT)) {
							}
							if(robot.distanceToObstacle(Direction.LEFT) != 1) {
								robot.rotate(Turn.RIGHT);
								robot.move(1);
							}
							else {
								robot.rotate(Turn.AROUND);
								robot.move(1);
							}
//						}
					}
				}
				else {
					while(!((ReliableRobot)robot).isOperational(Direction.FORWARD)) { // if forward is down, wait until comes back up
					}
					if(robot.distanceToObstacle(Direction.FORWARD) != 1) { //same logic as above 
						robot.move(1);
					}
					else {
						while(!((ReliableRobot)robot).isOperational(Direction.LEFT)) {
						}
						if(robot.distanceToObstacle(Direction.LEFT) != 1) {
							robot.rotate(Turn.RIGHT);
							robot.move(1);
						}
						else {
							robot.rotate(Turn.AROUND);
							robot.move(1);
						}
					}
				}
			}
		}
		else {
			while(!((ReliableRobot)robot).isOperational(Direction.RIGHT)) { // if left is down, wait until left comes back up
			}
			if(robot.distanceToObstacle(Direction.RIGHT) != 1) { //same logic for all of this as large chunk above
				robot.rotate(Turn.LEFT);
				robot.move(1);
			}
			else {
				if(((ReliableRobot)robot).isOperational(Direction.FORWARD)) {
					if(robot.distanceToObstacle(Direction.FORWARD) != 1) {
						robot.move(1);
					}
					else {
						while(!((ReliableRobot)robot).isOperational(Direction.LEFT)) {
						}
						if(robot.distanceToObstacle(Direction.LEFT) != 1) {
							robot.rotate(Turn.RIGHT);
							robot.move(1);
						}
						else {
							robot.rotate(Turn.AROUND);
							robot.move(1);
						}
					}
				}
				else {
					while(!((ReliableRobot)robot).isOperational(Direction.FORWARD)) {
						System.out.println("WAITING ON FORWARD");
					}
					if(robot.distanceToObstacle(Direction.FORWARD) != 1) {
						robot.move(1);
					}
					else {
						while(!((ReliableRobot)robot).isOperational(Direction.LEFT)) {
							System.out.println("WAITING ON RIGHT");
						}
						if(robot.distanceToObstacle(Direction.LEFT) != 1) {
							robot.rotate(Turn.RIGHT);
							robot.move(1);
						}
						else {
							robot.rotate(Turn.AROUND);
							robot.move(1);
						}
					}
				}
			}
		}

		return true;
	}

	private void setSensorsOnView(){
		TextView forward = controller.getView().findViewById(R.id.sensorForward);
		if (((ReliableRobot) robot).isOperational(Direction.FORWARD)){
			forward.setTextColor(Color.GREEN);
			forward.bringToFront();
			forward.setText("Sensor Forward: ON");
		}
		else{
			forward.setTextColor(Color.RED);
			forward.bringToFront();
			forward.setText("Sensor Forward: OFF");
		}

		TextView back = controller.getView().findViewById(R.id.backSensor);
		if (((ReliableRobot) robot).isOperational(Direction.BACKWARD)){
			back.setTextColor(Color.GREEN);
			back.bringToFront();
			back.setText("Sensor BACKWARD: ON");
		}
		else{
			back.setTextColor(Color.RED);
			back.bringToFront();
			back.setText("Sensor BACKWARD: OFF");
		}

		TextView left = controller.getView().findViewById(R.id.leftSensor);
		if (((ReliableRobot) robot).isOperational(Direction.LEFT)){
			left.setTextColor(Color.GREEN);
			left.bringToFront();
			left.setText("Sensor LEFT: ON");
		}
		else{
			left.setTextColor(Color.RED);
			left.bringToFront();
			left.setText("Sensor LEFT: OFF");
		}

		TextView right = controller.getView().findViewById(R.id.rightSensor);
		if (((ReliableRobot) robot).isOperational(Direction.RIGHT)){
			right.setTextColor(Color.GREEN);
			right.bringToFront();
			right.setText("Sensor RIGHT: ON");
		}
		else{
			right.setTextColor(Color.RED);
			right.bringToFront();
			right.setText("Sensor RIGHT: OFF");
		}
	}

	/** Get robot out of room with no surrounding walls to guide it.
	 * @return if the robot successfully moved out of a room with no walls around it
	 */
	private boolean getOutOfRoom() {
		int count = 0;
		int save = 1;
		while(!((ReliableRobot)robot).isOperational(Direction.FORWARD)) {
		}
		if (robot.distanceToObstacle(Direction.FORWARD) != 1) {
			count++;
		}
		while(!((ReliableRobot)robot).isOperational(Direction.LEFT)) {
		}
		if (robot.distanceToObstacle(Direction.LEFT) != 1) {
			count++;
		}
		while(!((ReliableRobot)robot).isOperational(Direction.RIGHT)) {
		}
		if (robot.distanceToObstacle(Direction.RIGHT) != 1) {
			save = robot.distanceToObstacle(Direction.RIGHT);
			count++;
		}
		while(!((ReliableRobot)robot).isOperational(Direction.BACKWARD)) {
		}
		if (robot.distanceToObstacle(Direction.LEFT) != 1) {
			count++;
		}
		if (count == 4) {
			robot.move(save - 1);
			return true;
		}
		return false;
	}
	
	/**Helper method to get robot with wallFollower out of the maze when it is at exit.
	 * @return true for robot left the maze, false if the left hand wall wasnt there and kept going
	 */
	private boolean getOutOfMazeAtExit() {
		while(!((ReliableRobot)robot).isOperational(Direction.RIGHT)) {
		}
		if (robot.distanceToObstacle(Direction.RIGHT) == Integer.MAX_VALUE) {
			robot.rotate(Turn.LEFT);
			robot.move(1);
			return true;
		}
		while(!((ReliableRobot)robot).isOperational(Direction.FORWARD))
		if (robot.distanceToObstacle(Direction.FORWARD) == Integer.MAX_VALUE) {
			robot.move(1);
			return true;
		}
		return false;
	}
	
	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		return INITIAL_ENERGY - robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return robot.getOdometerReading();
	}

	@Override
	public void stopHandler() {
		handler.removeCallbacks(driving);
	}

	@Override
	public void setSpeed(int time){
		speed = 40 * time + 20;
	}

}
