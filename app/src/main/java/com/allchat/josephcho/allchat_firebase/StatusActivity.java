package com.allchat.josephcho.allchat_firebase;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputEditText mStatusInput;
    private Button mStatusButton;

    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

    //TODO: progressdialogs are deprecated, need to update displaying progress to user
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentID = mCurrentUser.getUid();
        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentID);

        mToolbar = (Toolbar)findViewById(R.id.status_navbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String existingStatus = getIntent().getStringExtra("existingStatus");

        mStatusInput = (TextInputEditText) findViewById(R.id.status_input);
        mStatusButton = (Button) findViewById(R.id.status_input_button);

        mStatusInput.setText(existingStatus);

        mStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress = new ProgressDialog(StatusActivity.this);
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Just a sec while we change your status");
                mProgress.show();

                //set the status (upload to firebase)
                //String status = mStatusInput.getEditText().getText().toString();

                String status = mStatusInput.getText().toString();
                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            mProgress.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sorry, there was an error. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


    }
}











