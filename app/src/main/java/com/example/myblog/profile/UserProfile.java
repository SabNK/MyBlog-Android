package com.example.myblog.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myblog.MainActivity;
import com.example.myblog.R;
import com.example.myblog.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {
    private String id, name, email, created_date;
    private String appUrl;
    TextView mId,mName,mEmail,mDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mId = findViewById(R.id.txt_Id);
        mName = findViewById(R.id.txt_Name);
        mEmail = findViewById(R.id.txt_Email);
        mDate = findViewById(R.id.txt_Data);

        Intent data = getIntent();
        email = data.getStringExtra("email");
        appUrl = "http://192.168.31.149/api/getUserDetails.php?email="+email;

        getUserDetail();
    }
    private void getUserDetail(){
        if (onErrorDialog (email.isEmpty(), R.string.email_empty)){
            return;
        }
        else{
            StringRequest stringRequest = new StringRequest(Request.Method.GET,appUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject  jsonObject = new JSONObject(response);
                        id = jsonObject.getString("id");
                        name = jsonObject.getString("name");
                        email = jsonObject.getString("email");
                        created_date = jsonObject.getString("created_date");

                        mId.setText(id);
                        mName.setText(name);
                        mEmail.setText(email);
                        mDate.setText(created_date);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder alert;
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null){
                        switch (response.statusCode){
                            case 400:
                                onErrorDialog(true, R.string.error_400);
                                break;
                            case 403:
                                onErrorDialog(true, R.string.error_403);
                                break;
                            case 404:
                                onErrorDialog(true, R.string.error_404);
                                break;
                        }
                    }
                    else{
                        onErrorDialog(true, error.toString());
                    }
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("Accept","Application/json; charset=UTF-8");
                    return params;
                }


            };
            VolleySingleton.getInstance().addRequestQueue(stringRequest);
        }

    }
    private boolean onErrorDialog (boolean condition, int idRString) {
        if (condition) {
            AlertDialog.Builder alert = new AlertDialog.Builder(UserProfile.this);
            alert.setMessage(idRString);
            alert.setCancelable(false);
            alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
            return true;
        }
        return false;
    }

    private boolean onErrorDialog (boolean condition, String message) {
        if (condition) {
            AlertDialog.Builder alert = new AlertDialog.Builder(UserProfile.this);
            alert.setMessage(message);
            alert.setCancelable(false);
            alert.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
            return true;
        }
        return false;
    }

}
