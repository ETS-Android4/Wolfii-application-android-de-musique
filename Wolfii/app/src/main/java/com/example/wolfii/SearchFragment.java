package com.example.wolfii;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        fragmentManager = getActivity ().getSupportFragmentManager ();

        Button button = root.findViewById (R.id.bt_search);
        EditText editText = root.findViewById (R.id.musique);
        ClickOnSearch clickOnSearch = new ClickOnSearch ();
        clickOnSearch.setSearch (editText.getText ().toString ());
        button.setOnClickListener(clickOnSearch);
        return root;
    }
    private class ClickOnSearch implements OnClickListener {

        private String search;

        private void setSearch(String search){this.search = search;}

        @Override
        public void onClick (View v) {
            ArrayList<Musique> musiques = rechercher (search);
            Fragment fragment = new ListSearchFragment (musiques, search);
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.result_search, fragment, "search");
            fragmentTransaction.commit ();
        }
    }

    public ArrayList<Musique> rechercher(String search) {
        ArrayList<Musique> musiques = new ArrayList<> ();
        for(Musique m : MainActivity.maMusique) if(m.getName ().contains (search) || m.getAuthor ().contains (search)) musiques.add(m);
        return musiques;
    }
}
