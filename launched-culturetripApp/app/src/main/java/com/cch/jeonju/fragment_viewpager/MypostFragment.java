package com.cch.jeonju.fragment_viewpager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cch.jeonju.BoardWriteActivity;
import com.cch.jeonju.Noticeboard.Board;
import com.cch.jeonju.Noticeboard.BoardAdapter;
import com.cch.jeonju.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MypostFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "MypostFragment";
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private RecyclerView mMypostfragmentRecyclerview;
    private FloatingActionButton mFloatingActionButton;

    private BoardAdapter boardAdapter;
    public List<Board> mBoardList; //

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_mypostfragment, container, false);

        mMypostfragmentRecyclerview = rootView.findViewById(R.id.recycler_mypostfragment);

        mFloatingActionButton = rootView.findViewById(R.id.write_floatingbutton_mypostfragment);
        mFloatingActionButton.setOnClickListener(this);

       // mFloatingActionButton = getActivity().findViewById(R.id.write_floatingbutton_mypostfragment);
       // mMypostfragmentRecyclerview = getActivity().findViewById(R.id.recycler_mypostfragment); 잘못됨

        mBoardList = new ArrayList<>();

        putData();

        // 실시간으로 데이터를 싹 다가져오는거

        return rootView;
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), BoardWriteActivity.class));
    }

    public void putData() {
        mStore.collection("board")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        String currentUseremail = user.getEmail();
                        for(DocumentChange document : queryDocumentSnapshots.getDocumentChanges()) {

                            String userEmail = (String) document.getDocument().getData().get("useremail");
                            if(currentUseremail != null) {
                                if(currentUseremail.equals(userEmail)) {
                                    String title = (String) document.getDocument().getData().get("title");
                                    String contents = (String) document.getDocument().getData().get("contents");
                                    String name = (String) document.getDocument().getData().get("name");
                                    String uri = (String) document.getDocument().getData().get("uri");
                                    Board board = new Board(userEmail, title, contents, name, uri);
                                    mBoardList.add(board);
                                }
                            }
                        }
                        boardAdapter = new BoardAdapter(mBoardList, getActivity());
                        mMypostfragmentRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mMypostfragmentRecyclerview.setAdapter(boardAdapter);
                    }
                });
    }
}
