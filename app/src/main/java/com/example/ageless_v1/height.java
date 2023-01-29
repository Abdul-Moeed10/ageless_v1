package com.example.ageless_v1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class height extends Fragment {

    ProgressBar progressBar;
    int CurrentProgress;
    Button proceed_button;

    EditText ft, inch;



    public height() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_height, container, false);
        proceed_button = viewGroup.findViewById(R.id.proceed_button);
        progressBar = getActivity().findViewById(R.id.progressBar);
        CurrentProgress = 60;
        ft = viewGroup.findViewById(R.id.ft);
        inch = viewGroup.findViewById(R.id.inch);


        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp1 = ft.getText().toString();
                String temp2 = inch.getText().toString();
                if(!"".equals(temp1) && !"".equals(temp2)) {
                    Fragment fragment = new blood_group();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_view, fragment)
                            .commit();
                }
                else{
                    Toast.makeText(getActivity(), "Please enter an appropriate height", Toast.LENGTH_SHORT).show();
                    if(temp1=="0" && temp2=="0"){
                        Toast.makeText(getActivity(), "Please enter an appropriate height", Toast.LENGTH_SHORT).show();
                    }
                    else if("".equals(temp1)){
                        Toast.makeText(getActivity(), "Please enter a correct feet value or enter 0", Toast.LENGTH_SHORT).show();
                    }
                    else if("".equals(temp2)){
                        Toast.makeText(getActivity(), "Please enter a correct inches value or enter 0", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Please enter an appropriate height", Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setProgress(CurrentProgress);
            }
        });


        return viewGroup;
    }
}