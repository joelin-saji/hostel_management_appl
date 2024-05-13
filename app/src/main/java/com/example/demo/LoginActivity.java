package com.example.demo;

// Import necessary Android libraries
import android.accounts.OnAccountsUpdateListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Import androidx libraries
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// Import FirebaseAuth and other Firebase-related libraries
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// LoginActivity class definition
public class LoginActivity extends AppCompatActivity {

    // Declare UI elements
    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    FirebaseAuth auth;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Set the layout for the activity
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        loginPassword = findViewById(R.id.login_password);
        loginEmail = findViewById(R.id.login_email);
        signupRedirectText = findViewById(R.id.SignupRedirectText);
        loginButton = findViewById(R.id.login_button);
        forgotPassword = findViewById(R.id.forgot_password);

        // Set OnClickListener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve email and password entered by the user
                String password = loginPassword.getText().toString();
                String email = loginEmail.getText().toString();

                // Validate email format
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Check if password is empty
                    if (!password.isEmpty()) {
                        // Sign in with email and password using FirebaseAuth
                        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // If sign-in is successful, display success message and navigate to MainActivity
                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // If sign-in fails, display error message
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Display error if password is empty
                        loginPassword.setError("Password cannot be empty");
                    }
                } else if (email.isEmpty()) {
                    // Display error if email is empty
                    loginEmail.setError("Email.cannot be empty");
                } else {
                    // Display error if email format is invalid
                    loginEmail.setError("Please enter valid email");
                }
            }
        });

        // Set OnClickListener for the signup redirect text
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SignupActivity when signup redirect text is clicked
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        // Set OnClickListener for the forgot password text
        forgotPassword.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and display a dialog for password reset
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                // Set OnClickListener for the reset button in the dialog
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the user-entered email
                        String userEmail = emailBox.getText().toString();

                        // Check if email is empty or invalid
                        if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                            Toast.makeText(LoginActivity.this, "Enter your registered mail id", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Send password reset email using FirebaseAuth
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Check if password reset email was sent successfully
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Unable to send, failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                // Set OnClickListener for the cancel button in the dialog
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // Set background color of the dialog window
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }

                // Display the dialog
                dialog.show();
            }
        }));
    }
}
