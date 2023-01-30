package com.example.ageless_v1;

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



//        textWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                lbs.removeTextChangedListener(textWatcher);
//                String temp1 = kgs.getText().toString();
//                Double lbsweight = 0.0;
//                if(!"".equals(temp1)){
//                    lbsweight = Double.parseDouble(temp1);
//                    lbsweight = lbsweight*2.2;
//                    lbs.setText(lbsweight.toString());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        };

//        kgs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                lbs.setText("");
//                kgs.setText("");
//            }
//        });
//
//        lbs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                lbs.setText("");
//                kgs.setText("");
//            }
//        });

//        String temp1 = kgs.getText().toString();
//        Double lbsweight = 0.0;
//        if(!"".equals(temp1)){
//            lbsweight = Double.parseDouble(temp1);
//            lbsweight = lbsweight*2.2;
//            lbs.setText(lbsweight.toString());
//        }
//
//        String temp2 = lbs.getText().toString();
//        Double kgsweight = 0.0;
//        if(!"".equals(temp2)){
//            kgsweight = Double.parseDouble(temp2);
//            kgsweight = kgsweight/2.2;
//            kgs.setText(kgsweight.toString());
//        }


//                kgs.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        lbs.removeTextChangedListener(this);
//                        String temp = kgs.getText().toString();
//                        Double lbsweight = 0.0;
//                        if(!"".equals(temp)){
//                            lbsweight = Double.parseDouble(temp);
//                            lbsweight = lbsweight*2.2;
//                            lbs.setText(lbsweight.toString());
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//                    }
//                });
//
//
//                lbs.addTextChangedListener(new TextWatcher() {
//
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        kgs.removeTextChangedListener(this);
//                        String temp = lbs.getText().toString();
//                        Double kgsweight = 0.0;
//                        if(!"".equals(temp)){
//                            kgsweight = Double.parseDouble(temp);
//                            kgsweight = kgsweight/2.2;
//                            kgs.setText(kgsweight.toString());
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//                    }
//                });




//        lbs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String temp = kgs.getText().toString();
//                Double lgsweight = 0.0;
//            if(!"".equals(temp)){
//                lgsweight = Double.parseDouble(temp);
//                lgsweight = lgsweight*2.2;
//                lbs.setText(lgsweight.toString());
//            }
//            else{
//                Toast.makeText(getActivity(), "Please enter your weight", Toast.LENGTH_SHORT).show();
//            }
//            }
//        });
//        kgs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String temp = kgs.getText().toString();
//                Double kgsweight = 0.0;
//            if(!"".equals(temp)){
//                kgsweight = Double.parseDouble(temp);
//                kgsweight = kgsweight/2.2;
//                kgs.setText(kgsweight.toString());
//            }
//            else{
//                Toast.makeText(getActivity(), "Please enter your weight", Toast.LENGTH_SHORT).show();
//            }
//            }
//        });


        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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