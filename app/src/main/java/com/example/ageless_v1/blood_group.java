
package com.example.ageless_v1;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


public class blood_group extends Fragment {

    CardView A_plus, B_plus, O_plus, AB_plus;
    CardView A_minus, B_minus, O_minus, AB_minus;

    ProgressBar progressBar;
    int CurrentProgress;

    SharedPreferences sharedPreferences;

    public blood_group() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_blood_group, container, false);
        A_plus = viewGroup.findViewById(R.id.A_plus);
//        B_plus = viewGroup.findViewById(R.id.B_plus);
//        O_plus = viewGroup.findViewById(R.id.O_plus);
//        AB_plus = viewGroup.findViewById(R.id.AB_plus);
//        A_minus = viewGroup.findViewById(R.id.A_minus);
//        B_minus = viewGroup.findViewById(R.id.B_minus);
//        O_minus = viewGroup.findViewById(R.id.O_minus);
//        AB_minus = viewGroup.findViewById(R.id.AB_minus);
        CurrentProgress = 70;
        progressBar = getActivity().findViewById(R.id.progressBar);

        sharedPreferences  = this.getActivity().getSharedPreferences("Ageless", MODE_PRIVATE);
        SharedPreferences.Editor add_data = sharedPreferences.edit();

        A_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String blood_group = "A+";
                add_data.putString("blood_group", blood_group);
                add_data.apply();
                Fragment fragment = new emergency_contact_link();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_view, fragment)
                        .commit();
                progressBar.setProgress(CurrentProgress);
            }
        });

//        A_minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String blood_group = "A-";
//                add_data.putString("blood_group", blood_group);
//                add_data.apply();
//                Fragment fragment = new emergency_contact_link();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_view, fragment)
//                        .commit();
//                progressBar.setProgress(CurrentProgress);
//            }
//        });
//
//        B_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String blood_group = "B+";
//                add_data.putString("blood_group", blood_group);
//                add_data.apply();
//                Fragment fragment = new emergency_contact_link();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_view, fragment)
//                        .commit();
//                progressBar.setProgress(CurrentProgress);
//            }
//        });
//
//        B_minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String blood_group = "B-";
//                add_data.putString("blood_group", blood_group);
//                add_data.apply();
//                Fragment fragment = new emergency_contact_link();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_view, fragment)
//                        .commit();
//                progressBar.setProgress(CurrentProgress);
//            }
//        });
//
//        O_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String blood_group = "O+";
//                add_data.putString("blood_group", blood_group);
//                add_data.apply();
//                Fragment fragment = new emergency_contact_link();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_view, fragment)
//                        .commit();
//                progressBar.setProgress(CurrentProgress);
//            }
//        });
//
//        O_minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String blood_group = "O-";
//                add_data.putString("blood_group", blood_group);
//                add_data.apply();
//                Fragment fragment = new emergency_contact_link();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_view, fragment)
//                        .commit();
//                progressBar.setProgress(CurrentProgress);
//            }
//        });
//
//        AB_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String blood_group = "AB+";
//                add_data.putString("blood_group", blood_group);
//                add_data.apply();
//                Fragment fragment = new emergency_contact_link();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_view, fragment)
//                        .commit();
//                progressBar.setProgress(CurrentProgress);
//            }
//        });
//
//        AB_minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String blood_group = "AB-";
//                add_data.putString("blood_group", blood_group);
//                add_data.apply();
//                Fragment fragment = new emergency_contact_link();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_view, fragment)
//                        .commit();
//                progressBar.setProgress(CurrentProgress);
//            }
//        });



        return viewGroup;
    }
}