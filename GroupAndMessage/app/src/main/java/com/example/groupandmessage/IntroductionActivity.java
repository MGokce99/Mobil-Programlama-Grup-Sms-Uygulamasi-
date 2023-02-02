package com.example.groupandmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class IntroductionActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        findViewById(R.id.btn_register).setOnClickListener(view -> {
            startActivity(new Intent(IntroductionActivity.this,RegisterActivity.class));
        });
        findViewById(R.id.btn_login).setOnClickListener(view -> {
            startActivity(new Intent(IntroductionActivity.this,LoginActivity.class));
        });
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Toast.makeText(this,"Yönlendiriliyor",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(IntroductionActivity.this, MainActivity.class));
        }
    }
}