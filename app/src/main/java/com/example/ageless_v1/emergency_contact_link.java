package com.example.ageless_v1;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class emergency_contact_link extends Fragment {

    EditText emergency_phoneno;
    Button proceed_button;

    ProgressBar progressBar;
    int CurrentProgress;

    SharedPreferences sharedPreferences;

    public emergency_contact_link() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_emergency_contact_link, container, false);
        emergency_phoneno = viewGroup.findViewById(R.id.etPhoneNumber);
        proceed_button = viewGroup.findViewById(R.id.proceed_button);
        CurrentProgress = 80;
        progressBar = getActivity().findViewById(R.id.progressBar);

        sharedPreferences  = this.getActivity().getSharedPreferences("Ageless", MODE_PRIVATE);
        SharedPreferences.Editor add_data = sharedPreferences.edit();

        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emergency_no = emergency_phoneno.getText().toString();
                add_data.putString("emergency_no", emergency_no);
                add_data.apply();

                if(!"".equals(emergency_no)){
                    Fragment fragment = new medical_info();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_view, fragment)
                            .commit();
                    progressBar.setProgress(CurrentProgress);
                }
                else{
                    Toast.makeText(getActivity(), "Please enter emergency contact's phone number.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return viewGroup;
    }
}