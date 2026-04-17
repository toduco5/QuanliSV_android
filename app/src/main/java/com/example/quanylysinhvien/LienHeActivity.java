package com.example.quanylysinhvien;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LienHeActivity extends AppCompatActivity {

    Button btnGoi, btnEmail, btnWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lienhe);

        btnGoi = findViewById(R.id.btnGoi);
        btnEmail = findViewById(R.id.btnEmail);
        btnWeb = findViewById(R.id.btnWeb);

        btnGoi.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:02563812345"));
            startActivity(intent);
        });

        btnEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:qnu@qnu.edu.vn"));
            startActivity(intent);
        });

        btnWeb.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://qnu.edu.vn/"));
            startActivity(intent);
        });
    }
}