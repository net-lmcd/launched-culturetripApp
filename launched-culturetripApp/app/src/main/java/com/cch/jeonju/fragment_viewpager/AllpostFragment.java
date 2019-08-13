package com.cch.jeonju.fragment_viewpager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AllpostFragment extends Fragment {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private RecyclerView mMypostfragmentRecyclerview;

    private BoardAdapter boardAdapter;
    private List<Board> mBoardList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_allpostfragment, container, false);

        mMypostfragmentRecyclerview = rootView.findViewById(R.id.recycler_allpostfragment);

        mBoardList = new ArrayList<>();

        // 실시간으로 데이터를 싹 다가져오는거
        mStore.collection("board")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                        for(DocumentChange document : queryDocumentSnapshots.getDocumentChanges()) {
                            String userEmail = (String) document.getDocument().getData().get("useremail");
                            String title = (String) document.getDocument().getData().get("title");
                            String contents = (String) document.getDocument().getData().get("contents");
                            String name = (String) document.getDocument().getData().get("name");
                            String uri = (String) document.getDocument().getData().get("uri");
                            Board board = new Board(userEmail, title, contents, name, uri);
                            mBoardList.add(board);
                        }
                        boardAdapter = new BoardAdapter(mBoardList, getActivity());
                        mMypostfragmentRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mMypostfragmentRecyclerview.setAdapter(boardAdapter);
                    }
                });

        return rootView;
    }
}
