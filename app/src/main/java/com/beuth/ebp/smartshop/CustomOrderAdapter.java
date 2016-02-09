package com.beuth.ebp.smartshop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomOrderAdapter extends BaseAdapter {

    Context context;
    List<OrderItem> rowItem;

    CustomOrderAdapter(Context context, List<OrderItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;
    }

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_order_item, null);
        }

        View txtstatus = (View) convertView.findViewById(R.id.status);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        OrderItem row_pos = rowItem.get(position);
        // setting the image resource and title
        txtTitle.setText(row_pos.getTitle());
        switch (row_pos.getStatus()) {
            case "active":
                System.out.println("Excellent grade");
                System.out.println("Invalid grade");
                txtstatus.setBackgroundColor(Color.parseColor("#FF12F121"));
                txtstatus.invalidate();
                break;
            case "done":
                System.out.println("Very good grade");
                txtstatus.setBackgroundColor(Color.parseColor("#FFCA1C1C"));
                txtstatus.invalidate();
                break;
            case "pending":
                System.out.println("Good grade");
                txtstatus.setBackgroundColor(Color.parseColor("#FFEDF112"));
                txtstatus.invalidate();
                break;

            default:

                break;
        }


        return convertView;
    }
}
