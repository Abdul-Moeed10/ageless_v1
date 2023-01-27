package com.example.ageless_v1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


public class weight extends Fragment {

    ProgressBar progressBar;
    Button proceed_button, lbs_button, kg_button;
    int CurrentProgress;
    EditText weight;


    public weight() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_gender, container, false);
        progressBar = getActivity().findViewById(R.id.progressBar);
        CurrentProgress = 50;
        proceed_button = viewGroup.findViewById(R.id.proceed_button);
        lbs_button = viewGroup.findViewById(R.id.lbs_button);
        kg_button = viewGroup.findViewById(R.id.kg_button);
        weight = viewGroup.findViewById(R.id.weight);

        lbs_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lbsweight = weight.getText().toString();
                double lbs_weight = Double.parseDouble(lbsweight);
                lbs_weight = lbs_weight*0.45;
                weight.setText(lbsweight.toString());
            }
        });

        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new height();
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