package com.example.demo;

// Import necessary Android libraries
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Import necessary AndroidX libraries
import androidx.activity.EdgeToEdge; // Used for edge-to-edge display
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Import Firebase authentication and database libraries
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// SignupActivity class definition
public class SignupActivity extends AppCompatActivity {

    // Declare UI elements
    EditText signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display (requires androidx.activity:activity:1.2.0-alpha08 or higher)
        EdgeToEdge.enable(this);

        // Set the layout for the activity
        setContentView(R.layout.activity_signup);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // Set OnClickListener for the signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get user-entered email and password
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();

                // Validate email and password
                if(email.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                if(password.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                } else {
                    // Create user account with email and password using FirebaseAuth
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Check if user registration is successful
                            if(task.isSuccessful()){
                                // Display success message and redirect to LoginActivity
                                Toast.makeText(SignupActivity.this, "You have registered successfully!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                // Display error message if registration fails and redirect to LoginActivity
                                Toast.makeText(SignupActivity.this, "SignUp Failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

        // Set OnClickListener for the login redirect text
        loginRedirectText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Redirect to LoginActivity when login redirect text is clicked
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
