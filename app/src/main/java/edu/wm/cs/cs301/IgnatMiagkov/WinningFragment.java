package edu.wm.cs.cs301.IgnatMiagkov;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentGeneratingBinding;
import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentWinningBinding;


public class WinningFragment extends Fragment {
    private FragmentWinningBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWinningBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}