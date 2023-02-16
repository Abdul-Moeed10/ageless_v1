package com.example.ageless_v1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class Profile_Setup extends AppCompatActivity {


    int CurrentProgress = 10;
    private ProgressBar progressBar;
    private Button next_button;

    private Account_Type_Fragment account_type_fragment = new Account_Type_Fragment();
    private DateOfBirth dateOfBirth = new DateOfBirth();
    private Gender_Fragment gender_fragment = new Gender_Fragment();

//    private FirebaseAuth firebaseAuth;
//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference databaseReference;
//    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    private String uid;
//    private UserInfo userInfo = new UserInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        uid = user.getUid();
//        databaseReference = firebaseDatabase.getReference("UserInfo").child(uid);


        progressBar = findViewById(R.id.progressBar);

        Account_Type_Fragment account_type_fragment = new Account_Type_Fragment();
        fragmentTransaction.replace(R.id.fragment_container_view, account_type_fragment)
                        .commit();

        progressBar.setProgress(CurrentProgress);

    }

//    public void update_data(String account_type){
//        userInfo.setAccount_type(account_type);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                databaseReference.setValue(userInfo);
//                databaseReference.updateChildren((Map<String, Object>) userInfo);
////                Toast.makeText(getActivity(), "User set", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
////                Toast.makeText(getActivity(), "User not set. Try again.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
