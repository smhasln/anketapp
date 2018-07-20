package com.sengelgrup.anket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminAdapter extends BaseAdapter {

    ArrayList<String> dizi_tarih;
    ArrayList<String> dizi_isim;
    ArrayList<String> dizi_telefon;
    ArrayList<String> dizi_1;
    ArrayList<String> dizi_2;
    ArrayList<String> dizi_3;
    ArrayList<String> dizi_4;

    LayoutInflater layoutInflater = null;

    public AdminAdapter(User user,
                        ArrayList<String> tarihler,
                        ArrayList<String> isimler,
                        ArrayList<String> telefonlar,
                        ArrayList<String> dizi_1,
                        ArrayList<String> dizi_2,
                        ArrayList<String> dizi_3,
                        ArrayList<String> dizi_4)
    {
        this.dizi_tarih = tarihler;
        this.dizi_isim = isimler;
        this.dizi_telefon = telefonlar;
        this.dizi_1 = dizi_1;
        this.dizi_2 = dizi_2;
        this.dizi_3 = dizi_3;
        this.dizi_4 = dizi_4;

        layoutInflater = (LayoutInflater)user.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dizi_tarih.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View RowView = layoutInflater.inflate(R.layout.admin_list,null);

        TextView txt_tarih = RowView.findViewById(R.id.list_tarih);
        TextView txt_isim = RowView.findViewById(R.id.list_isim);
        TextView txt_telefon = RowView.findViewById(R.id.list_telefon);

        RatingBar admin1 = RowView.findViewById(R.id.admin_bir);
        RatingBar admin2 = RowView.findViewById(R.id.admin_iki);
        RatingBar admin3 = RowView.findViewById(R.id.admin_uc);
        RatingBar admin4 = RowView.findViewById(R.id.admin_dort);

        txt_tarih.setText(dizi_tarih.get(position));
        txt_isim.setText(dizi_isim.get(position));
        txt_telefon.setText(dizi_telefon.get(position));

        admin1.setRating(Float.valueOf(dizi_1.get(position)));
        admin2.setRating(Float.valueOf(dizi_2.get(position)));
        admin3.setRating(Float.valueOf(dizi_3.get(position)));
        admin4.setRating(Float.valueOf(dizi_4.get(position)));


        return RowView;
    }
}
