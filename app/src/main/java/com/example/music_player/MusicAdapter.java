package com.example.music_player;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyVieHolder> {

   private Context nContext;
   private ArrayList<MusicFiles> nFiles;

   MusicAdapter(Context nContext, ArrayList<MusicFiles> nFiles){
       this.nFiles = nFiles;
       this.nContext = nContext;
   }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(nContext).inflate(R.layout.music_items, parent, false);
        return new MyVieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, int position) {
       MusicFiles model = nFiles.get(position);
        holder.file_name.setText(nFiles.get(position).getTitle());
        byte[] image = getAlbumArt(model.getPath());
        Log.d("File_Path", "onBindViewHolder: "+image);

        if (image != null){
            Glide.with(nContext).asBitmap().load(image).into(holder.album_art);
        }
        else{
            Glide.with(nContext).load(R.drawable.music).into(holder.album_art);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(nContext, PlayerActivity.class);
            intent.putExtra("position", position);
            nContext.startActivity(intent);
        });
        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(nContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener((item) -> {
                    switch (item.getItemId()){
                        case R.id.delete:
                            deleteFile(position , v);
                            break;
                    }
                    return true;
                });
            }
        });
    }

    private void deleteFile(int position, View v) {
       Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
               Long.parseLong(nFiles.get(position).getId()));

       File file = new File(nFiles.get(position).getPath());
       boolean deleted = file.delete();
       if(deleted){
           nContext.getContentResolver().delete(contentUri, null , null);
           nFiles.remove(position);
           notifyItemRemoved(position);
           notifyItemRangeChanged(position, nFiles.size());
           Snackbar.make(v , "File Deleted!", Snackbar.LENGTH_LONG).show();
        }
       else{
           Snackbar.make(v , "Can't Delete File!", Snackbar.LENGTH_LONG).show();
       }
   }

    @Override
    public int getItemCount() {
        return nFiles.size();
    }


    public class MyVieHolder extends RecyclerView.ViewHolder{
        TextView file_name;
        ImageView album_art, menuMore;
        public MyVieHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            menuMore = itemView.findViewById(R.id.menuMore);
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
