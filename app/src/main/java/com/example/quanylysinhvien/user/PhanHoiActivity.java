package com.example.quanylysinhvien.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanylysinhvien.R;

public class PhanHoiActivity extends AppCompatActivity {

    EditText edtNoiDungPhanHoi;
    Button btnGuiPhanHoi;
    String tenDangNhap = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phanhoi);

        edtNoiDungPhanHoi = findViewById(R.id.edtNoiDungPhanHoi);
        btnGuiPhanHoi = findViewById(R.id.btnGuiPhanHoi);

        if (getIntent() != null) {
            String temp = getIntent().getStringExtra("tenDangNhap");
            if (temp != null) tenDangNhap = temp;
        }

        btnGuiPhanHoi.setOnClickListener(v -> {
            String noiDung = edtNoiDungPhanHoi.getText().toString().trim();

            if (noiDung.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung phản hồi", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Cảm ơn " + tenDangNhap + " đã phản hồi!", Toast.LENGTH_SHORT).show();
            edtNoiDungPhanHoi.setText("");
        });
    }
}