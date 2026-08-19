package edu.uga.cs.hungerhero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ReceiverVerification extends AppCompatActivity {

    private EditText editTextOrganizationName, editTextContactPerson, editTextContactEmail, editTextContactPhone;
    private Button buttonVerify;

    private DatabaseReference verificationRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.verification_page);

        editTextOrganizationName = findViewById(R.id.editTextOrganizationName);
        editTextContactPerson = findViewById(R.id.editTextContactPerson);
        editTextContactEmail = findViewById(R.id.editTextContactEmail);
        editTextContactPhone = findViewById(R.id.editTextContactPhone);
        buttonVerify = findViewById(R.id.buttonVerify);

        firebaseAuth = FirebaseAuth.getInstance();
        verificationRef = FirebaseDatabase.getInstance().getReference("receiver_verifications");

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyReceiver();
            }
        });
    }

    private void verifyReceiver() {
        String organizationName = editTextOrganizationName.getText().toString().trim();
        String contactPerson = editTextContactPerson.getText().toString().trim();
        String contactEmail = editTextContactEmail.getText().toString().trim();
        String contactPhone = editTextContactPhone.getText().toString().trim();

        if (organizationName.isEmpty() || contactPerson.isEmpty() || contactEmail.isEmpty() || contactPhone.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = firebaseAuth.getCurrentUser().getUid();

        Map<String, Object> verificationData = new HashMap<>();
        verificationData.put("organizationName", organizationName);
        verificationData.put("contactPerson", contactPerson);
        verificationData.put("contactEmail", contactEmail);
        verificationData.put("contactPhone", contactPhone);

        verificationRef.child(userId).setValue(verificationData);

        Toast.makeText(this, "Verification successful", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ReceiverVerification.this, Receive.class);
        startActivity(intent);
        finish();
    }
}
