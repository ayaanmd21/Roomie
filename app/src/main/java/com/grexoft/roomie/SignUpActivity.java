package com.grexoft.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.grexoft.roomie.utils.SharedPrefsUtil;
import com.grexoft.roomie.utils.Utils;

public class SignUpActivity extends AppCompatActivity {
    public static final String USER_NAME = "userId";
    public static final String NAME = "name";
    private EditText etUserName, etName, etPassword, etMobile;
    private Button btnFinish;
    String userName, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etName = (EditText) findViewById(R.id.et_username);
        etUserName = (EditText) findViewById((R.id.et_user_id));
        etPassword = (EditText) findViewById(R.id.et_Password);
        btnFinish = (Button) findViewById(R.id.btn_finish);

        userName = getIntent().getStringExtra(USER_NAME);
        name = getIntent().getStringExtra(NAME);


        etUserName.setText(userName);
        etName.setText(name);
        etName.setSelection(name.length());
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etName.getText().toString()) ||
                        TextUtils.isEmpty(etUserName.getText().toString())
                        || TextUtils.isEmpty(etPassword.getText().toString())
                        || TextUtils.isEmpty(etMobile.getText().toString())) {
                    Utils.showSnackBar(btnFinish, getString(R.string.error_all_fields));
                } else {

                    SharedPrefsUtil.setBooleanPreference(SignUpActivity.this, SharedPrefsUtil.IS_INITIALIZED, true);
                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                    finish();
                }
            }
        });

        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                Utils.setCompoundDrawable(etUserName, SignUpActivity.this);

            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Utils.setCompoundDrawable(etPassword, SignUpActivity.this);
            }
        });

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                Utils.setCompoundDrawable(etName, SignUpActivity.this);

            }
        });

        etMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Utils.setCompoundDrawable(etMobile, SignUpActivity.this);
            }
        });



    }
}
