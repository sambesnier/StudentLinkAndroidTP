package com.example.sambesnier.studentlink;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class VoteActivity extends AppCompatActivity {

    private OkHttpClient client;
    WebSocket ws;
    TextView question;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Typeface lobster = Typeface.createFromAsset( getAssets(), "Lobster.otf" );
        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );

        TextView yesBtn = (TextView) findViewById(R.id.iconBtnYes);
        TextView noBtn = (TextView) findViewById(R.id.iconBtnNo);
        TextView blurBtn = (TextView) findViewById(R.id.iconBtnBlur);

        TextView votants = (TextView) findViewById(R.id.votants);
        TextView yes = (TextView) findViewById(R.id.yesResp);
        TextView no = (TextView) findViewById(R.id.noResp);
        TextView blur = (TextView) findViewById(R.id.blurResp);

        question = (TextView) findViewById(R.id.question);
        question.setTypeface(lobster);
        yesBtn.setTypeface(font);
        noBtn.setTypeface(font);
        blurBtn.setTypeface(font);

        votants.setTypeface(lobster);
        yes.setTypeface(lobster);
        no.setTypeface(lobster);
        blur.setTypeface(lobster);

        addButtonListener();

        client = new OkHttpClient();

        start();
    }

    private void start() {
        username = getIntent().getStringExtra("username");
        Request request = new Request.Builder().url("ws://10.0.2.2:8080/studentlink/vote/" + username).build();
        VoteWebSocketListener listener = new VoteWebSocketListener();
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
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
                Intent intent = new Intent(VoteActivity.this, ParameterActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_logout:
                String url ="http://10.0.2.2:8081/studentlink/users/logout";

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);

                JSONObject jsonObj = new JSONObject(params);

                JsonObjectRequest stringRequest = new JsonObjectRequest
                        (com.android.volley.Request.Method.POST, url, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                File file = new File("studentlink.token");
                                file.delete();
                                Intent intent = new Intent(VoteActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, new com.android.volley.Response.ErrorListener() {
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

    public void addButtonListener() {
        final LinearLayout yesBtn = (LinearLayout) findViewById(R.id.btnYes);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("header", 10);
                    json.put("user", username);
                    ws.send(json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        final LinearLayout noBtn = (LinearLayout) findViewById(R.id.btnNo);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("header", 20);
                    json.put("user", username);
                    ws.send(json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        final LinearLayout blurBtn = (LinearLayout) findViewById(R.id.btnBlur);
        blurBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("header", 30);
                    json.put("user", username);
                    ws.send(json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final class VoteWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            JSONObject json = new JSONObject();
            try {
                json.put("header", 1);
                json.put("user", username);
                webSocket.send(json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {

            final JSONObject json;
            try {
                json = new JSONObject(text);
                traitement(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {

        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {

        }

        public void traitement(final JSONObject json) throws JSONException {
            switch (json.getInt("header")) {
                case 1:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                question.setText(json.getString("question"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case 3:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                TextView votants = (TextView) findViewById(R.id.votants);
                                TextView yes = (TextView) findViewById(R.id.yesResp);
                                TextView no = (TextView) findViewById(R.id.noResp);
                                TextView blur = (TextView) findViewById(R.id.blurResp);

                                votants.setText("Votants : " + json.getString("votants"));
                                yes.setText("Oui : " + json.getString("yes"));
                                no.setText("Non : " + json.getString("no"));
                                blur.setText("Flou : " + json.getString("blur"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
            }
        }
    }
}
