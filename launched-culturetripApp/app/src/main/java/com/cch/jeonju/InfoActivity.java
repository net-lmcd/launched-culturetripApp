package com.cch.jeonju;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class InfoActivity extends AppCompatActivity {

    String passedTitle;  // MapActivity로부터 넘어온 intent값
    int passedThreadnum;
    int index;

    String imgUrl;

    ImageView imageView;
    TextView textView1, textView2, textView3, textView4, textView5, textView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent  = getIntent();
        passedTitle = intent.getStringExtra("pass");
        passedThreadnum = intent.getIntExtra("threadNum",0);
        index = intent.getIntExtra("index",0);

        imageView = findViewById(R.id.imageview);
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        textView3 = findViewById(R.id.text3);
        textView4 = findViewById(R.id.text4);
        textView5 = findViewById(R.id.text5);
        textView6 = findViewById(R.id.text6);

        if(passedThreadnum == 0 ) {
            Glide.with(this).load(MapActivity.information.get(index).getFileUrl()).into(imageView);
            textView1.setText(MapActivity.information.get(index).getDataTitle());
            textView2.setText("지정번호 : "+MapActivity.information.get(index).getDesignateNo());
            textView3.setText("설립년도 : "+MapActivity.information.get(index).getDesignateDate());
            textView4.setText("주소 : "+MapActivity.information.get(index).getAddrDtl());
            textView5.setText("문화재 내용  : "+MapActivity.information.get(index).getDataContent()+"\n"+MapActivity.information.get(index).getIntroDataContent());
        } else if (passedThreadnum == 1 ) {
            Glide.with(this).load(MapActivity.information.get(index).getFileUrl()).into(imageView);
            textView1.setText(MapActivity.information.get(index).getDataTitle());
            textView2.setText("주소 : "+MapActivity.information.get(index).getAddrDtl());
            textView3.setText("내용 : "+MapActivity.information.get(index).getDataContent());
            textView4.setText("정보 : "+MapActivity.information.get(index).getTouretcetera());
        } else if (passedThreadnum == 2 ) {
            Glide.with(this).load(MapActivity.information.get(index).getFileUrl()).into(imageView);
            textView1.setText(MapActivity.information.get(index).getDataTitle());
            textView2.setText("주소 : "+MapActivity.information.get(index).getAddrDtl());
            textView3.setText("홈페이지 : "+ MapActivity.information.get(index).getHomepage());
            textView4.setText("tel : " + MapActivity.information.get(index).getTel()+"\n"+"우편번호 : " +MapActivity.information.get(index).getZipcode());
            textView5.setText("내용 : \n"+MapActivity.information.get(index).getDataContent());
        } else {
            Glide.with(this).load(MapActivity.information.get(index).getFileUrl()).into(imageView);
            textView1.setText(MapActivity.information.get(index).getDataTitle());
            textView2.setText("주소 : "+ MapActivity.information.get(index).getAddrDtl());
            textView3.setText("홈페이지 : "+ MapActivity.information.get(index).getHomepage());
            textView4.setText("전화번호 : "+MapActivity.information.get(index).getTel()+" \n우편번호 : "+MapActivity.information.get(index).getZipcode());
            textView5.setText("내용 : \n" +MapActivity.information.get(index).getDataContent());
        }
    }
}
