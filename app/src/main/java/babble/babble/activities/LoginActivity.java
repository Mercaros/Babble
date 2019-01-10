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

import babble.babble.R;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private TextView loginButton;
    private TextView createAccountButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = findViewById(R.id.profile_activity_username);
        passwordInput = findViewById(R.id.profile_activity_description);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.register_account);
        mAuth = FirebaseAuth.getInstance();

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSignUp();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if fields aren't empty so it can sign in
                if (!TextUtils.isEmpty(emailInput.getText().toString()) && !TextUtils.isEmpty(passwordInput.getText().toString())) {
                    mAuth.signInWithEmailAndPassword(emailInput.getText().toString(), passwordInput.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        sendToMain();
                                        Toast.makeText(LoginActivity.this, R.string.succesfully_login, Toast.LENGTH_LONG).show();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendToMain() {
        //Zorgt ervoor dat de gebruiker niet terug gaat naar Loginactivity tenzij ze uitloggen
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendToSignUp() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
