package com.example.myblog.auth;

import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myblog.MainActivity;
import com.example.myblog.R;
import com.example.myblog.volley.VolleySingleton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private String appUrl;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    MaterialEditText mName, mEmail, mPassword, mConfirmPassword;
    CheckBox mTerms;
    Button mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        appUrl = "http://192.168.31.149/api/signUp.php";
        mName = findViewById(R.id.text_Name);
        mEmail = findViewById(R.id.text_Email);
        mPassword = findViewById(R.id.text_Password);
        mConfirmPassword = findViewById(R.id.text_Confirm_Password);
        mTerms = findViewById(R.id.cbAcceptPolicy);

        mSignUp = findViewById(R.id.btn_createAccount);
    }
    public void signUp (View view) {
        name = mName.getText().toString();
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        confirmPassword = mConfirmPassword.getText().toString();
        if (!(
        onErrorDialog(name.isEmpty(),R.string.name_empty) &&
        onErrorDialog(name.length()<4,R.string.name_too_short) &&
        onErrorDialog(email.isEmpty(),R.string.email_empty) &&
        onErrorDialog(password.isEmpty(),R.string.password_empty) &&
        onErrorDialog(confirmPassword.isEmpty(),R.string.password_empty) &&
        onErrorDialog(password.length() < 8,R.string.password_length_8) &&
        onErrorDialog(!password.equals(confirmPassword),R.string.confirm_password_mismatch) &&
        onErrorDialog(!mTerms.isChecked(),R.string.confirm_password_mismatch))) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, appUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    onErrorDialog(Boolean.parseBoolean(response), R.string.account_created);
                    onErrorDialog(!Boolean.parseBoolean(response), R.string.error_account_created);
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
                        onErrorDialog(true, R.string.error_general);

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Accept", "Application/json: charset-UTF-8");
                    return super.getHeaders();
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            VolleySingleton.getInstance().addRequestQueue(stringRequest);
        }
    }
    private boolean onErrorDialog (boolean condition, int idRString) {
        if (condition) {
            AlertDialog.Builder alert = new AlertDialog.Builder(SignUp.this);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(SignUp.this);
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
