package com.example.music_player;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.music_player.MainActivity.albums;
import static com.example.music_player.MainActivity.musicFiles;

public class AlbumsFragment extends Fragment {
    RecyclerView recyclerView;
//    MusicAdapter musicAdapter;
    AlbumAdapter albumAdapter;
    public AlbumsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        albumAdapter = new AlbumAdapter(getContext(), albums);
        recyclerView.setAdapter(albumAdapter);


        return view;
    }
}