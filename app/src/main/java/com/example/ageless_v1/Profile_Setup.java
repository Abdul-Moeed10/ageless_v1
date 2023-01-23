package com.example.ageless_v1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class Profile_Setup extends AppCompatActivity {


    private int CurrentProgress = 10;
    private ProgressBar progressBar;
    private Button next_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        progressBar = findViewById(R.id.progressBar);
        next_button = findViewById(R.id.next_button);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, Account_Type_Fragment.class, null)
                    .commit();
        }

        progressBar.setProgress(CurrentProgress);
//        next_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CurrentProgress = CurrentProgress + 10;
//                progressBar.setProgress(CurrentProgress);
//                progressBar.setMax(50);
//            }
//        });



    }
}
