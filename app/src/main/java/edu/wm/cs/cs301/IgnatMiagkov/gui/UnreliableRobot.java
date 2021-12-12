package edu.wm.cs.cs301.IgnatMiagkov.gui;

import android.widget.TextView;

import edu.wm.cs.cs301.IgnatMiagkov.PlayAnimationFragment;
import edu.wm.cs.cs301.IgnatMiagkov.R;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Robot.Direction;

/**Responsibilities: Moves around the maze, extends ReliableRobot. Adds sensor failures based on user input for failure. As unreliable
 * sensors are added, initialize a thread for each of them. || Collaborators RobotDriver, UnreliableSensor
 * @author Ignat Miagkov
 *
 */
public class UnreliableRobot extends ReliableRobot {

	private static final int TIME_TO_REPAIR = 2;
	private static final int TIME_BETWEEN_FAILS = 4;
	
	private String sensors;
	
	/**
	 * Initializes an unreliable robot. Is an extension of reliable robot. Checks to see in user input string for which
	 * sensor need to be initialized as unreliable and create failure and repair process for those sensors. All sensors will still
	 * be initialized as unreliable due to extension from reliable.
	 * @param controller
	 * @param sensors string of 1's 0's from user
	 */
	public UnreliableRobot(PlayAnimationFragment controller, String sensors) {
		super(controller);
		this.sensors = sensors;
		for (int i = 0; i < sensors.length(); i++) {
			if (i == 0) {
				forward = new UnreliableSensor();
				this.addDistanceSensor(forward, Direction.FORWARD);
				if(sensors.charAt(i) == '0') {
					this.startFailureAndRepairProcess(Direction.FORWARD, TIME_BETWEEN_FAILS, TIME_TO_REPAIR);
					try {
						Thread.sleep((long)1300);
					}catch (InterruptedException e) {
						
					}
				}
			}
			else if (i == 1) {
				this.left = new UnreliableSensor();
				this.addDistanceSensor(left, Direction.LEFT);
				if(sensors.charAt(i) == '0') {
					this.startFailureAndRepairProcess(Direction.LEFT, TIME_BETWEEN_FAILS, TIME_TO_REPAIR);
					try {
						Thread.sleep((long)1300);
					}catch (InterruptedException e) {
						
					}
				}
			}
			else if (i == 2) {
				this.right = new UnreliableSensor();
				this.addDistanceSensor(right, Direction.RIGHT);
				if(sensors.charAt(i) == '0') {
					this.startFailureAndRepairProcess(Direction.RIGHT, TIME_BETWEEN_FAILS, TIME_TO_REPAIR);
					try {
						Thread.sleep((long)1300);
					}catch (InterruptedException e) {
						
					}
				}
			}
			else {
				this.backward = new UnreliableSensor();
				this.addDistanceSensor(backward, Direction.BACKWARD);
				if(sensors.charAt(i) == '0') {
					this.startFailureAndRepairProcess(Direction.BACKWARD, TIME_BETWEEN_FAILS, TIME_TO_REPAIR);
					try {
						Thread.sleep((long)1300);
					}catch (InterruptedException e) {
						
					}
				}
			}
		}
	}
	
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		UnreliableSensor sensorToUse = null;
		switch(direction) {
		case FORWARD:
			sensorToUse = (UnreliableSensor) forward;
			break;
		case LEFT:
			sensorToUse = (UnreliableSensor) left;
			break;
		case RIGHT:
			sensorToUse = (UnreliableSensor) right;
			break;
		case BACKWARD:
			sensorToUse = (UnreliableSensor) backward;
			break;
		}
		sensorToUse.createThread();
		sensorToUse.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
	}
	
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		switch(direction) {
		case FORWARD:
			if(this.sensors.charAt(0) == '0')
				forward.stopFailureAndRepairProcess();
			break;
		case LEFT:
			if(this.sensors.charAt(1) == '0')
				left.stopFailureAndRepairProcess();
			break;
		case RIGHT:
			if(this.sensors.charAt(2) == '0')
				right.stopFailureAndRepairProcess();
			break;
		case BACKWARD:
			if(this.sensors.charAt(3) == '0')
				backward.stopFailureAndRepairProcess();
			break;
		}

	}

}
