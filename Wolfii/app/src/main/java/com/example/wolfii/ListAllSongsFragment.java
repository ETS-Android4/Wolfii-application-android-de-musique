package com.example.wolfii;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ListAllSongsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Musique> maMusique;
    private MyMusiqueAdapter monAdapter;



    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listallsongs, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.myRecyclerView);
        maMusique = MainActivity.mesMusiques; // on recupere ici toutes les musiques sous forme d'un tableau

        monAdapter = new MyMusiqueAdapter (maMusique, getActivity ());
        ClickOnMusic clickOnMusic = new ClickOnMusic ();
        clickOnMusic.setMesMusiques (maMusique);
        clickOnMusic.setContext (getActivity ());
        monAdapter.setmMusiqueItemClickListener(clickOnMusic);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayout.VERTICAL, false));
        mRecyclerView.setAdapter(monAdapter);

        return root;
    }
}