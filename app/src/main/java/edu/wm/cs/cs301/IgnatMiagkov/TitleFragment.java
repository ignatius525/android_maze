package edu.wm.cs.cs301.IgnatMiagkov;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentTitleBinding;

public class TitleFragment extends Fragment {

    private FragmentTitleBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentTitleBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SeekBar seekBar = getView().findViewById(R.id.seekBar);
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
                    Toast.makeText(getActivity(), "Current value is " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(TitleFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}