package com.example.tablia.presentation.auth;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tablia.R;
import com.example.tablia.presentation.onboarding.view.OnboardingFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (savedInstanceState == null) {
            SharedPreferences prefs = getSharedPreferences("TabliaPrefs", MODE_PRIVATE);
            boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

            Fragment initialFragment = isFirstRun ? new OnboardingFragment() : new OnboardingFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.authFragmentContainer, initialFragment)
                    .commit();
        }
    }
}