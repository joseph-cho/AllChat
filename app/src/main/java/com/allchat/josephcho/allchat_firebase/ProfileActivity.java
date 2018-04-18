package com.allchat.josephcho.allchat_firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView mDisplayID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get the intent that started this activity
        //from usersactivity
        String userId = getIntent().getStringExtra("userId");

        mDisplayID = (TextView) findViewById(R.id.profile_displayName);
        mDisplayID.setText(userId);
    }
}
