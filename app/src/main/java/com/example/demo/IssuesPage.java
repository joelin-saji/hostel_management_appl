package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IssuesPage extends AppCompatActivity {

    EditText roomnumb, mailid, issue;
    String UserID = "";
    Button iss_history_btn, iss_submit_btn;

    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues_page);
        iss_history_btn = findViewById(R.id.issues_history_button);
        iss_submit_btn = findViewById(R.id.issues_submit_button);
        roomnumb =(EditText) findViewById(R.id.room_number);
        mailid =(EditText) findViewById(R.id.email);
        issue =(EditText) findViewById(R.id.issue);
        databaseUsers = FirebaseDatabase.getInstance().getReference();

        iss_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData();
            }
        });

        iss_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IssuesPage.this, IssuesList.class));
                finish();
            }
        });

    }

    private void InsertData() {
        String iss_roomnum = roomnumb.getText().toString().trim();
        String iss_email = mailid.getText().toString().trim();
        String iss_issues = issue.getText().toString().trim();
        // Generate a unique key for the new issue entry
        String newIssueKey = databaseUsers.push().getKey();

        issue_dataholder user = new issue_dataholder(iss_email, iss_issues, iss_roomnum);
        databaseUsers.child("issue").child(newIssueKey).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(IssuesPage.this, "Issue Submitted successfully", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }



}