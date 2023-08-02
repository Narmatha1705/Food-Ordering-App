package com.example.foodscout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText username,email,password,confirmpwd,fullname;
    Button signup;
    TextView login;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmpwd= (EditText) findViewById(R.id.confirmpwd);
        fullname= (EditText) findViewById(R.id.fullname);
        signup = (Button) findViewById(R.id.signup);
        login = (TextView) findViewById(R.id.signin);

        fAuth = (FirebaseAuth) FirebaseAuth.getInstance();
        fStore = (FirebaseFirestore) FirebaseFirestore.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailValue = email.getText().toString().trim();
                final String passwordValue = password.getText().toString().trim();
                final String confpwdValue = confirmpwd.getText().toString().trim();
                final String fullnameValue = fullname.getText().toString().trim();
                final String usernameValue = username.getText().toString().trim();

                if(TextUtils.isEmpty(usernameValue)) {
                    username.setError("Username is Required");
                    return;
                }

                if(TextUtils.isEmpty(passwordValue)){
                    password.setError("Password is Required.");
                    return;
                }
                if(passwordValue.length()<8){
                    password.setError("Password must be >= 8.");
                    return;
                }

                if(TextUtils.isEmpty(confpwdValue)) {
                    confirmpwd.setError("Password is Required.");
                    return;
                }
                if (!passwordValue.equals(confpwdValue)) {
                        confirmpwd.setError("Password does not match");
                        return;
                }

                if(TextUtils.isEmpty(fullnameValue)) {
                    fullname.setError("Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(emailValue)){
                    email.setError("Email is Required.");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(emailValue,passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df=fStore.collection("Users").document(user.getUid());
                            Map<String,Object> userInfo =new HashMap<>();
                            userInfo.put("Username",usernameValue);
                            userInfo.put("FullName",fullnameValue);
                            userInfo.put("Email",emailValue);

                            userInfo.put("isUser",1);
                            df.set(userInfo);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(RegisterActivity.this, "Error !!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });



            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}
