package com.cch.jeonju;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardWriteActivity extends AppCompatActivity implements View.OnClickListener {

    final int GALLERY_CODE=100;
    private static final String TAG = "BoardWriteActivity";
    private AdView mAdView;

    private FirebaseStorage storage;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private EditText mWriteTitleText;
    private EditText mWriteContentsText;
    private ImageView mImageView;

    private String id;
    private String name;

    InputMethodManager imm;
    Uri uri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mWriteContentsText = findViewById(R.id.write_contents_text);
        mWriteTitleText = findViewById(R.id.write_title_text);
        mImageView = findViewById(R.id.write_image_view);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        mWriteTitleText.setOnEditorActionListener(onEditorActionListener);
        mWriteContentsText.setOnEditorActionListener(onEditorActionListener);
        findViewById(R.id.write_upload_button).setOnClickListener(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        name = document.getData().get("name").toString();
                        Log.d(TAG, "DocumentSnapshot data : "+document.getData());
                    } else {
                        Log.d(TAG,"No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });

        mAdView = findViewById(R.id.boardwrite_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                write();
                return true;
            } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                mWriteContentsText.requestFocus();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        write();
    }

    private void write() {

        if(uri == null) {
            Toast.makeText(this, "사진을 등록해야 업로드가 가능합니다.", Toast.LENGTH_LONG).show();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        id = mStore.collection("users").document().getId();
        Map<String, Object> post = new HashMap<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format_time = format.format(System.currentTimeMillis());
        post.put("useremail",user.getEmail());
        post.put("title",mWriteTitleText.getText().toString());
        post.put("contents",mWriteContentsText.getText().toString());
        post.put("name",name);
        post.put("date",format_time);
        post.put("uri",uri.toString());


        mStore.collection("board").document(id).set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BoardWriteActivity.this, "업로드 성공", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BoardWriteActivity.this,"업로드 실패", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void imageViewClicked(View view) {
        tedPermission();

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_CODE);

    }
    public void uploadTask(ImageView imageView) {
        storage = FirebaseStorage.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference storageRef = storage.getReference();
        final StorageReference mountainImagesRef = storageRef.child(user.getUid()+"/boardimage.jpg");

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable)  imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()) {
                    Log.e("실패1","실패1");
                    throw task.getException();
                }
                return mountainImagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    uri = downloadUri;
                    Log.e("성공","성공"+downloadUri);
                    loadingEnd();
                } else {
                    Log.e("실패2","실패2");
                }
            }
        });
    }

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("1")
                .setDeniedMessage("2")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE) {
            Log.d("onActivityResult: ", String.valueOf(resultCode));
            if(resultCode == RESULT_OK) {
                try {

                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    Log.d("img",img.toString());
                    in.close();
                   // Glide.with(this).load(img).into(mImageView);
                    mImageView.setImageBitmap(img);
                    loading();
                    uploadTask(mImageView);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
        }
    }

    public void cardViewClicked(View view) {
        imm.hideSoftInputFromWindow(findViewById(R.id.write_title_text).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.write_contents_text).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.write_image_view).getWindowToken(), 0);
    }

    public void loading() {
        //로딩
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog = new ProgressDialog(BoardWriteActivity.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("잠시만 기다려 주세요");
                        progressDialog.show();
                    }
                }, 0);
    }

    public void loadingEnd() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 0);
    }
}
