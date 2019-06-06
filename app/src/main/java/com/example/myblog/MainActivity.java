package com.example.myblog;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myblog.auth.SignUp;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myblog.volley.VolleySingleton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.example.myblog.profile.UserProfile;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String appUrl;
    private String email;
    private String password;
    MaterialEditText mEmail;
    MaterialEditText mPassword;
    TextView mCreateAccount;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appUrl = "http://192.168.31.149/api/logIn.php";
        Toast.makeText(this, "Вау?", Toast.LENGTH_SHORT).show();
        mEmail = findViewById(R.id.text_Email);
        mPassword = findViewById(R.id.text_Password);
        mCreateAccount = findViewById(R.id.lb_CreateAccount);
        /*mButton = findViewById(R.id.btn_login);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });*/
    }

    public void logIn (View view) {
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        Toast.makeText(this, "Зачем вы нажали?", Toast.LENGTH_SHORT).show();

        onErrorDialog(email.isEmpty(),R.string.email_empty);
        onErrorDialog(password.isEmpty(),R.string.password_empty);
        onErrorDialog(password.length() < 8,R.string.password_length_8);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, appUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (Boolean.parseBoolean(response)) {
                    Intent intent = new Intent (getApplicationContext(), UserProfile.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }
                else { // вычистить с помощью метода
                    onErrorDialog(!Boolean.parseBoolean(response),response); //R.string.error_general
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
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
                } else
                    onErrorDialog(true, error.toString()); //R.string.error_general

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String>params = new HashMap<>();
                params.put("Accept", "Application/json: charset-UTF-8");
                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance().addRequestQueue(stringRequest);
    }

    public void startSignUpActivity(View view) {
        Intent intent = new Intent (getApplicationContext(), SignUp.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }

    private boolean onErrorDialog (boolean condition, int idRString) {
        if (condition) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
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
