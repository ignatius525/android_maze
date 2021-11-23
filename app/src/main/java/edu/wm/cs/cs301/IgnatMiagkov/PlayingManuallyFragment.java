package edu.wm.cs.cs301.IgnatMiagkov;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentGeneratingBinding;
import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentPlayingManuallyBinding;

public class PlayingManuallyFragment extends Fragment {

    private TextView clicks;
    private FragmentPlayingManuallyBinding binding;
    private int countButtonClicks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPlayingManuallyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clicks = getView().findViewById(R.id.clicks);
        //Button upButton = getView().findViewById(R.id.upButton);
        TextView win = getView().findViewById(R.id.winning);
        win.setVisibility(View.GONE);

        binding.upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Up Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
                clicks.setText("Clicks to Win: " + (10 - countButtonClicks));
                if (countButtonClicks == 10){
                    clicks.setVisibility(View.GONE);
                    win.setVisibility(View.VISIBLE);
                    binding.upButton.setVisibility(View.GONE);
                    binding.downButton.setVisibility(View.GONE);
                    binding.leftButton.setVisibility(View.GONE);
                    binding.rightButton.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            NavHostFragment.findNavController(PlayingManuallyFragment.this)
                                    .navigate(R.id.action_ThirdFragment_to_winningFragment);
                        }
                    }, 3000);
                }
            }
        });

        binding.downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Down Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
                clicks.setText("Clicks to Win: " + (10 - countButtonClicks));
                if (countButtonClicks == 10){
                    clicks.setVisibility(View.GONE);
                    win.setVisibility(View.VISIBLE);
                    binding.upButton.setVisibility(View.GONE);
                    binding.downButton.setVisibility(View.GONE);
                    binding.leftButton.setVisibility(View.GONE);
                    binding.rightButton.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            NavHostFragment.findNavController(PlayingManuallyFragment.this)
                                    .navigate(R.id.action_ThirdFragment_to_winningFragment);
                        }
                    }, 3000);
                }
            }
        });

        binding.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Left Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
                clicks.setText("Clicks to Win: " + (10 - countButtonClicks));
                if (countButtonClicks == 10){
                    clicks.setVisibility(View.GONE);
                    win.setVisibility(View.VISIBLE);
                    binding.upButton.setVisibility(View.GONE);
                    binding.downButton.setVisibility(View.GONE);
                    binding.leftButton.setVisibility(View.GONE);
                    binding.rightButton.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            NavHostFragment.findNavController(PlayingManuallyFragment.this)
                                    .navigate(R.id.action_ThirdFragment_to_winningFragment);
                        }
                    }, 3000);
                }
            }
        });

        binding.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Right Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
                clicks.setText("Clicks to Win: " + (10 - countButtonClicks));
                if (countButtonClicks == 10){
                    clicks.setVisibility(View.GONE);
                    win.setVisibility(View.VISIBLE);
                    binding.upButton.setVisibility(View.GONE);
                    binding.downButton.setVisibility(View.GONE);
                    binding.leftButton.setVisibility(View.GONE);
                    binding.rightButton.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            NavHostFragment.findNavController(PlayingManuallyFragment.this)
                                    .navigate(R.id.action_ThirdFragment_to_winningFragment);
                        }
                    }, 3000);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}