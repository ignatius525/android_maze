package edu.wm.cs.cs301.IgnatMiagkov;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import edu.wm.cs.cs301.IgnatMiagkov.databinding.ActivityAmazeBinding;

public class AMazeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAmazeBinding binding;
    Fragment currentFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAmazeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_amaze);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        FragmentManager fragmentManager = AMazeActivity.this.getSupportFragmentManager();
//        List<Fragment> fragments = fragmentManager.getFragments();
//        if(fragments != null){
//            for(Fragment fragment : fragments){
//                if(fragment != null && fragment.isVisible())
//                    currentFrag = fragment;
//            }
//        }
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                if (currentFrag != null){
//                    switch (currentFrag.getId()){
//                        case(2):
//                            NavHostFragment.findNavController(currentFrag)
//                                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
//                    }
//                }
//                NavHostFragment.findNavController(currentFrag)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_amaze);

//        navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    @Override
//    public void onBackPressed(){
//        FragmentManager fragmentManager = AMazeActivity.this.getSupportFragmentManager();
//        List<Fragment> fragments = fragmentManager.getFragments();
//        if(fragments != null){
//            for(Fragment fragment : fragments){
//                if(fragment != null && fragment.isVisible())
//                    currentFrag = fragment;
//            }
//        }
//
//        NavController navController = Navigation.findNavController(this, currentFrag.getId());
//        switch(currentFrag.getId()){
//            case R.id.SecondFragment:
//                navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
//                break;
//            case R.id.ThirdFragment:
//                navController.navigate(R.id.action_ThirdFragment_to_FirstFragment);
//                break;
//        }
//    }
}