package com.example.mooicab1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Registration extends AppCompatActivity {
    Button reg;
    EditText fn,ph,email,city;
    ProgressDialog progressDialog;
    List<List_Data_cp> list_data_cp;
    String login_status="No";

    //String url2="https://easytute.in/mooicab/reg.php";
    String url1="https://easytute.in/mooicab/reg.php";
    int f;
    String tac="NA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        list_data_cp=new ArrayList<>();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(true);

        fn=(EditText)findViewById(R.id.fn);
        ph=(EditText)findViewById(R.id.ph);
        email=(EditText)findViewById(R.id.email);
        city=(EditText)findViewById(R.id.city);

        reg=(Button)findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fn.getText().toString().trim().equals("")){
                    fn.setError("Please Enter Your Name");
                    fn.requestFocus();
                }else if (ph.getText().toString().trim().equals("")){
                    ph.setError("Please Enter Your Phone Number");
                    ph.requestFocus();
                }else if (email.getText().toString().trim().equals("")){
                    email.setError("Please Enter Your Email Id");
                    email.requestFocus();
                }else if (city.getText().toString().trim().equals("")){
                    city.setError("Please Enter Your Pin Code");
                    city.requestFocus();
                }else {
                    progressDialog.show();
                    reg cp=new reg();
                    new Thread(cp).start();
                }
            }
        });
    }
   /* class check_ph implements Runnable{
        @Override
        public void run() {
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject ob = array.getJSONObject(i);
                            List_Data_cp listDatacp = new List_Data_cp(
                                    ob.getString("ph_no"));
                            list_data_cp.add(listDatacp);
                            String[] PhNo=new String[array.length()];
                            PhNo[i]=listDatacp.getPhone();

                            if (PhNo[i].equals(ph.getText().toString().trim())){
                                f=1;
                            }else {
                                f=0;
                            }

                        }
                        if (f==1){
                            shoeDialog();
                            progressDialog.dismiss();
                        }else {
                            reg r=new reg();
                            new Thread(r).start();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Some error occurred!", Toast.LENGTH_SHORT).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Some error occurred!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });
            RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);


        }
    }
    public void shoeDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Message");

        dialog.setMessage("This number is already registered!");

        dialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {


                finish();

            }

        });

        dialog.setCancelable(true);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }

        }).show();
    }*/
    class reg implements Runnable{
        @Override
        public void run() {
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(getApplicationContext(),"Registered Successful!",Toast.LENGTH_SHORT).show();

                            SharedPreferences sp=getSharedPreferences(login_status, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putString("loginStatus","Yes");
                            editor.apply();


                            progressDialog.dismiss();

                            Intent i=new Intent(getApplicationContext(),MapsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);


                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Some error occurred!",Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                    progressDialog.dismiss();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<String, String>();
                    params.put("fn",fn.getText().toString().trim());
                    params.put("ph_no",ph.getText().toString().trim());
                    params.put("email",email.getText().toString().trim());
                    params.put("city",city.getText().toString().trim());

                    return params;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }
    }
}
