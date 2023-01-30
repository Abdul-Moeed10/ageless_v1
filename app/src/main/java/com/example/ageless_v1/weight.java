package com.example.ageless_v1;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.w3c.dom.Text;


public class weight extends Fragment {

    ProgressBar progressBar;
    int CurrentProgress;
    Button proceed_button;


    EditText kgs;

    SharedPreferences sharedPreferences;


    public weight() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_weight, container, false);
        proceed_button = viewGroup.findViewById(R.id.proceed_button);
        progressBar = getActivity().findViewById(R.id.progressBar);
        kgs = viewGroup.findViewById(R.id.kg_weight);
        CurrentProgress = 50;

        sharedPreferences  = this.getActivity().getSharedPreferences("Ageless", MODE_PRIVATE);
        SharedPreferences.Editor add_data = sharedPreferences.edit();


        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = kgs.getText().toString();
                add_data.putString("weight", weight);
                add_data.apply();
                String temp = kgs.getText().toString();
                if(!"".equals(temp)) {
                    Fragment fragment = new height();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_view, fragment)
                            .commit();
                }
                else{
                    Toast.makeText(getActivity(), "Please enter your weight", Toast.LENGTH_SHORT).show();
                }
                progressBar.setProgress(CurrentProgress);
            }
        });
        return viewGroup;
    }
}