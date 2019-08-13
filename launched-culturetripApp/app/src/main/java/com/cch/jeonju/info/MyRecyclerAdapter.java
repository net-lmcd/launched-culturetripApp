package com.cch.jeonju.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.cch.jeonju.MapActivity;
import com.cch.jeonju.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private final List<Information> mdataList;
    private final Context mContext;
    public MyRecyclerAdapter(List<Information> dataList, Context mContext) {
        this.mdataList = dataList;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Information information = mdataList.get(position);
        holder.title.setText(information.getDataTitle());
        holder.contents.setText(information.getAddrDtl());

   /*     if(information.getBitmap() == null) {
            try {
                new ImageTaskRecycle().execute(position);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        Glide.with(mContext).load(information.getFileUrl()).into(holder.imageView);
       // holder.imageView.setImageBitmap(information.getBitmap());
    }

    @Override
    public int getItemCount() {
        return mdataList.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        TextView contents;

        // 생성자에 전체 layout에 해당하는 View가 들어옴
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recycler_title_text);
            contents = itemView.findViewById(R.id.recycler_content_text);
            imageView = itemView.findViewById(R.id.recycler_image_view);
        }
    }

    public class ImageTaskRecycle extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {

            Log.d("imageTask class started", "c");

            try {
                    URL url = new URL(MapActivity.information.get(integers[0]).getFileUrl());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    MapActivity.information.get(integers[0]).setBitmap(bitmap);
                    Log.d("Result of bitmap : ", bitmap.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}

