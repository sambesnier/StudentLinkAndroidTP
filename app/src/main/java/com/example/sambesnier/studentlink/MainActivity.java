package com.example.sambesnier.studentlink;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getToken();

        addButtonListener();
    }

    public void addButtonListener() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Button btnSignin = (Button) findViewById(R.id.signinBtn);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = getApplicationContext();
                final EditText editUsername = (EditText) findViewById(R.id.usernameEdit);
                final EditText editEmail = (EditText) findViewById(R.id.emailEdit);
                final EditText editPassword = (EditText) findViewById(R.id.passwordEdit);

                String url ="http://10.0.2.2:8081/studentlink/users";

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", editUsername.getText().toString());
                params.put("email", editEmail.getText().toString());
                params.put("password", editPassword.getText().toString());

                JSONObject jsonObj = new JSONObject(params);

                JsonObjectRequest stringRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(context, "Vous êtes inscrit", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "Erreur lors de l'inscription", Toast.LENGTH_LONG).show();
                            }
                        });

                queue.add(stringRequest);
            }
        });
    }

    public void getToken() {
        //reading text from file
        try {
            FileInputStream fileIn=openFileInput("studentlink.token");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            JSONObject obj = new JSONObject(s);
            String token = obj.get("token").toString();
            String username = obj.get("username").toString();

            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("token", token);

            String url ="http://10.0.2.2:8081/studentlink/users/tokens";

            sendPostRequest(params, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPostRequest(Map<String, String> params, String url) {

        final RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonObj = new JSONObject(params);

        JsonObjectRequest stringRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        FileOutputStream fileout = null;
                        try {
                            fileout = openFileOutput("studentlink.token", MODE_PRIVATE);
                            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                            outputWriter.write(response.toString());
                            outputWriter.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        try {
                            intent.putExtra("username", response.getString("username"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(stringRequest);
    }
}
