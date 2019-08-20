package com.example.mooicab1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText ph_no;
    TextView for_reg;
    String fetchedPhone;
    Button login;
    ProgressDialog progressDialog;
    Boolean LOGIN=false;
    String logUrl="https://easytute.in/mooicab/login.php";
    List<List_Data> list_data;
    String login_status="No";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //fetch login data
        list_data=new ArrayList<>();
        //initialize progress dialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(true);


        login=(Button)findViewById(R.id.login);

        ph_no=(EditText)findViewById(R.id.ph);
        for_reg=(TextView) findViewById(R.id.for_reg);
        for_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j=new Intent(getApplicationContext(),Registration.class);
                startActivity(j);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ph_no.getText().toString().trim().equals("")){
                    ph_no.setError("Please Enter Your Phone number");
                    ph_no.requestFocus();
                }else {
                    loguse l=new loguse();
                    new Thread(l).start();
                    progressDialog.show();
                }
            }
        });
    }
    class loguse implements Runnable {

        @Override
        public void run() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, logUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject ob = array.getJSONObject(i);
                            final List_Data listData = new List_Data(ob.getString("ph_no"));
                            list_data.add(listData);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                    fetchedPhone = listData.getphone();
                                    if (fetchedPhone.equals(ph_no.getText().toString().trim())) {
                                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                                        SharedPreferences sp = getSharedPreferences(login_status, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("loginStatus", "Yes");
                                        editor.apply();
                                        progressDialog.hide();
                                        Intent i = new Intent(Login.this, MapsActivity.class);
                                        startActivity(i);
                                    } else if (!LOGIN) {
                                        Toast.makeText(getApplicationContext(), "Wrong Phone number", Toast.LENGTH_LONG).show();

                                        progressDialog.hide();
                                    }

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Some error occurred!", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ph_no", ph_no.getText().toString());  //name should be same as database

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}