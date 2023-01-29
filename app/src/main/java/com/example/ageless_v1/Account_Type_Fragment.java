package com.example.ageless_v1;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Account_Type_Fragment extends Fragment {

    CardView userbox;
    CardView contactbox;
    ProgressBar progressBar;
    int CurrentProgress;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid;
    UserInfo userInfo = new UserInfo();


    String user_type;
    String account_type;

    DateOfBirth dateOfBirth = new DateOfBirth();

    private void add_to_database(){
        userInfo.setAccount_type(account_type);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(userInfo);
//                Toast.makeText(getActivity(), "User set", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getActivity(), "User not set. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

        FirebaseAuth firebaseAuth;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        uid = user.getUid();
        databaseReference = firebaseDatabase.getReference("UserInfo").child(uid);



        try {
            userbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    account_type = "Primary User";
                    add_to_database();
//                    Fragment fragment = new DateOfBirth();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container_view, fragment)
//                            .commit();
//                    progressBar.setProgress(CurrentProgress);
                }


            });
        }
        catch (Exception e){
            Log.e("Getdata", e.getMessage(), e);
        }

        return viewGroup;
    }

}