package com.example.sambesnier.studentlink;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

    String user = "Superman";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Typeface lobster = Typeface.createFromAsset( getAssets(), "Lobster.otf" );
        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );

        TextView yes = (TextView) findViewById(R.id.iconBtnYes);
        TextView no = (TextView) findViewById(R.id.iconBtnNo);
        TextView blur = (TextView) findViewById(R.id.iconBtnBlur);
        question = (TextView) findViewById(R.id.question);
        question.setTypeface(lobster);
        yes.setTypeface(font);
        no.setTypeface(font);
        blur.setTypeface(font);

        addButtonListener();

        client = new OkHttpClient();

        start();
    }

    private void start() {
        Request request = new Request.Builder().url("ws://192.168.1.97:8080/StudentLink/vote/sam").build();
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

    public void addButtonListener() {
        final LinearLayout yesBtn = (LinearLayout) findViewById(R.id.btnYes);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("header", 10);
                    json.put("user", user);
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
                    json.put("user", user);
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
                    json.put("user", user);
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
                json.put("user", user);
                webSocket.send(json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
                final JSONObject json = new JSONObject(text);
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
    }
}
