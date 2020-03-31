package com.example.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class RVAdapter extends RecyclerView.Adapter<RVAdapter.PhotoHolder> {

    private List<File> files;

    RVAdapter(List<File> files) {
        this.files = files;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_holder, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoHolder holder, final int position) {
        final File file = files.get(position);
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        holder.iv.setImageBitmap(myBitmap);
        holder.iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.bin.setVisibility(View.VISIBLE);
                holder.undo.setVisibility(View.VISIBLE);
                holder.tint.setVisibility(View.VISIBLE);
                return false;
            }
        });
        holder.bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position < files.size()) {
                    file.delete();
                    files.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        holder.undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.bin.setVisibility(View.GONE);
                holder.undo.setVisibility(View.GONE);
                holder.tint.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    void addFile(File file) {
        if (files == null){
            files = new ArrayList<File>();
        }
        files.add(file);
        notifyDataSetChanged();
    }

    class PhotoHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        ImageView bin;
        ImageView undo;
        RelativeLayout tint;

        PhotoHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_holder);
            bin = itemView.findViewById(R.id.iv_bin);
            undo = itemView.findViewById(R.id.iv_undo);
            tint = itemView.findViewById(R.id.iv_tinted);
        }
    }
}
