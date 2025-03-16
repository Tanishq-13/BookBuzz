package com.example.myapplication.launch_page;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.launch_page.fragmentss.UploadFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchpage);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Load LaunchPageFragment by default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_cont, new launch_page())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.slideshare) {
                selectedFragment = new UploadFragment();
            }
            if (item.getItemId() == R.id.home) {
                selectedFragment = new launch_page();
            }
//            // Add other fragments for different tabs if needed
//
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_cont, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}
