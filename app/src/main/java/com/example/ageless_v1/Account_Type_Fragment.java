package com.example.ageless_v1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class Account_Type_Fragment extends Fragment {

    CardView userbox;
    CardView contactbox;
    ProgressBar progressBar;
    int CurrentProgress;


//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference databaseReference;
//    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    private String uid;
//    private UserInfo userInfo = new UserInfo();

    SharedPreferences sharedPreferences;



    public Account_Type_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_account_type, container, false);
        userbox = viewGroup.findViewById(R.id.userbox);
        contactbox = viewGroup.findViewById(R.id.contactbox);
        progressBar = getActivity().findViewById(R.id.progressBar);
        CurrentProgress = 20;

//        FirebaseAuth firebaseAuth;
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        uid = user.getUid();

        sharedPreferences  = this.getActivity().getSharedPreferences("Ageless", MODE_PRIVATE);
        SharedPreferences.Editor add_data = sharedPreferences.edit();



        userbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user_type = "Basic User";
                    add_data.putString("account_type", user_type);
                    add_data.apply();

                    Fragment fragment = new DateOfBirth();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_view, fragment)
                            .commit();
                    progressBar.setProgress(CurrentProgress);

                }


            });

        contactbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "This feature is unavailable", Toast.LENGTH_SHORT).show();
            }
        });


        return viewGroup;
    }

//    private void update_data(String account_type){
//        databaseReference = firebaseDatabase.getReference("UserInfo").child(uid);
//        databaseReference.setValue(account_type).addOnCompleteListener(new OnCompleteListener() {
//            @Override
//            public void onComplete(@NonNull Task task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(getActivity(), "Account type set", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getActivity(), "Account type not set", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

}