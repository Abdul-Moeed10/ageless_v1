package com.example.ageless_v1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class medical_info extends Fragment {

    EditText medical;
    Button proceed_button;

    ProgressBar progressBar;
    int CurrentProgress;

    SharedPreferences sharedPreferences;

    public medical_info() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_medical_info, container, false);
        proceed_button = viewGroup.findViewById(R.id.proceed_button);
        CurrentProgress = 90;
        progressBar = getActivity().findViewById(R.id.progressBar);
        medical = viewGroup.findViewById(R.id.medical_info);

        sharedPreferences  = this.getActivity().getSharedPreferences("Ageless", MODE_PRIVATE);
        SharedPreferences.Editor add_data = sharedPreferences.edit();

        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medical_info = medical.getText().toString();
                add_data.putString("medical_info", medical_info);
                add_data.apply();


                if(!"".equals(medical_info)){
//                    Fragment fragment = new blood_group();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container_view, fragment)
//                            .commit();

                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                    progressBar.setProgress(CurrentProgress);
                }
                else{
                    Toast.makeText(getActivity(), "Please enter your medical information or enter none.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return viewGroup;
    }
}