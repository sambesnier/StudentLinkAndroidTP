package com.example.sambesnier.studentlink;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sambesnier.studentlink.adapters.VoteAdapter;
import com.example.sambesnier.studentlink.models.Vote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        getVotes();

        /*List<Vote> votes = genererVotes();

        VoteAdapter adapter = new VoteAdapter(ListVoteActivity.this, votes);
        mListView.setAdapter(adapter);*/
    }

    private void getVotes() {
        final RequestQueue queue = Volley.newRequestQueue(this);

        EditText question = (EditText) findViewById(R.id.questionEdit);

        String url ="http://10.0.2.2:8080/studentlink/votes";

        JsonObjectRequest stringRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<Vote> votes = new ArrayList<Vote>();
                        try {
                            JSONArray votesObj = response.getJSONArray("votes");
                            for (int i = 0; i < votesObj.length(); i++) {
                                votes.add(new Vote(votesObj.getJSONObject(i).getString("username"), votesObj.getJSONObject(i).getString("question")));
                            }
                            VoteAdapter adapter = new VoteAdapter(ListVoteActivity.this, votes);
                            mListView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des votes", Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(stringRequest);
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
        votes.add(new Vote("sam", "Que c'est beau..."));
        votes.add(new Vote("Adrien", "Il est quelle heure ??"));
        votes.add(new Vote("Clément", "On y est presque"));
        return votes;
    }
}
