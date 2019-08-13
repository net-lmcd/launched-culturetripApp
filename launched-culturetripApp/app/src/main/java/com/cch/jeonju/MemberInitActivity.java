package com.cch.jeonju;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.cch.jeonju.memberinit.MemberInfo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberInitActivity extends AppCompatActivity {

    private AdView mAdView;
    private static final String TAG = "MemberInitActivity";

    InputMethodManager imm;
    EditText initnameEditText;
    EditText initphoneEditText;
    EditText initdateEditText;
    EditText initaddressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initnameEditText = findViewById(R.id.membernameEditText);
        initdateEditText = findViewById(R.id.memberdateEditText);
        initphoneEditText = findViewById(R.id.memberphonenumberEditText);
        initaddressEditText = findViewById(R.id.memberaddressEditText);
        initnameEditText.setOnEditorActionListener(onEditorActionListener);
        initphoneEditText.setOnEditorActionListener(onEditorActionListener);
        initdateEditText.setOnEditorActionListener(onEditorActionListener);
        initaddressEditText.setOnEditorActionListener(onEditorActionListener);

        mAdView = findViewById(R.id.memberinit_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        findViewById(R.id.member_check_button).setOnClickListener(onClickListener);
    }

    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                profileUpdate();
                return true;
            } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                int textVIewId = v.getId();
                switch(textVIewId) {
                    case R.id.membernameEditText:
                        initphoneEditText.requestFocus();
                        break;
                    case R.id.memberphonenumberEditText:
                        initdateEditText.requestFocus();
                        break;
                    case R.id.memberdateEditText:
                        initaddressEditText.requestFocus();
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
                case R.id.member_check_button:
                    profileUpdate();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void profileUpdate() {

        String name =  ((EditText)findViewById(R.id.membernameEditText)).getText().toString();
        String phoneNumber =  ((EditText)findViewById(R.id.memberphonenumberEditText)).getText().toString();
        String birthDay =  ((EditText)findViewById(R.id.memberdateEditText)).getText().toString();
        String address =  ((EditText)findViewById(R.id.memberaddressEditText)).getText().toString();

        if(name.length() > 0 && phoneNumber.length() > 9 && birthDay.length() > 5 && address.length() > 0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthDay, address);

            if( user != null ) {
                db.collection("users").document(user.getUid()).set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원정보 등록이 완료되었습니다.");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 등록이 실패하였습니다.");
                                Log.d(TAG, "Error writing document", e);
                            }
                        });
            }

        } else {
            startToast("회원정보를 모두 기입해 주세요.");
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void cardViewClicked(View view) {
        imm.hideSoftInputFromWindow(findViewById(R.id.membernameEditText).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.memberphonenumberEditText).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.memberdateEditText).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.memberaddressEditText).getWindowToken(), 0);
    }
}
