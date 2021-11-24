package edu.wm.cs.cs301.IgnatMiagkov;

import android.os.AsyncTask;
import android.os.Bundle;
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
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.nio.channels.AsynchronousByteChannel;

import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentGeneratingBinding;

public class GeneratingFragment extends Fragment {

    private ProgressBar progressBar;
    private String selectedDriver;
    private String selectedRobotConfig;
    private Button begin_button;
    private FragmentGeneratingBinding binding;
    private String builder;
    private Integer diff;
    private long time;
    AsyncTask task;
    Integer count = 1;

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

        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                builder = bundle.getString("builderKey");
                diff = bundle.getInt("difficultyKey");
                time = diff * 25;
//                TextView textView = getView().findViewById(R.id.textView3);
//                textView.setText(builder);
//                TextView textView1 = getView().findViewById(R.id.textView4);
//                textView1.setText(diff.toString());
            }
        });

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
        task = new MyTask().execute(100);
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
    public void onDestroyView() {
        task.cancel(true);
        count = 1;
        super.onDestroyView();
        binding = null;
    }
    // Code refactored from https://www.concretepage.com/android/android-asynctask-example-with-progress-bar for async thread
    class MyTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            for (; count <= params[0]; count++) {
                try {
                    Thread.sleep(time);
                    publishProgress(count);
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