package edu.wm.cs.cs301.IgnatMiagkov.gui;

import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TextView;

import edu.wm.cs.cs301.IgnatMiagkov.PlayAnimationFragment;
import edu.wm.cs.cs301.IgnatMiagkov.R;
import edu.wm.cs.cs301.IgnatMiagkov.generation.CardinalDirection;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Maze;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Robot.Direction;

/**
 * Responsibilities: Determine distance to closest wall based on mount direction, malfunction randomly, relay distance in terms of global directions||
 * Collaborators: ReliableSensor, Robot, Maze
 * @author Ignat Miagkov
 *
 */
public class UnreliableSensor extends ReliableSensor implements Runnable{

	long timeFail;
	long timeRepair;
	private PlayAnimationFragment frag;
	private Thread failThread;
	private TextView view;

	/**
	 * Action of failThread. While the thread is not interrupted, sleep for 4 seconds, break sensor, wait for another 2, fix, repeat
	 * When thread is interrupted, print message and yield memory.
	 */
	public void run() {
		try {
			while(Thread.currentThread().isAlive()) {
				Thread.sleep(4000);
				breaking();
//				threadMessage(this.direction + " BROKEN");
//				view.setTextColor(Color.RED);
//				view.bringToFront();
//				view.setText(view.getText() + ": OFF");
				Thread.sleep(2000);
				this.fixing();
//				view.setTextColor(Color.GREEN);
//				view.setText(view.getText() + ": ON");
				threadMessage(this.direction+ "  FIXED");
			}
		}catch (InterruptedException e) {
			threadMessage("Thread Interrupted");
			Thread.yield();
		}
	}

	/**
	 * Initializes an unreliable sensor. Starts new thread with each new unreliable sensor initialized
	 */
	public UnreliableSensor() {
		// TODO Auto-generated constructor stub
		isOperational = true;
	}
	
	public void breaking() {
		isOperational = false;
	}
	
	public void fixing() {
		isOperational = true;
	}
	
	/**
	 * Creates a new thread for the sensor when called from the startFailure method call from UnreliableRobot. 
	 * Made sense to separate the thread creation and start into two different methods.
	 */
	public void createThread() {
		failThread = new Thread(new UnreliableSensor());
	}
	
	
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		timeFail = meanTimeBetweenFailures * 1000;
		timeRepair = meanTimeToRepair * 1000;
		failThread.start();
	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		failThread.interrupt();
	}
	
	/**
	 * Ripped directly from Java docs. If exception is thrown, prints out which thread and specific message for that thread.
	 * @param message
	 */
	static void threadMessage(String message) {
        String threadName =
            Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                          threadName,
                          message);
    }
	
}
