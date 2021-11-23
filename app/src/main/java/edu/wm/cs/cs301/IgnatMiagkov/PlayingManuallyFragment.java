package edu.wm.cs.cs301.IgnatMiagkov;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentGeneratingBinding;
import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentPlayingManuallyBinding;

public class PlayingManuallyFragment extends Fragment {

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

        //Button upButton = getView().findViewById(R.id.upButton);

        binding.upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Up Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
            }
        });

        binding.downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Down Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
            }
        });

        binding.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Left Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
            }
        });

        binding.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Right Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}