package com.example.ageless_v1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Set_Password extends AppCompatActivity {

    private EditText user_new_password;
    private EditText user_confirm_password;
    private Button register_button;
    String user_email;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Data");

        user_new_password=findViewById(R.id.user_new_password);
        user_confirm_password=findViewById(R.id.user_confirm_password);

        Button register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });

        getData();


    }

    private String getData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_email=snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return user_email;
    }

    private void registerNewUser(){
        String new_password, confirm_password;
        new_password = user_new_password.getText().toString();
        confirm_password = user_confirm_password.getText().toString();

        if (TextUtils.isEmpty(new_password)){
            Toast.makeText(Set_Password.this, "please enter a password", Toast.LENGTH_SHORT).show();
        }
        else{
            if (new_password != confirm_password){
                Toast.makeText(Set_Password.this, "passwords do not match", Toast.LENGTH_SHORT).show();
            }
        }

        firebaseAuth.createUserWithEmailAndPassword(user_email,new_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Set_Password.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(Set_Password.this, MainActivity.class);
                            startActivity(myIntent);
                        }
                     else{
                            Toast.makeText(Set_Password.this, "failed to register user", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}
