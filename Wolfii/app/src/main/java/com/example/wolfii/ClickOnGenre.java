package com.example.wolfii;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Wolfii;

import java.util.ArrayList;
import java.util.List;

import static com.example.wolfii.MainActivity.database;
import static com.example.wolfii.MainActivity.mService;
import static com.example.wolfii.MainActivity.mesMusiques;
import static com.example.wolfii.MusiqueService.maMusique;

public class ClickOnGenre implements MyStringAdapter.ArtisteItemClickListener {

    private MyMusiqueAdapter monMusiqueAdapter;
    private ArrayList<Musique> musiques = new ArrayList<> ();
    private RecyclerView mRecyclerView;
    private Context context;
    private ImageView shuffleiv;
    private List<String> hiddenTitle = database.mainDao ().getHiddenTitle ();

    public void setRecyclerViewForMusic(RecyclerView rv) { mRecyclerView = rv; }
    public void setContext(Context sContext){context = sContext;}
    public void setShuffle(ImageView shuffle) {this.shuffleiv = shuffle;}

    @SuppressLint("WrongConstant")
    @Override
    public void onArtisteItemClick (View view, String genre, int position) {
        if(genre.equals ("Download")) {
           musiques = HideMusic (mesMusiques);
        }
        else if (genre.equals ("Liked Music")) {
            for(String path : database.mainDao ().getLikes ())
                for(Musique musique : mesMusiques)
                    if(musique.getPath ().equals (path)
                            && !hiddenTitle.contains (musique.getPath ()))
                        musiques.add(musique);
        }
        else if (genre.equals ("Hidden Music")) {
            for(String path : database.mainDao ().getHiddenTitle ())
                for(Musique musique : mesMusiques)
                    if(musique.getPath ().equals (path)) {
                        musiques.add(musique);
                    }
        }
        else {
            musiques = HideMusic(recuperer_musique (genre));
        }
        // on affiche le bouton shuffle
        shuffleiv.setVisibility (View.VISIBLE);
        ClickOnShuffle shuffle = new ClickOnShuffle ();
        shuffle.setContext (context);
        shuffle.setmRecyclerView (mRecyclerView);
        shuffle.setPlaylist (musiques);
        shuffleiv.setOnClickListener (shuffle);


        monMusiqueAdapter = new MyMusiqueAdapter (musiques, context);
        ClickOnMusic clicker = new ClickOnMusic();
        clicker.setMesMusiques (musiques);
        clicker.setContext (context);
        monMusiqueAdapter.setmMusiqueItemClickListener (clicker);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (context.getApplicationContext(), LinearLayout.VERTICAL, false));
        mRecyclerView.setAdapter (monMusiqueAdapter);
    }

    ArrayList<Musique> HideMusic(ArrayList<Musique> musiques){
        ArrayList<Musique> trueList = new ArrayList<Musique> ();
        for(Musique m : musiques)
            if(!hiddenTitle.contains (m.getPath ()))
                trueList.add(m);
        return trueList;
    }

    @Override
    public void onArtisteItemLongClick (View view, String genre, int position) {
        if(genre.equals ("Download")) {
            musiques = HideMusic (mesMusiques);
        }
        else if (genre.equals ("Liked Music")) {
            for(String path : database.mainDao ().getLikes ())
                for(Musique musique : mesMusiques)
                    if(musique.getPath ().equals (path)
                            && !hiddenTitle.contains (musique.getPath ()))
                        musiques.add(musique);
        }
        else if (genre.equals ("Hidden Music")) {
            for(String path : database.mainDao ().getHiddenTitle ())
                for(Musique musique : mesMusiques)
                    if(musique.getPath ().equals (path))
                        musiques.add(musique);
        }
        else {
            musiques = HideMusic(recuperer_musique (genre));
        }

        Dialog dialog = new Dialog(context);

        // set content view
        dialog.setContentView(R.layout.ajouter_a_une_playlist);

        // initialize width and height
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        //set layout
        dialog.getWindow().setLayout(width, height);
        dialog.show ();

        EditText editText = dialog.findViewById (R.id.nom_playlist);
        Button addToPlaylist = dialog.findViewById (R.id.add);
        Button addToCurrentPlaylist = dialog.findViewById (R.id.addToCurrentPlaylist);

        addToPlaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                for(Musique musique : musiques) {
                    DataMusique dataMusique = new DataMusique ();
                    dataMusique.setNomMusique (musique.getName ());
                    dataMusique.setPath (musique.getPath ());
                    dataMusique.setAuthor (musique.getAuthor ());
                    dataMusique.setDuration (musique.getDuration ());
                    dataMusique.setDateTaken (musique.getDateTaken ());
                    dataMusique.setGenre (musique.getGenre ());

                    DataPlaylist dataPlaylist = new DataPlaylist ();
                    dataPlaylist.setNom (editText.getText ().toString ());

                    database.mainDao ().insertMusic (dataMusique);
                    database.mainDao ().insertPlaylist (dataPlaylist);
                }
                dialog.dismiss ();
            }
        });

        addToCurrentPlaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                for (Musique musique : musiques) {
                    if (! maMusique.isEmpty ()) {
                        maMusique.add (musique);
                    } else {
                        Toast.makeText (Wolfii.getAppContext (), "Lecture de : " + musique.getName (), Toast.LENGTH_SHORT).show ();

                        ArrayList<Musique> musiqueArray = new ArrayList<> ();
                        musiqueArray.add (musique);
                        mService.setMusiquePlaylist (musiqueArray, 0);
                        mService.arretSimpleMusique ();
                        mService.musiqueDemaPause ();
                    }

                }
            }
        });
    }
    private ArrayList<Musique> recuperer_musique (String genre) {
        // on recupere toutes les musiques selon l'artiste qui nous interesse
        ArrayList<Musique> musiques = new ArrayList<> ();
        for (Musique m : mesMusiques) if (m.getGenre ().equals (genre)) musiques.add (m);
        return musiques;
    }
}