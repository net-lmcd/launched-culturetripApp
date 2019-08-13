package com.cch.jeonju.Noticeboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cch.jeonju.R;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private List<Board> mBoardList;
    private Context mContext;
    public BoardAdapter(List<Board> mBoardList, Context mContext) {
        this.mBoardList = mBoardList;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BoardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Board board = mBoardList.get(position);
        holder.mTitleTextVIew.setText("제목 : " +board.getTitle());
        holder.mNameTextView.setText(board.getName());
        holder.mContentsTextView.setText("내용 : "+board.getContents());
        Glide.with(mContext).load(board.getUri()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mBoardList.size();
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextVIew;
        private TextView mNameTextView;
        private TextView mContentsTextView;
        private ImageView mImageView;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextVIew = itemView.findViewById(R.id.board_title_text);
            mNameTextView = itemView.findViewById(R.id.board_name_text);
            mImageView = itemView.findViewById(R.id.board_image_view);
            mContentsTextView = itemView.findViewById(R.id.board_contents_text);
        }
    }
}
