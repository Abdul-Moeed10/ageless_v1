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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Set_Username extends AppCompatActivity {

    private EditText user_fullname;
    private EditText user_username;
    private Button register_button;
    String user_email;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    UserInfo userInfo = new UserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_username);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserInfo").child(uid);


        user_fullname=findViewById(R.id.user_fullname);
        user_username=findViewById(R.id.user_username);



        Button register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                registerNewUser();
//                if (TextUtils.isEmpty(new_password)){
//                    Toast.makeText(Set_Username.this, "please enter a password", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    if (new_password != confirm_password){
//                        Toast.makeText(Set_Username.this, "passwords do not match", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(Set_Username.this, "passwords match", Toast.LENGTH_SHORT).show();
//                        registerNewUser();
//                        Intent myIntent = new Intent(Set_Username.this, Login.class);
//                        startActivity(myIntent);
//                    }
//                }

                String fullname = user_fullname.getText().toString();
                String username = user_username.getText().toString();

                if(TextUtils.isEmpty(fullname) || TextUtils.isEmpty(username)){
                    Toast.makeText(Set_Username.this, "Please enter details", Toast.LENGTH_SHORT).show();
                }
                else{
                    add_to_database(fullname, username);
                }

            }
        });



    }

    private void add_to_database(String fullname, String username){
        userInfo.setUser_full_name(fullname);
        userInfo.setUser_username(username);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(userInfo);
                Toast.makeText(Set_Username.this, "Name added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Set_Username.this, "Name not added. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    private void registerNewUser(){
//        String new_password, confirm_password;
//        new_password = user_new_password.getText().toString();
//        confirm_password = user_confirm_password.getText().toString();
//        String email;
//       // databaseReference.child(phone_no).child(user_email);
//        Intent intent = getIntent();
//        email = intent.getStringExtra("user_email");
//
//
//        if (TextUtils.isEmpty(new_password) || TextUtils.isEmpty(confirm_password)) {
//            Toast.makeText(getApplicationContext(),
//                            "Please enter password!!",
//                            Toast.LENGTH_LONG)
//                    .show();
//            return;
//        }
//
//        firebaseAuth.createUserWithEmailAndPassword(email, confirm_password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(Set_Username.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                            Intent myIntent = new Intent(Set_Username.this, Login.class);
//                            startActivity(myIntent);
//                        }
//                     else{
//                            Toast.makeText(Set_Username.this, "failed to register user", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//    }
}
