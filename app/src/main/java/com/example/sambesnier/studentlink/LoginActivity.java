package com.example.sambesnier.studentlink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addButtonListener();
    }

    public void addButtonListener() {
        final RequestQueue queue = Volley.newRequestQueue(this);

        final Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = getApplicationContext();
                final EditText editUsername = (EditText) findViewById(R.id.usernameEdit);
                final EditText editPassword = (EditText) findViewById(R.id.passwordEdit);

                String url ="http://10.0.2.2:8081/studentlink/users/login";

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", editUsername.getText().toString());
                params.put("password", editPassword.getText().toString());

                JSONObject jsonObj = new JSONObject(params);

                JsonObjectRequest stringRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                FileOutputStream fileout= null;
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
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                try {
                                    intent.putExtra("username", response.getString("username"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                ProgressDialog nDialog;
                                nDialog = new ProgressDialog(LoginActivity.this);
                                nDialog.setMessage("Chargement...");
                                nDialog.setTitle("Connexion");
                                nDialog.setIndeterminate(false);
                                nDialog.setCancelable(true);
                                nDialog.show();

                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "Mauvais mot de passe/email", Toast.LENGTH_LONG).show();
                            }
                        });

                queue.add(stringRequest);
            }
        });
    }
}
