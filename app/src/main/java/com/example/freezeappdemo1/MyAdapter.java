package com.example.freezeappdemo1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
    private Context context;


    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(context, R.layout.cell_listview, null);
        } else {
            view = convertView;
        }

        CheckBox checkBox = view.findViewById(R.id.checkbox_cell);
        TextView textView = view.findViewById(R.id.tv_cell_content);

        return view;
    }
}
