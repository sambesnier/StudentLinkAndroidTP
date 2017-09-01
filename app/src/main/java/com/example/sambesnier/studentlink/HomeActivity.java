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
import android.widget.LinearLayout;
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

public class HomeActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = getIntent().getStringExtra("username");

        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        Typeface lobster = Typeface.createFromAsset( getAssets(), "Lobster.otf" );

        TextView button = (TextView) findViewById(R.id.iconList);
        TextView button2 = (TextView) findViewById(R.id.iconNew);
        TextView button3 = (TextView) findViewById(R.id.iconAccount);
        TextView button4 = (TextView) findViewById(R.id.iconPref);
        TextView title = (TextView) findViewById(R.id.helloName);
        button.setTypeface(font);
        button2.setTypeface(font);
        button3.setTypeface(font);
        button4.setTypeface(font);
        title.setTypeface(lobster);
        title.setText("Bonjour " + username);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        addButtonListener();
    }

    public void addButtonListener() {
        LinearLayout btnNew = (LinearLayout) findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewVoteActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(intent);
            }
        });

        LinearLayout btnList = (LinearLayout) findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListVoteActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        LinearLayout btnAccount = (LinearLayout) findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        LinearLayout btnPref = (LinearLayout) findViewById(R.id.btnPref);
        btnPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ParameterActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final RequestQueue queue = Volley.newRequestQueue(this);

        switch (item.getItemId()) {
            case R.id.action_account:
                Intent intent = new Intent(HomeActivity.this, ParameterActivity.class);
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
                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
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
