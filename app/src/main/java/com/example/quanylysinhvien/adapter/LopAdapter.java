package com.example.quanylysinhvien.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanylysinhvien.R;
import com.example.quanylysinhvien.dao.LopDao;
import com.example.quanylysinhvien.model.Lop;

import java.util.ArrayList;
import java.util.List;

public class LopAdapter extends BaseAdapter implements Filterable {

    private Activity activity;
    private int layout;
    private ArrayList<Lop> list;
    private ArrayList<Lop> listOld;
    private LopDao lopDao;

    public LopAdapter(Activity activity, int layout, ArrayList<Lop> list) {
        this.activity = activity;
        this.layout = layout;
        this.list = list;
        this.listOld = new ArrayList<>(list);
        this.lopDao = new LopDao(activity);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView txtMaLopHoc, txtTenLopHoc;
        ImageView imageView, imageEdit, imageDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(activity)
                    .inflate(layout, parent, false);

            holder.txtMaLopHoc = convertView.findViewById(R.id.txtMaLophoc);
            holder.txtTenLopHoc = convertView.findViewById(R.id.txtTenLophoc);
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.imageEdit = convertView.findViewById(R.id.imageeditlop);
            holder.imageDelete = convertView.findViewById(R.id.imageViewdeletelop);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Lop lop = list.get(position);

        holder.txtMaLopHoc.setText(lop.getMaLop());
        holder.txtTenLopHoc.setText(lop.getTenLop());

        holder.imageView.setImageResource(R.drawable.logoqnu);

        holder.imageEdit.setOnClickListener(v -> showDialogSua(lop));

        holder.imageDelete.setOnClickListener(v -> xoaLop(lop));

        return convertView;
    }

    private void showDialogSua(Lop lop) {

        View view = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_sua_lop, null);

        EditText edtMaLop = view.findViewById(R.id.edtMaLopSua);
        EditText edtTenLop = view.findViewById(R.id.edtTenLopSua);

        edtMaLop.setText(lop.getMaLop());
        edtTenLop.setText(lop.getTenLop());

        edtMaLop.setEnabled(false);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Sửa lớp học")
                .setView(view)
                .setPositiveButton("Lưu", null)
                .setNegativeButton("Hủy", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {

                    String tenMoi = edtTenLop.getText()
                            .toString()
                            .trim();

                    if (tenMoi.isEmpty()) {
                        edtTenLop.setError("Nhập tên lớp");
                        edtTenLop.requestFocus();
                        return;
                    }

                    Lop lopMoi = new Lop(
                            lop.getMaLop(),
                            tenMoi
                    );

                    if (lopDao.update(lopMoi)) {

                        lop.setTenLop(tenMoi);
                        notifyDataSetChanged();

                        Toast.makeText(activity,
                                "Sửa thành công",
                                Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    } else {
                        Toast.makeText(activity,
                                "Sửa thất bại",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void xoaLop(Lop lop) {

        new AlertDialog.Builder(activity)
                .setTitle("Xóa lớp")
                .setMessage("Bạn có chắc muốn xóa " + lop.getMaLop() + " ?")
                .setPositiveButton("Xóa", (dialog, which) -> {

                    if (lopDao.delete(lop.getMaLop())) {

                        list.remove(lop);
                        listOld.remove(lop);

                        notifyDataSetChanged();

                        Toast.makeText(activity,
                                "Xóa thành công",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(activity,
                                "Xóa thất bại",
                                Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public Filter getFilter() {
        return filterLop;
    }

    private Filter filterLop = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String key = constraint.toString().toLowerCase();

            ArrayList<Lop> newList = new ArrayList<>();

            if (key.isEmpty()) {
                newList.addAll(listOld);
            } else {

                for (Lop lop : listOld) {

                    if (lop.getMaLop().toLowerCase().contains(key)
                            || lop.getTenLop().toLowerCase().contains(key)) {

                        newList.add(lop);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = newList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            list.clear();
            list.addAll((List<Lop>) results.values);
            notifyDataSetChanged();
        }
    };
}