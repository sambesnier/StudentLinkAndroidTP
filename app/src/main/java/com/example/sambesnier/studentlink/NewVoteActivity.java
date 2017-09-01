package com.example.sambesnier.studentlink;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewVoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vote);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Typeface lobster = Typeface.createFromAsset( getAssets(), "Lobster.otf" );
        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );

        TextView title = (TextView) findViewById(R.id.titleQuestion);
        TextView icon = (TextView) findViewById(R.id.iconQuestion);
        title.setTypeface(lobster);
        icon.setTypeface(font);

        addButtonListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void addButtonListener() {
        final Button submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitQuestion();
            }
        });
    }

    private void submitQuestion() {
        final RequestQueue queue = Volley.newRequestQueue(this);

        EditText question = (EditText) findViewById(R.id.questionEdit);

        String url ="http://10.0.2.2:8080/studentlink/votes";

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "sam");
        params.put("question", question.getText().toString());

        JSONObject jsonObj = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(NewVoteActivity.this, VoteActivity.class);
                        intent.putExtra("user", "sam");
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Erreur lors de l'inscription", Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(stringRequest);
    }
}
