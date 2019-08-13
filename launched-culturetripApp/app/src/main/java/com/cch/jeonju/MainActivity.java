package com.cch.jeonju;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cch.jeonju.info.Information;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private static final String TAG = "MainActivity";
    RelativeLayout layout;

    float lat[] = new float[34];
    float lon[] = new float[34];
    String title[] = new String[34];
    int allListindex = 0;
    int threadNum;

    private CardView card1, card2, card3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
        // 데이터 수집을위한 loading 메소드
        loading();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
            nextActivity(SignupActivity.class);
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot != null) {
                            if(documentSnapshot.exists()) {
                                Log.d(TAG, "DocumentSnapshot date : " +documentSnapshot.getData());
                            } else {
                                Log.d(TAG, "No Such document");
                                Toast.makeText(MainActivity.this, "회원등록화면으로 이동합니다.", Toast.LENGTH_LONG).show();
                                nextActivity(MemberInitActivity.class);
                            }
                        }
                        mainExceute();
                    }
                }
            });
        }

    }

    public void mainExceute() {
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);

        ButtonListener buttonListener = new ButtonListener();
        card1.setOnClickListener(buttonListener);
        card2.setOnClickListener(buttonListener);
        card3.setOnClickListener(buttonListener);

        ArrayList<Mythread1> mythreads = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            mythreads.add(new Mythread1(MapActivity.request[i], i));
            mythreads.get(i).start();
            try {
                mythreads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (this) {
            new UrlTaskinMain().execute();
        }
    }
    class ButtonListener implements View.OnClickListener {

        Intent intent;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.card1:
                    intent = new Intent(MainActivity.this, MapActivity.class);
                    break;
                case R.id.card2:
                    intent = new Intent(MainActivity.this, RecyclerViewActivity.class);
                    break;
                case R.id.card3:
                    intent = new Intent(MainActivity.this, PostActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }

    public String getTagValue(String tag, Element eElement) {
        NodeList nlist = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlist.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }

    String a, b, c;

    class Mythread1 extends Thread {

        String url;

        Mythread1(String url, int num) {
            this.url = url;
            threadNum = num;
        }

        @Override
        public void run() {
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            {
                try {
                    DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();

                    try {
                        Document doc = dBuilder.parse(url);
                        doc.getDocumentElement().normalize();
                        NodeList nList = doc.getElementsByTagName("list");

                        for (int temp = 0; temp < nList.getLength(); temp++) {
                            Node nNode = nList.item(temp);
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;
                                //System.out.println(eElement.getTextContent());
                                if (threadNum == 0) {

                                    MapActivity.information.add(new Information(0, getTagValue("dataTitle", eElement), getTagValue("designateDate", eElement),
                                            getTagValue("designateNo", eElement), getTagValue("posx", eElement), getTagValue("posy", eElement),
                                            getTagValue("dataSid", eElement), getTagValue("dataContent", eElement), getTagValue("addrDtl", eElement), getTagValue("introDataContent", eElement),
                                            "", "", "", "", ""));

                                    a = getTagValue("dataTitle", eElement);
                                    b = getTagValue("posx", eElement);
                                    c = getTagValue("posy", eElement);

                                    lat[allListindex] = Float.valueOf(b);
                                    lon[allListindex] = Float.valueOf(c);
                                    title[allListindex] = a;
                                    allListindex++;

                                } else if (threadNum == 1) {

                                    MapActivity.information.add(new Information(1, getTagValue("dataTitle", eElement), "", "",
                                            getTagValue("posx", eElement), getTagValue("posy", eElement),
                                            getTagValue("dataSid", eElement), getTagValue("dataContent", eElement), getTagValue("addr", eElement),
                                            "", getTagValue("touretcetera", eElement), "", "", "", ""));

                                    a = getTagValue("dataTitle", eElement);
                                    b = getTagValue("posx", eElement);
                                    c = getTagValue("posy", eElement);

                                    lat[allListindex] = Float.valueOf(b);
                                    lon[allListindex] = Float.valueOf(c);
                                    title[allListindex] = a;
                                    allListindex++;

                                } else if (threadNum == 2) {
                                    MapActivity.information.add(new Information(2, getTagValue("dataTitle", eElement), "",
                                            "", getTagValue("posx", eElement), getTagValue("posy", eElement),
                                            getTagValue("dataSid", eElement), getTagValue("dataContent", eElement), (getTagValue("addr", eElement) + getTagValue("addrDtl", eElement)), "",
                                            "", getTagValue("tel", eElement), getTagValue("homepage", eElement), getTagValue("zipcode", eElement), ""));

                                    a = getTagValue("dataTitle", eElement);
                                    b = getTagValue("posx", eElement);
                                    c = getTagValue("posy", eElement);

                                    lat[allListindex] = Float.valueOf(b);
                                    lon[allListindex] = Float.valueOf(c);
                                    title[allListindex] = a;
                                    allListindex++;

                                    Log.d("1", "1");
                                } else if (threadNum == 3) {
                                    MapActivity.information.add(new Information(3, getTagValue("dataTitle", eElement), "",
                                            "", getTagValue("posx", eElement), getTagValue("posy", eElement),
                                            getTagValue("dataSid", eElement), getTagValue("dataContent", eElement), (getTagValue("addr", eElement) + getTagValue("addrDtl", eElement)), "",
                                            "", getTagValue("tel", eElement), getTagValue("userHomepage", eElement), getTagValue("zipCode", eElement), getTagValue("introContent", eElement)));

                                    a = getTagValue("dataTitle", eElement);
                                    b = getTagValue("posx", eElement);
                                    c = getTagValue("posy", eElement);

                                    lat[allListindex] = Float.valueOf(b);
                                    lon[allListindex] = Float.valueOf(c);
                                    title[allListindex] = a;
                                    allListindex++;
                                }
                            }    // for end
                        }    // if end
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class UrlTaskinMain extends AsyncTask<Void, Void, Void> {

        String sumaddurl = "";
        String a0 = "http://openapi.jeonju.go.kr/rest/historic/getHistoricFile?authApiKey=DIFVRMOVXUMNJHY&dataSid=";
        String a1 = "http://openapi.jeonju.go.kr/rest/local/getLocalFile?authApiKey=VLughqV6eQYlGvBrrhckojS2eeoOYSsOdZpl9uKN5UpoXUovjlow0dFZ24sKBrljiORx%2F60AYubPwJs0bvicMA%3D%3D&dataSid=";
        String a2 = "http://openapi.jeonju.go.kr/rest/experience/getExperienceFile?authApiKey=VLughqV6eQYlGvBrrhckojS2eeoOYSsOdZpl9uKN5UpoXUovjlow0dFZ24sKBrljiORx%2F60AYubPwJs0bvicMA%3D%3D&dataSid=";
        String a3 = "http://openapi.jeonju.go.kr/rest/culture/getCultureFile?authApiKey=VLughqV6eQYlGvBrrhckojS2eeoOYSsOdZpl9uKN5UpoXUovjlow0dFZ24sKBrljiORx%2F60AYubPwJs0bvicMA%3D%3D&dataSid=";

        @Override
        protected Void doInBackground(Void... voids) {
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            {
                try {
                    DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
                    try {

                        for (int index = 0; index < MapActivity.information.size(); index++) {

                            if (MapActivity.information.get(index).getNum() == 0) {
                                sumaddurl = a0 + MapActivity.information.get(index).getDataSid();
                            } else if (MapActivity.information.get(index).getNum() == 1) {
                                sumaddurl = a1 + MapActivity.information.get(index).getDataSid();
                            } else if (MapActivity.information.get(index).getNum() == 2) {
                                sumaddurl = a2 + MapActivity.information.get(index).getDataSid();
                            } else {
                                sumaddurl = a3 + MapActivity.information.get(index).getDataSid();
                            }
                            Document doc = dBuilder.parse(sumaddurl);
                            doc.getDocumentElement().normalize();
                            NodeList nList = doc.getElementsByTagName("list");

                            for (int temp = 0; temp < nList.getLength(); temp++) {
                                Node nNode = nList.item(temp);
                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                                    Element eElement = (Element) nNode;
                                    MapActivity.information.get(index).setFileUrl(getTagValue("fileUrl", eElement));

                                }    // for end
                            }    // if end
                        }
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
            Log.d("urltask finished", "b");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingEnd();
        }
    }

    @Override
    public void onBackPressed() {
        // app 강제종료하기
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void nextActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void loading() {
        //로딩
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog = new ProgressDialog(MainActivity.this);
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