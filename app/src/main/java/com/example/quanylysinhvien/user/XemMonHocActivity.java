package com.example.quanylysinhvien.user;

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

public class XemMonHocActivity extends AppCompatActivity {

    TextView tvTitleMonHoc, tvThongBaoMonHoc;
    ListView lvMonHoc;

    DBHelper dbHelper;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_mon_hoc);

        anhXa();
        dbHelper = new DBHelper(this);
        loadMonHoc();
    }

    private void anhXa() {
        tvTitleMonHoc = findViewById(R.id.tvTitleMonHoc);
        tvThongBaoMonHoc = findViewById(R.id.tvThongBaoMonHoc);
        lvMonHoc = findViewById(R.id.lvMonHocUser);
    }

    private void loadMonHoc() {
        list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT maMon, tenMon, soTinChi FROM MONHOC", null);

        if (c.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("maMon", "Mã môn: " + c.getString(0));
                map.put("tenMon", "Tên môn: " + c.getString(1));
                map.put("soTinChi", "Số tín chỉ: " + c.getInt(2));
                list.add(map);
            } while (c.moveToNext());

            tvThongBaoMonHoc.setText("Tổng số môn học: " + list.size());
        } else {
            tvThongBaoMonHoc.setText("Chưa có dữ liệu môn học.");
        }

        c.close();
        db.close();

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.dong_xem_mon_hoc,
                new String[]{"maMon", "tenMon", "soTinChi"},
                new int[]{R.id.tvMaMon, R.id.tvTenMon, R.id.tvSoTinChi}
        );

        lvMonHoc.setAdapter(adapter);
    }
}