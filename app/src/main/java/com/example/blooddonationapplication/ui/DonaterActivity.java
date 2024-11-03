package com.example.blooddonationapplication.ui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blooddonationapplication.MainActivity;
import com.example.blooddonationapplication.databinding.ActivityDonaterBinding;
import com.example.blooddonationapplication.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DonaterActivity extends AppCompatActivity {
    private ActivityDonaterBinding binding;
    private String name, phone, email, password,bloodGroup,age,address,gender;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDonaterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        String[] items = new String[]{"Select Blood Group","A+","A-","B+","B-","AB+","AB-","O+","0-"};
        binding.bloodGroup.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));

        binding.bloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodGroup = binding.bloodGroup.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        FirebaseUser currentUser = auth.getCurrentUser();
        boolean fromRegistration = getIntent().getBooleanExtra("fromRegistration", false);
        if (currentUser == null && !fromRegistration) {
            startActivity(new Intent(DonaterActivity.this, LoginActivity.class));
            finish();
            return;
        }

        String currentUserId = currentUser != null ? currentUser.getUid() : null;
        if (currentUserId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("user").child(currentUserId);
        }

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = binding.inputName.getText().toString();
                phone = binding.inputPhone.getText().toString();
                email = binding.inputEmail.getText().toString();
                password = binding.inputPassword.getText().toString();
                age = binding.inputAge.getText().toString();
                gender = binding.inputGender.getText().toString();
                address = binding.inputAddress.getText().toString();
                if (name.isEmpty()) {
                    binding.inputName.setError("Please enter your name");
                    binding.inputName.requestFocus();
                } else if (phone.isEmpty()) {
                    binding.inputPhone.setError("Please enter your Number");
                    binding.inputPhone.requestFocus();
                } else if (email.isEmpty()) {
                    binding.inputEmail.setError("Please enter your Email");
                    binding.inputEmail.requestFocus();
                } else if (password.isEmpty()) {
                    binding.inputPassword.setError("Please enter your Password");
                    binding.inputPassword.requestFocus();
                }
                else if(age.isEmpty()) {
                    binding.inputAge.setError("Please enter your Age");
                }
                else if(gender.isEmpty()) {
                    binding.inputGender.setError("Please enter your Gender");
                }
                else if(address.isEmpty()) {
                    binding.inputAddress.setError("Please enter your Address");
                }
                createUserWithEmailAndPassword(email, password);
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DonaterActivity.this, LoginActivity.class));
            }
        });
    }

    private void createUserWithEmailAndPassword(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            insertData();
                        } else {
                            handleRegistrationFailure(task);
                        }
                    }
                });
    }

    private void insertData() {
        UserModel userModel = new UserModel(name, phone,email, password,bloodGroup,age,gender,address);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("phone", phone);
        hashMap.put("email", email);
        hashMap.put("password", password);
        hashMap.put("blood", bloodGroup);
        hashMap.put("age", age);
        hashMap.put("gender", gender);
        hashMap.put("address",address);
        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Registration successful, navigate to MainActivity
                    startActivity(new Intent(DonaterActivity.this, MainActivity.class));
                    finish(); // Close this activity
                    Toast.makeText(DonaterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                } else {
                    // Registration failed, display error message
                    String errorMessage = "Failed to register donor: " + task.getException().getMessage();
                    Log.e(TAG, errorMessage);
                    Toast.makeText(DonaterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




























    private void handleRegistrationFailure(Task<AuthResult> task) {
        Exception exception = task.getException();
        if (exception instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(DonaterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DonaterActivity.this, "Authentication failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
