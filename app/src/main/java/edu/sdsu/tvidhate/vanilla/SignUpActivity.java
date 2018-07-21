package edu.sdsu.tvidhate.vanilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity implements SharedConstants,View.OnClickListener {

    private EditText username,emailId,password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.signUpUsernameEditText);
        emailId = findViewById(R.id.signUpEmailAddressEditText);
        password = findViewById(R.id.signUpPasswordEditText);
        Button submit = findViewById(R.id.submitButton);
        Button reset = findViewById(R.id.resetButton);

        auth = FirebaseAuth.getInstance();

        submit.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.submitButton:
                final String email = emailId.getText().toString().trim();
                String pass = password.getText().toString().trim();
                final String userName = username.getText().toString().trim();
                if(validInput()){
                    Log.d("TPV-NOTE","user data: "+email+" "+pass+" "+userName);
                    auth.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.d("TPV-NOTE","Exception: "+task.getException());
                                        Toast.makeText(SignUpActivity.this, REGISTRATION_FAILED, Toast.LENGTH_SHORT).show();
                                        emailId.setError(INVALID_EMAIL);
                                    } else {
                                        FirebaseUser user = auth.getCurrentUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(email)
                                                .build();
                                        try {
                                            if(user != null){
                                                user.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d("TPV-NOTE", "User profile updated.");
                                                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                                    finish();
                                                                }else{
                                                                    Log.d("TPV-NOTE","user profile update failed");
                                                                }
                                                            }
                                                        });
                                            }

                                        } catch (Exception e) {
                                            Log.d("TPV-NOTE", "failed: "+e);
                                        }
                                    }
                                }
                            });
                }else{
                    Toast.makeText(getApplicationContext(), ENTER_REQUIRED_FIELDS, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.resetButton:
                emailId.setText(EMPTY_STRING);
                password.setText(EMPTY_STRING);
                username.setText(EMPTY_STRING);
                break;
        }
    }

    private boolean validInput() {
        boolean dataValid = true;
        if (TextUtils.isEmpty(emailId.getText().toString())) {
            emailId.setError(ENTER_EMAIL);
            dataValid = false;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError(ENTER_PASSWORD);
            dataValid = false;
        }
        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setText(ANONYMOUS);
        }
        if (password.getText().toString().length() < 6) {
            password.setError(PASSWORD_LENGTH);
            Toast.makeText(getApplicationContext(), PASSWORD_LENGTH,
                    Toast.LENGTH_SHORT).show();
            dataValid = false;
        }
        return dataValid;
    }

}
