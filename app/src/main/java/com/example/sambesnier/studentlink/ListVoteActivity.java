package com.example.sambesnier.studentlink;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sambesnier.studentlink.adapters.VoteAdapter;
import com.example.sambesnier.studentlink.models.Vote;

import java.util.ArrayList;
import java.util.List;

public class ListVoteActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vote);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Typeface lobster = Typeface.createFromAsset( getAssets(), "Lobster.otf" );

        TextView title = (TextView) findViewById(R.id.titleVotes);
        title.setTypeface(lobster);

        mListView = (ListView) findViewById(R.id.listVotes);

        List<Vote> votes = genererVotes();

        VoteAdapter adapter = new VoteAdapter(ListVoteActivity.this, votes);
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private List<Vote> genererVotes(){
        List<Vote> votes = new ArrayList<Vote>();
        votes.add(new Vote("Florent", "Mon premier tweet !"));
        votes.add(new Vote("Kevin", "C'est ici que ça se passe !"));
        votes.add(new Vote("Logan", "Que c'est beau..."));
        votes.add(new Vote("Mathieu", "Il est quelle heure ??"));
        votes.add(new Vote("Willy", "On y est presque"));
        votes.add(new Vote("Romain", "Mon premier tweet !"));
        votes.add(new Vote("Jeremy", "C'est ici que ça se passe !"));
        votes.add(new Vote("Sam", "Que c'est beau..."));
        votes.add(new Vote("Adrien", "Il est quelle heure ??"));
        votes.add(new Vote("Clément", "On y est presque"));
        return votes;
    }
}
