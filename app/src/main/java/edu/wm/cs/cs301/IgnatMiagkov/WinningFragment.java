package edu.wm.cs.cs301.IgnatMiagkov;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

        getParentFragmentManager().setFragmentResultListener("forWinScreenManual", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                int minDistance = bundle.getInt("minDistance");
                int distanceTraveled = bundle.getInt("distanceTraveled");
//                Snackbar.make(getView(), "DIFF IS" + diff, Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
                TextView textView1 = getView().findViewById(R.id.minDistance);
                textView1.setText("Minimum Distance to Exit: " + minDistance);
                TextView textView2 = getView().findViewById(R.id.disTraveled);
                textView2.setText("Distance Traveled: " + distanceTraveled);
//                textView.setText(builder);
//                TextView textView1 = getView().findViewById(R.id.textView4);
//                textView1.setText(diff.toString());
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(WinningFragment.this)
                        .navigate(R.id.action_winningFragment_to_FirstFragment);
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
                    NavHostFragment.findNavController(WinningFragment.this).navigate(R.id.action_winningFragment_to_FirstFragment);
                    return true;
                }
                return false;
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}