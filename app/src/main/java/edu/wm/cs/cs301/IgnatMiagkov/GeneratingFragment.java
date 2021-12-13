package edu.wm.cs.cs301.IgnatMiagkov;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.nio.channels.AsynchronousByteChannel;
import java.util.Random;

import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentGeneratingBinding;

import edu.wm.cs.cs301.IgnatMiagkov.generation.Factory;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Floorplan;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Maze;
import edu.wm.cs.cs301.IgnatMiagkov.generation.MazeFactory;
import edu.wm.cs.cs301.IgnatMiagkov.generation.Order;
import edu.wm.cs.cs301.IgnatMiagkov.MazeHolder;
import edu.wm.cs.cs301.IgnatMiagkov.OrderHolder;
import edu.wm.cs.cs301.IgnatMiagkov.RobotHolder;
import edu.wm.cs.cs301.IgnatMiagkov.gui.ReliableRobot;
import edu.wm.cs.cs301.IgnatMiagkov.gui.WallFollower;
import edu.wm.cs.cs301.IgnatMiagkov.gui.Wizard;

public class GeneratingFragment extends Fragment implements Order{

    private String selectedBuilder;
    private ProgressBar progressBar;
    private String selectedDriver;
    private String selectedRobotConfig;
    private Button begin_button;
    private FragmentGeneratingBinding binding;
    private int diff;
    private long time;
    private Handler handler = new Handler();
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private String filename;
    private int seed;
    private int skillLevel;
    private Builder builder;
    private boolean perfect;

    protected Factory factory;
    private int percentDone;

    boolean started;


    AsyncTask task;
    Integer count = 1;

    public GeneratingFragment(){
        filename = null;
        factory = new MazeFactory();
        skillLevel = 0;
        builder = Builder.DFS;
        perfect = false;
        percentDone = 0;
        started = false;
        seed = 13;


    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentGeneratingBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                selectedBuilder = bundle.getString("builderKey");
                diff = bundle.getInt("difficultyKey");
//                Snackbar.make(getView(), "DIFF IS" + diff, Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
//                TextView textView = getView().findViewById(R.id.textView3);
//                textView.setText(builder);
//                TextView textView1 = getView().findViewById(R.id.textView4);
//                textView1.setText(diff.toString());
            }
        });
//        Snackbar.make(getView(), "DIFF IS" + diff, Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
//        this.skillLevel = diff;
        Spinner spinner2 = getView().findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.driver, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter);
        if (spinner2 != null){
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    // this line will get you the selected item of the spinner
                    selectedDriver = parent.getItemAtPosition(position).toString();
                    switch(selectedDriver){
                        case "Wizard":
                            RobotHolder.setDataDriver(new Wizard());
                            break;
                        case "WallFollower":
                            RobotHolder.setDataDriver(new WallFollower());
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });
        }

        Spinner spinner3 = getView().findViewById(R.id.spinner3);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity(), R.array.robot, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner3.setAdapter(adapter3);
        if (spinner3 != null){
            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    // this line will get you the selected item of the spinner
                    selectedRobotConfig = parent.getItemAtPosition(position).toString();
                    switch(selectedRobotConfig){
                        case "Premium":
                            RobotHolder.setDataSensors("1111");
                            break;
                        case "Mediocre":
                            RobotHolder.setDataSensors("1001");
                            break;
                        case "So-So":
                            RobotHolder.setDataSensors("0110");
                            break;
                        case "Shaky":
                            RobotHolder.setDataSensors("0000");
                            break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });
        }
        begin_button = getView().findViewById(R.id.button_begin);
        begin_button.setVisibility(View.GONE);
        progressBar = getView().findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(GeneratingFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.buttonBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "DRIVER = " + selectedDriver, Toast.LENGTH_SHORT).show();
                if (selectedDriver.equals("Manual")){
                    NavHostFragment.findNavController(GeneratingFragment.this)
                            .navigate(R.id.action_SecondFragment_to_ThirdFragment);
                }
                else{
                    NavHostFragment.findNavController(GeneratingFragment.this)
                            .navigate(R.id.action_SecondFragment_to_playAnimationFragment);
                }
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Toast.makeText(getActivity(), "Back press", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(GeneratingFragment.this).navigate(R.id.action_SecondFragment_to_FirstFragment);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        skillLevel = OrderHolder.getSkillLevel();
        builder = OrderHolder.getBuilder();
        Random rnd = new Random();
        this.seed = rnd.nextInt();
        if (OrderHolder.getRevisit() == true){
            this.seed = sharedPref.getInt(builder.toString() + skillLevel, getSeed());
        }

        Snackbar.make(getView(),"KEY " + builder.toString() + skillLevel, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

        editor.putInt(builder.toString() + skillLevel, this.seed);
        editor.apply();
//        if (selectedBuilder != null) {
//            switch (selectedBuilder) {
//                case "Prim":
//                    this.builder = Builder.Prim;
//                    break;
//                case "Boruvka":
//                    this.builder = Builder.Boruvka;
//                    break;
//                default:
//                    this.builder = Builder.DFS;
//                    break;
//            }
//        }
        factory.order(this);
        task = new MyTask(this).execute(100);

//

    }

    @Override
    public void onStop(){
        super.onStop();
        task.cancel(true);
    }

    @Override
    public void onDestroyView() {
        task.cancel(true);
        count = 1;
        super.onDestroyView();
        binding = null;
    }

    @Override
    public int getSkillLevel() {
        return skillLevel;
    }

    @Override
    public Builder getBuilder() {
        return builder;
    }

    @Override
    public boolean isPerfect() {
        return this.perfect;
    }

    @Override
    public int getSeed() {
        return this.seed;
    }

    @Override
    public void deliver(Maze mazeConfig) {
        if (Floorplan.deepdebugWall)
        {   // for debugging: dump the sequence of all deleted walls to a log file
            // This reveals how the maze was generated
            mazeConfig.getFloorplan().saveLogFile(Floorplan.deepedebugWallFileName);
        }
        MazeHolder.setMaze(mazeConfig);

//        ft.replace(android.R.id.content, fragment2);
//        ft.addToBackStack(null);
//        ft.commit();

    }

    public int getPercentDone(){
        return this.percentDone;
    }

    @Override
    public void updateProgress(int percentage) {
        if (this.percentDone < percentage && percentage <= 100) {
            this.percentDone = percentage;
//            progressBar.setProgress(percentDone);
        }
    }

    // Code refactored from https://www.concretepage.com/android/android-asynctask-example-with-progress-bar for async thread
    class MyTask extends AsyncTask<Integer, Integer, String> {

        private Order order;

        public MyTask(Order order){
            this.order = order;
        }
        @Override
        protected String doInBackground(Integer... params) {
            while(progressBar.getProgress() < progressBar.getMax()) {
//                progressBar.setProgress(order.getPercentDone() / 100);
                try {
                    publishProgress(order.getPercentDone());
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            begin_button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            count = 1;
            this.cancel(true);
            Snackbar.make(getView(), "Don't forget to select a driver!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
    }

}