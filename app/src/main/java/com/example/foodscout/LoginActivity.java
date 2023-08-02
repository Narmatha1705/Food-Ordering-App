package com.example.foodscout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView signup, forgetpass;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        signup = (TextView) findViewById(R.id.signup);
        forgetpass = (TextView) findViewById(R.id.forgetpwd);

        fAuth = (FirebaseAuth) FirebaseAuth.getInstance();
        fstore = (FirebaseFirestore) FirebaseFirestore.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String emailValue1 = email.getText().toString();
                // String passwordValue1 = password.getText().toString();

                //Toast.makeText(LoginActivity.this, "Email: " + emailValue1 + " Password: " + passwordValue1, Toast.LENGTH_SHORT).show();

                String emailValue = email.getText().toString().trim();
                String passwordValue = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailValue)) {
                    email.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(passwordValue)) {
                    password.setError("Password is Required.");
                    return;
                }
                if (passwordValue.length() < 8) {
                    password.setError("Password must be >= 8.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Toast.makeText(LoginActivity.this, "You are logged in now", Toast.LENGTH_SHORT).show();
                        checkUserAccessLevel(authResult.getUser().getUid());
                        //startActivity(new Intent(getApplicationContext(), HomePage.class));
                        // Intent intent = new Intent(LoginActivity.this, HomePage.class);
                        //startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error !!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }


            //signup.setOnClickListener(new View.OnClickListener() {
            //@Override
            // public void onClick(View v) {
            // Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            //startActivity(intent);

            // }
            //});
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final EditText resetMail = new EditText(v.getContext());
                // final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                //  passwordResetDialog.setTitle("Reset Password?");
                //  passwordResetDialog.setMessage("Enter your email to reset password:");
                // passwordResetDialog.setView(resetMail);

                //  passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                //  @Override
                // public void onClick(DialogInterface dialog, int which) {

                // String mail = resetMail.getText().toString().trim();
                String emailValue = email.getText().toString().trim();
                if (TextUtils.isEmpty(emailValue)) {
                    Toast.makeText(getApplication(), "Enter your registered email id on email field to reset your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility((View.VISIBLE));

                fAuth.sendPasswordResetEmail(emailValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Reset Link is sent to your Email", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "ERROR ! Reset Link could not be sent" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
                //.addOnFailureListener(new OnFailureListener() {
                //@Override
                //  public void onFailure(@NonNull Exception e) {
                //  Toast.makeText(LoginActivity.this, "ERROR ! Reset Link could not be sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                //     }
                // });


            }
        });
        //    passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
        //     @Override
        //    public void onClick(DialogInterface dialog, int which) {

        //   }
        //  });
        // }
        //  });

        // }

    }

    public void checkUserAccessLevel(String uid){
        DocumentReference df = fstore.collection("Users").document(uid);
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    if (document.exists()){
                        if(document.get("isAdmin")!=null){
                            Intent intent = new Intent(LoginActivity.this, AdminFrontpage.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(LoginActivity.this, HomePage.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }


}
