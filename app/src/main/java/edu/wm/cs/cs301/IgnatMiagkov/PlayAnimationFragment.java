package edu.wm.cs.cs301.IgnatMiagkov;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentPlayAnimationBinding;
import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentPlayingManuallyBinding;
import edu.wm.cs.cs301.IgnatMiagkov.generation.CardinalDirection;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Floorplan;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Maze;
import edu.wm.cs.cs301.IgnatMiagkov.gui.CompassRose;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Constants;
import edu.wm.cs.cs301.IgnatMiagkov.gui.FirstPersonView;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Map;
import edu.wm.cs.cs301.IgnatMiagkov.gui.MazePanel;
import edu.wm.cs.cs301.IgnatMiagkov.gui.ReliableRobot;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Robot;
import edu.wm.cs.cs301.IgnatMiagkov.gui.RobotDriver;

import edu.wm.cs.cs301.IgnatMiagkov.RobotHolder;
import edu.wm.cs.cs301.IgnatMiagkov.gui.UnreliableRobot;
import edu.wm.cs.cs301.IgnatMiagkov.gui.WallFollower;


public class PlayAnimationFragment extends Fragment {

    public TextView bat;
    private FragmentPlayAnimationBinding binding;
    private int countButtonClicks;

    private MazePanel panel;
    private Maze mazeConfig;
    FirstPersonView firstPersonView;
    Map mapView;
    private int distanceTraveled;
    int minDistance;
    private boolean showMaze;           // toggle switch to show overall maze on screen
    private boolean showSolution;       // toggle switch to show solution in overall maze on screen
    private boolean mapMode; // true: display map of maze, false: do not display map of maze
    // mapMode is toggled by user keyboard input, causes a call to drawMap during play mode

    // current position and direction with regard to MazeConfiguration
    int px, py ; // current position on maze grid (x,y)
    int dx, dy;  // current direction

    int angle; // current viewing angle, east == 0 degrees
    int walkStep; // counter for intermediate steps within a single step forward or backward
    Floorplan seenCells; // a matrix with cells to memorize which cells are visible from the current point of view
    // the FirstPersonView obtains this information and the Map uses it for highlighting currently visible walls on the map
    private CompassRose cr; // compass rose to show current direction

    private String sensors;

    Robot robot;
    RobotDriver driver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPlayAnimationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        clicks = getView().findViewById(R.id.clicks);
        //Button upButton = getView().findViewById(R.id.upButton);
//        TextView win = getView().findViewById(R.id.winning);
//        win.setVisibility(View.GONE);
        panel = getView().findViewById(R.id.mazePanel2);
        getView().findViewById(R.id.go2winning).setVisibility(View.GONE);
        getView().findViewById(R.id.go2losing).setVisibility(View.GONE);

        binding.upButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Up Button has been hit", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
                countButtonClicks++;
                walk(1);
                if (isOutside(px,py)){
                    driver.stopHandler();
                    Bundle result = new Bundle();
                    result.putFloat("batteryUsed", driver.getEnergyConsumption());
                    result.putInt("distanceTraveled", driver.getPathLength());
                    result.putInt("minDistance", minDistance);
                    getParentFragmentManager().setFragmentResult("forWinScreenManual", result);
                    NavHostFragment.findNavController(PlayAnimationFragment.this)
                            .navigate(R.id.action_playAnimationFragment_to_winningFragment);
                }
//                clicks.setText("Clicks to Win: " + (1000 - countButtonClicks));
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            NavHostFragment.findNavController(PlayAnimationFragment.this)
//                                    .navigate(R.id.action_ThirdFragment_to_winningFragment);
//                        }
//                    }, 3000);
//                }
            }
        });

        binding.downButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Down Button has been hit", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
                countButtonClicks++;
                walk(-1);
                if (isOutside(px,py)){
                    driver.stopHandler();
                    Bundle result = new Bundle();
                    result.putFloat("batteryUsed", robot.getBatteryLevel());
                    result.putInt("distanceTraveled", robot.getOdometerReading());
                    getParentFragmentManager().setFragmentResult("forWinScreenManual", result);
                    NavHostFragment.findNavController(PlayAnimationFragment.this)
                            .navigate(R.id.action_playAnimationFragment_to_winningFragment);
                }
//                clicks.setText("Clicks to Win: " + (1000 - countButtonClicks));
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            NavHostFragment.findNavController(PlayAnimationFragment.this)
//                                    .navigate(R.id.action_ThirdFragment_to_winningFragment);
//                        }
//                    }, 3000);
//                }
            }
        });

        binding.leftButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Left Button has been hit", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
                countButtonClicks++;
//                rotate(1);
                rotate(1);
//                clicks.setText("Clicks to Win: " + (1000 - countButtonClicks));
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            NavHostFragment.findNavController(PlayAnimationFragment.this)
//                                    .navigate(R.id.action_ThirdFragment_to_winningFragment);
//                        }
//                    }, 3000);
//                }
            }
        });

        binding.rightButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Right Button has been hit", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
                countButtonClicks++;
                rotate(-1);
//                clicks.setText("Clicks to Win: " + (1000 - countButtonClicks));
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            NavHostFragment.findNavController(PlayAnimationFragment.this)
//                                    .navigate(R.id.action_ThirdFragment_to_winningFragment);
//                        }
//                    }, 3000);
//                }
            }
        });

        binding.go2winning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(PlayAnimationFragment.this).navigate(R.id.action_playAnimationFragment_to_winningFragment);
            }
        });

        binding.go2losing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(PlayAnimationFragment.this).navigate(R.id.action_playAnimationFragment_to_losingFragment);
            }
        });

        binding.toggleButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Snackbar.make(getView(), "MAP IS ON", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    mapMode = true;
                } else {
                    // The toggle is disabled
                    Snackbar.make(getView(), "MAP IS OFF", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    mapMode = false;
                }
                draw();
            }
        });

        binding.wallToggle3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    showMaze = true;
                } else{
                    showMaze = false;
                }
                draw();
            }
        });

        binding.solutionSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    showSolution = true;
                } else{
                    showSolution = false;
                }
                draw();
            }
        });

        SeekBar seekBar = getView().findViewById(R.id.speedBar);
        if (seekBar != null){
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // Write code to perform some action when progress is changed.
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Write code to perform some action when touch is started.
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Write code to perform some action when touch is stopped.
                    Toast.makeText(getActivity(), "Current value is " + (seekBar.getProgress() + 1), Toast.LENGTH_SHORT).show();
                    driver.setSpeed(10 - seekBar.getProgress());
                }
            });
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Toast.makeText(getActivity(), "Back press", Toast.LENGTH_SHORT).show();
                    driver.stopHandler();
                    NavHostFragment.findNavController(PlayAnimationFragment.this).navigate(R.id.action_playAnimationFragment_to_FirstFragment);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.upButton3).setVisibility(View.GONE);
        getView().findViewById(R.id.downButton3).setVisibility(View.GONE);
        getView().findViewById(R.id.leftButton3).setVisibility(View.GONE);
        getView().findViewById(R.id.rightButton3).setVisibility(View.GONE);

        getView().findViewById(R.id.rightSensor).bringToFront();
        getView().findViewById(R.id.leftSensor).bringToFront();
        getView().findViewById(R.id.backSensor).bringToFront();
        getView().findViewById(R.id.sensorForward).bringToFront();

        bat = getView().findViewById(R.id.battery);

        mazeConfig = MazeHolder.getMaze();
//        minDistance = mazeConfig.getDistanceToExit(mazeConfig.getStartingPosition()[0], mazeConfig.getStartingPosition()[1]);
        distanceTraveled = 0;
        showMaze = false ;
        showSolution = false ;
        mapMode = false;
        // init data structure for visible walls
        seenCells = new Floorplan(mazeConfig.getWidth()+1,mazeConfig.getHeight()+1) ;
        // set the current position and direction consistently with the viewing direction
        setPositionDirectionViewingDirection();
        walkStep = 0; // counts incremental steps during move/rotate operation

        // configure compass rose
        cr = new CompassRose();
        cr.setPositionAndSize(Constants.VIEW_WIDTH/2,
                (int)(0.1*Constants.VIEW_HEIGHT),35);


        if (panel != null) {
            startDrawer();
        }
        else {
            // else: dry-run without graphics, most likely for testing purposes
            printWarning();
        }

        sensors = RobotHolder.getDataSensors();

        if (sensors == "1111"){
            robot = new ReliableRobot(this, sensors);
        }
        else{
            robot = new UnreliableRobot(this, sensors);
        }

        driver = RobotHolder.getDataDriver();

        driver.setRobot(robot);
        driver.setMaze(mazeConfig);

        driver.setController(this);
        bat.setText("BATTERY LEVEL: " + robot.getBatteryLevel());

        try{
            driver.drive2Exit();
        } catch(AssertionError e){
            if (driver.getClass().getName().equals("WallFollower")){
                robot.stopFailureAndRepairProcess(Robot.Direction.FORWARD);
                robot.stopFailureAndRepairProcess(Robot.Direction.RIGHT);
                robot.stopFailureAndRepairProcess(Robot.Direction.LEFT);
                robot.stopFailureAndRepairProcess(Robot.Direction.BACKWARD);
            }
            robot.resetOdometer();
            robot.setBatteryLevel(3600);
            NavHostFragment.findNavController(PlayAnimationFragment.this).navigate(R.id.action_playAnimationFragment_to_losingFragment);
        } catch(Exception e){
            if (driver.getClass().getName().equals("WallFollower")){
                robot.stopFailureAndRepairProcess(Robot.Direction.FORWARD);
                robot.stopFailureAndRepairProcess(Robot.Direction.RIGHT);
                robot.stopFailureAndRepairProcess(Robot.Direction.LEFT);
                robot.stopFailureAndRepairProcess(Robot.Direction.BACKWARD);
            }
            robot.resetOdometer();
            robot.setBatteryLevel(3600);
            NavHostFragment.findNavController(PlayAnimationFragment.this).navigate(R.id.action_playAnimationFragment_to_losingFragment);

        }
    }

    /**
     * Internal method to set the current position, the direction
     * and the viewing direction to values consistent with the
     * given maze.
     */
    private void setPositionDirectionViewingDirection() {
        // obtain starting position
        int[] start = mazeConfig.getStartingPosition() ;
        setCurrentPosition(start[0],start[1]) ;
        // set current view direction and angle
        angle = 0; // angle matches with east direction,
        // hidden consistency constraint!
        setDirectionToMatchCurrentAngle();
        // initial direction is east, check this for sanity:
        assert(dx == 1);
        assert(dy == 0);
    }

    /**
     * Initializes the drawer for the first person view
     * and the map view and then draws the initial screen
     * for this state.
     */
    protected void startDrawer() {
        firstPersonView = new FirstPersonView(Constants.VIEW_WIDTH,
                Constants.VIEW_HEIGHT, Constants.MAP_UNIT,
                Constants.STEP_SIZE, seenCells, mazeConfig.getRootnode()) ;
        mapView = new Map(seenCells, 15, mazeConfig) ;
        // draw the initial screen for this state
        draw();
    }

    protected void draw() {
        if (panel == null) {
//            printWarning();
            return;
        }
        // draw the first person view and the map view if wanted
        firstPersonView.draw(panel, px, py, walkStep, angle,
                getPercentageForDistanceToExit()) ;
        if (isInMapMode()) {
            mapView.draw(panel, px, py, angle, walkStep,
                    isInShowMazeMode(),isInShowSolutionMode()) ;
        }
        // update the screen with the buffer graphics
        panel.commit();
    }

    float getPercentageForDistanceToExit() {
        return mazeConfig.getDistanceToExit(px, py) /
                ((float) mazeConfig.getMazedists().getMaxDistance());
    }
    /**
     * Prints the warning about a missing panel only once
     */
    boolean printedWarning = false;
    protected void printWarning() {
        if (printedWarning)
            return;
        System.out.println("StatePlaying.start: warning: no panel, dry-run game without graphics!");
        printedWarning = true;
    }
    ////////////////////////////// set methods ///////////////////////////////////////////////////////////////
    ////////////////////////////// Actions that can be performed on the maze model ///////////////////////////
    protected void setCurrentPosition(int x, int y) {
        px = x ;
        py = y ;
    }
    private void setCurrentDirection(int x, int y) {
        dx = x ;
        dy = y ;
    }
    /**
     * Sets fields dx and dy to be consistent with
     * current setting of field angle.
     */
    private void setDirectionToMatchCurrentAngle() {
        setCurrentDirection((int) Math.cos(radify(angle)), (int) Math.sin(radify(angle))) ;
    }

    ////////////////////////////// get methods ///////////////////////////////////////////////////////////////
    public int[] getCurrentPosition() {
        int[] result = new int[2];
        result[0] = px;
        result[1] = py;
        return result;
    }
    public CardinalDirection getCurrentDirection() {
        return CardinalDirection.getDirection(dx, dy);
    }
    boolean isInMapMode() {
        return mapMode ;
    }
    boolean isInShowMazeMode() {
        return showMaze ;
    }
    boolean isInShowSolutionMode() {
        return showSolution ;
    }
    public Maze getMazeConfiguration() {
        return mazeConfig ;
    }
    //////////////////////// Methods for move and rotate operations ///////////////
    final double radify(int x) {
        return x*Math.PI/180;
    }
    /**
     * Helper method for walk()
     * @param dir is the direction of interest
     * @return true if there is no wall in this direction
     */
    protected boolean checkMove(int dir) {
        CardinalDirection cd = null;
        switch (dir) {
            case 1: // forward
                cd = getCurrentDirection();
                break;
            case -1: // backward
                cd = getCurrentDirection().oppositeDirection();
                break;
            default:
                throw new RuntimeException("Unexpected direction value: " + dir);
        }
        return !mazeConfig.hasWall(px, py, cd);
    }
    /**
     * Draws and waits. Used to obtain a smooth appearance for rotate and move operations
     */
    private void slowedDownRedraw() {
        draw() ;
        try {
            Thread.sleep(25);
        } catch (Exception e) {
            // may happen if thread is interrupted
            // no reason to do anything about it, ignore exception
        }
    }

    /**
     * Performs a rotation with 4 intermediate views,
     * updates the screen and the internal direction
     * @param dir for current direction, values are either 1 or -1
     */
    private synchronized void rotate(int dir) {
        final int originalAngle = angle;
        final int steps = 4;

        for (int i = 0; i != steps; i++) {
            // add 1/4 of 90 degrees per step
            // if dir is -1 then subtract instead of addition
            angle = originalAngle + dir*(90*(i+1))/steps;
            angle = (angle+1800) % 360;
            // draw method is called and uses angle field for direction
            // information.
            slowedDownRedraw();
        }
        // update maze direction only after intermediate steps are done
        // because choice of direction values are more limited.
        setDirectionToMatchCurrentAngle();
        //logPosition(); // debugging
        drawHintIfNecessary();
    }

    /**
     * Moves in the given direction with 4 intermediate steps,
     * updates the screen and the internal position
     * @param dir, only possible values are 1 (forward) and -1 (backward)
     */
    private synchronized void walk(int dir) {
        // check if there is a wall in the way
        if (!checkMove(dir))
            return;
        // walkStep is a parameter of FirstPersonDrawer.draw()
        // it is used there for scaling steps
        // so walkStep is implicitly used in slowedDownRedraw
        // which triggers the draw operation in
        // FirstPersonDrawer and MapDrawer
        for (int step = 0; step != 4; step++) {
            walkStep += dir;
            slowedDownRedraw();
        }
        setCurrentPosition(px + dir*dx, py + dir*dy) ;
        walkStep = 0; // reset counter for next time
        //logPosition(); // debugging
        drawHintIfNecessary();
    }

    /**
     * Checks if the given position is outside the maze
     * @param x coordinate of position
     * @param y coordinate of position
     * @return true if position is outside, false otherwise
     */
    private boolean isOutside(int x, int y) {
        return !mazeConfig.isValidPosition(x, y) ;
    }

    private void drawHintIfNecessary() {
        if (isInMapMode())
            return; // no need for help
        // in testing environments, there is sometimes no panel to draw on
        // or the panel is unable to deliver a graphics object
        // check this and quietly move on if drawing is impossible
        if (panel == null) {
            printWarning();
            return;
        }
        // if current position faces a dead end, show map with solution
        // for guidance
        if (isFacingDeadEnd()) {
            //System.out.println("Facing deadend, help by showing solution");
            mapView.draw(panel, px, py, angle, walkStep, true, true) ;
        }
        else {
            // draw compass rose
            cr.setCurrentDirection(getCurrentDirection());
            cr.paintComponent(panel);
        }
        panel.commit();
    }

    /**
     * Checks if the current position and direction
     * faces a dead end
     * @return true if at the current position there is
     * a wall to the left, right and front, false otherwise
     */
    private boolean isFacingDeadEnd() {
        return (!isOutside(px,py) &&
                mazeConfig.hasWall(px, py, getCurrentDirection()) &&
                mazeConfig.hasWall(px, py, getCurrentDirection().oppositeDirection().rotateClockwise()) &&
                mazeConfig.hasWall(px, py, getCurrentDirection().rotateClockwise()));
    }

    /**
     * Sets the robot and robot driver
     * @param robot the robot that is used for the automated playing mode
     * @param robotdriver the driver that is used for the automated playing mode
     */
    public void setRobotAndDriver(Robot robot, RobotDriver robotdriver) {
        this.robot = robot;
        driver = robotdriver;

    }
    /**
     * The robot that is used in the automated playing mode.
     * Null if run in the manual mode.
     * @return the robot, may be null
     */
    public Robot getRobot() {
        return robot;
    }
    /**
     * The robot driver that is used in the automated playing mode.
     * Null if run in the manual mode.
     * @return the driver, may be null
     */
    public RobotDriver getDriver() {
        return driver;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}