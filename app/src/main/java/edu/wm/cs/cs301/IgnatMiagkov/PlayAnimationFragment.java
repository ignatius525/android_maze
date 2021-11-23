package edu.wm.cs.cs301.IgnatMiagkov;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentPlayAnimationBinding;
import edu.wm.cs.cs301.IgnatMiagkov.databinding.FragmentPlayingManuallyBinding;


public class PlayAnimationFragment extends Fragment {

    private TextView clicks;
    private FragmentPlayAnimationBinding binding;
    private int countButtonClicks;

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

        binding.upButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Up Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
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
                Snackbar.make(v, "Down Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
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
                Snackbar.make(v, "Left Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
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
                Snackbar.make(v, "Right Button has been hit", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                countButtonClicks++;
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}