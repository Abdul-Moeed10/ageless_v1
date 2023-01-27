package com.example.ageless_v1;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


public class Gender_Fragment extends Fragment {

    ProgressBar progressBar;
    int CurrentProgress;

    CardView malebox;
    CardView femalebox;

    public Gender_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_gender, container, false);
        progressBar = getActivity().findViewById(R.id.progressBar);
        malebox = viewGroup.findViewById(R.id.malebox);
        femalebox = viewGroup.findViewById(R.id.femalebox);
        CurrentProgress = 40;

        malebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new weight();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_view, fragment)
                        .commit();
                progressBar.setProgress(CurrentProgress);
            }
        });
        femalebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new weight();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_view, fragment)
                        .commit();
                progressBar.setProgress(CurrentProgress);
            }
        });
        return viewGroup;
    }
}