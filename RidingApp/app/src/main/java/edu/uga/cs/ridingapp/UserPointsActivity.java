package edu.uga.cs.ridingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserPointsActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private TextView userPointsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_points);

        // Initialize UI components
        userPointsTextView = findViewById(R.id.textView2);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            updateUserPoints(userEmail);
        }

    }

    private void updateUserPoints(String userEmail) {
        DatabaseReference usersRef = database.getReference("users"); // Assuming "users" is the top-level node in your database
        Query query = null;
        if (userEmail != null && !userEmail.isEmpty()) {
            query = usersRef.orderByChild("email").equalTo(userEmail);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        HelperClass helperClass = userSnapshot.getValue(HelperClass.class);
                        if (helperClass != null) {
                            // Display user points in TextView
                            String userPoints = String.valueOf(helperClass.getUserPoints());
                            userPointsTextView.setText("User Points: " + userPoints);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                    Log.e("Firebase", "Error querying user data", databaseError.toException());
                }
            });
        }
        else{
            Log.e("Firebase", "User email is null or empty");
        }

    }
}
