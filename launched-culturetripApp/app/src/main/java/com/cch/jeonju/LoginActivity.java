package com.cch.jeonju;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AdView mAdView;

    InputMethodManager imm;
    EditText email_editText;
    EditText password_editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, getString(R.string.app_admob_id));

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        mAuth = FirebaseAuth.getInstance();

        email_editText = findViewById(R.id.email_Edittext);
        password_editText = findViewById(R.id.password_Edittext);
        email_editText.setOnEditorActionListener(onEditorActionListener);
        password_editText.setOnEditorActionListener(onEditorActionListener);

        findViewById(R.id.login_button).setOnClickListener(onClickListener);
        findViewById(R.id.goto_passwordresetButton).setOnClickListener(onClickListener);
        findViewById(R.id.goto_initmemberinfo).setOnClickListener(onClickListener);

        mAdView = findViewById(R.id.login_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
                return true;
            } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                password_editText.requestFocus();
                return true;
            }
            return false;
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.login_button:
                    login();
                    break;
                case R.id.goto_passwordresetButton:
                    nextActivity(ResetPasswordActivity.class);
                    break;
                case R.id.goto_initmemberinfo:
                    nextActivity(SignupActivity.class);
                    break;
            }
        }
    };

    private void login() {

        String email =  ((EditText)findViewById(R.id.email_Edittext)).getText().toString();
        String password = ((EditText)findViewById(R.id.password_Edittext)).getText().toString();

        if(email.length() > 0 && password.length()>0 ) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인에 성공하였습니다.");
                                nextActivity(MainActivity.class);
                            } else {
                                if(task.getException() != null) {
                                    startToast("이메일또는 비밀번호가 존재하지 않습니다.");
                                }
                            }
                        }
                    });
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

    public void CardViewClicked(View view) {
        imm.hideSoftInputFromWindow(findViewById(R.id.email_Edittext).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.password_Edittext).getWindowToken(), 0);
    }
}
