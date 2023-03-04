package com.example.finalloginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.internal.SafeIterableMap;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //String User, usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = findViewById(R.id.loginBttn);
        TextView register = findViewById(R.id.registerBttn);
        EditText emailLogin = findViewById(R.id.userName);
        EditText passwordLogin = findViewById(R.id.password);

        //log in button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = emailLogin.getText().toString();
                String passwordInput = passwordLogin.getText().toString();

                db.collection("users")
                        .whereEqualTo("email", emailInput)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                       if(!queryDocumentSnapshots.isEmpty()){
                          DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                          String userPassword = documentSnapshot.getString("password");

                       // Check if the password entered by the user matches the password in Firestore
                          if (Objects.equals(userPassword, String.valueOf(passwordInput))) {
                             // User logged in successfully
                             Toast.makeText(MainActivity.this, "Welcome!"
                                     ,Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(getApplication(), InsideActivity.class);
                             startActivity(intent);
                          }

                          else {
                           // Incorrect password
                           Toast.makeText(MainActivity.this, "Incorrect Password"
                                   ,Toast.LENGTH_SHORT).show();
                           }
                       }

                       else {
                                // User not found in Firestore
                                Toast.makeText(MainActivity.this, "User not found"
                                        ,Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error" + e
                                        , Toast.LENGTH_SHORT).show();
                                Log.w("FIRESTORE", "Error finding user", e);
                            }
                        });

                }
            });

        //register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}