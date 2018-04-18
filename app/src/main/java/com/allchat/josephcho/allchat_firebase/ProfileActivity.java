package com.allchat.josephcho.allchat_firebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mProfilePic;
    private TextView mProfileName, mProfileStatus;
    private Button mProfileSendFriendReq;

    private DatabaseReference mUsersDB;

    private DatabaseReference mFriendReqDB;

    private FirebaseUser mCurrentUser;

    private String mCurrentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get the intent that started this activity
        //from usersactivity
        final String userId = getIntent().getStringExtra("userId"); //id of user whose profile we are visiting

        mUsersDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mFriendReqDB = FirebaseDatabase.getInstance().getReference().child("FriendReqs");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mProfilePic = (ImageView) findViewById(R.id.profile_pic);
        mProfileName = (TextView)findViewById(R.id.profile_displayName);
        mProfileStatus = (TextView)findViewById(R.id.profile_status);
        mProfileSendFriendReq = (Button)findViewById(R.id.profile_send_friendReq);

        mCurrentState = "notFriends";

        mUsersDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayName = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();

                mProfileName.setText(displayName);
                mProfileStatus.setText(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //when current user clicks send friend req button on another user's page they are visiting...
        mProfileSendFriendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentState.equals("notFriends")) {

                    //if the users aren't already friends, go into the FriendReqDB and set the request
                    //status between the two user ID's as sent
                    mFriendReqDB.child(mCurrentUser.getUid()).child(userId).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {

                                //set the recipient of the friend request's status to received
                                mFriendReqDB.child(userId).child(mCurrentUser.getUid()).child("request_type")
                                        .setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfileActivity.this, "Request Sent!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed to send request", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
