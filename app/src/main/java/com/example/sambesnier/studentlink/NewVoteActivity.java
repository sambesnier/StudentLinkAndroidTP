package com.example.sambesnier.studentlink;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NewVoteActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vote);

        username = getIntent().getStringExtra("username");

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

        String url ="http://10.0.2.2:8081/studentlink/votes";

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", getIntent().getStringExtra("username"));
        params.put("question", question.getText().toString());

        JSONObject jsonObj = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(NewVoteActivity.this, VoteActivity.class);
                        intent.putExtra("username", username);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final RequestQueue queue = Volley.newRequestQueue(this);

        switch (item.getItemId()) {
            case R.id.action_account:
                Intent intent = new Intent(NewVoteActivity.this, ParameterActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_logout:
                String url ="http://10.0.2.2:8081/studentlink/users/logout";

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);

                JSONObject jsonObj = new JSONObject(params);

                JsonObjectRequest stringRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                File file = new File("studentlink.token");
                                file.delete();
                                Intent intent = new Intent(NewVoteActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                queue.add(stringRequest);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
