package babble.babble.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import babble.babble.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private TextView registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailInput = findViewById(R.id.emailInputRegister);
        passwordInput = findViewById(R.id.passWordInputRegister);
        registerButton = findViewById(R.id.signUpButton);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if fields aren't empty so it can create a new account
                if (!TextUtils.isEmpty(emailInput.getText().toString()) && !TextUtils.isEmpty(passwordInput.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(emailInput.getText().toString(), passwordInput.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success
                                        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue("");
                                        sendToMain();
                                        Toast.makeText(RegisterActivity.this, R.string.account_creation_confirm, Toast.LENGTH_LONG).show();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    //If fields are empty display a message to the user
                    Toast.makeText(RegisterActivity.this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
