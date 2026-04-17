package com.example.quanylysinhvien.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.database.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class XemPhanHoiActivity extends AppCompatActivity {

    TextView tvTitlePhanHoi, tvThongBaoPhanHoi;
    ListView lvPhanHoi;

    DBHelper dbHelper;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_phan_hoi);

        anhXa();

        dbHelper = new DBHelper(this);

        loadPhanHoi();
    }

    private void anhXa() {
        tvTitlePhanHoi = findViewById(R.id.tvTitlePhanHoi);
        tvThongBaoPhanHoi = findViewById(R.id.tvThongBaoPhanHoi);
        lvPhanHoi = findViewById(R.id.lvPhanHoi);
    }

    private void loadPhanHoi() {

        list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id, tenTaiKhoan, noiDung, ngayGui FROM PHANHOI ORDER BY id DESC",
                null
        );

        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();

                String id = String.valueOf(c.getInt(0));
                String tenTaiKhoan = c.getString(1);
                String noiDung = c.getString(2);
                String ngayGui = c.getString(3);

                if (ngayGui == null || ngayGui.trim().isEmpty()) {
                    ngayGui = "Chưa có ngày gửi";
                }

                map.put("id", "Mã phản hồi: " + id);
                map.put("tenTaiKhoan", "Tài khoản: " + tenTaiKhoan);
                map.put("noiDung", "Nội dung: " + noiDung);
                map.put("ngayGui", "Ngày gửi: " + ngayGui);

                list.add(map);

            } while (c.moveToNext());
        }

        c.close();
        db.close();

        tvThongBaoPhanHoi.setText("Tổng phản hồi: " + list.size());

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.dong_phan_hoi,
                new String[]{"id", "tenTaiKhoan", "noiDung", "ngayGui"},
                new int[]{R.id.tvMaPhanHoi, R.id.tvTaiKhoanPhanHoi, R.id.tvNoiDungPhanHoi, R.id.tvNgayGuiPhanHoi}
        );

        lvPhanHoi.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhanHoi();
    }
}