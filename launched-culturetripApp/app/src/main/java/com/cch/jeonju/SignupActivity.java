package com.cch.jeonju;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private AdView mAdView;
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    public InputMethodManager imm;

    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordcheckEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        emailEditText = findViewById(R.id.signup_email_Edittext);
        passwordEditText = findViewById(R.id.signup_password_Edittext);
        passwordcheckEditText = findViewById(R.id.signup_passwordcheck_Edittext);
        emailEditText.setOnEditorActionListener(onEditorActionListener);
        passwordEditText.setOnEditorActionListener(onEditorActionListener);
        passwordcheckEditText.setOnEditorActionListener(onEditorActionListener);
        findViewById(R.id.signup_Button).setOnClickListener(onClickListener);
        findViewById(R.id.goto_login_button).setOnClickListener(onClickListener);

        mAdView = findViewById(R.id.signup_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signUp();
                return true;
            } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                int textVIewId = v.getId();
                switch(textVIewId) {
                    case R.id.signup_email_Edittext:
                        passwordEditText.requestFocus();
                        break;
                    case R.id.signup_password_Edittext:
                        passwordcheckEditText.requestFocus();
                        break;
                }
                return true;
            }
            return false;
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.signup_Button:
                    signUp();
                    break;
                case R.id.goto_login_button:
                    nextActivity(LoginActivity.class);
                    break;
            }
        }
    };

    private void signUp() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordcheck = passwordcheckEditText.getText().toString();

        if(email.length() > 0 && password.length()>0 && passwordcheck.length() >0 ) {
            if(password.equals(passwordcheck)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원가입이 성공적으로 완료되었습니다.");
                                    nextActivity(MainActivity.class);
                                } else {
                                    if(task.getException() != null) {
                                        startToast(task.getException().toString());
                                    }
                                }
                            }
                        });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        } else {
            startToast("이메일 또는 비밀번호를 입력해 주세요");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void nextActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cardViewClicked(View view) {
        imm.hideSoftInputFromWindow(findViewById(R.id.signup_email_Edittext).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.signup_password_Edittext).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.signup_passwordcheck_Edittext).getWindowToken(), 0);
    }
}
