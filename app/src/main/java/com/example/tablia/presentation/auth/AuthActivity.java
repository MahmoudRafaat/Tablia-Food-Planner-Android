package com.example.tablia.presentation.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tablia.R;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            String destination = getIntent().getStringExtra("destination");
            if ("login".equals(destination)) {
                navController.navigate(R.id.loginFragment, null, new NavOptions.Builder()
                        .setPopUpTo(R.id.onboardingFragment, true)
                        .build());
            }
        }
    }
}
