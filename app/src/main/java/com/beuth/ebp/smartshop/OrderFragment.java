package com.beuth.ebp.smartshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends ListFragment implements AdapterView.OnItemClickListener {

    CustomOrderAdapter adapter;
    private List<OrderItem> rowItems;

    private List<Order> Orders;
    Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        args = getArguments();
        Orders = (List<Order>) args.getSerializable("roworders");
        return inflater.inflate(R.layout.list_product_fragment, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        rowItems = new ArrayList<OrderItem>();
        for (int i = 0; i < Orders.size(); i++) {

            OrderItem items = new OrderItem(Orders.get(i).getTitle(), Orders.get(i).getStatus());
            rowItems.add(items);
        }
        adapter = new CustomOrderAdapter(getActivity(), rowItems);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);

        intent.putExtra("titleproduct", Orders.get(position).getOrderid());
        intent.putExtra("idproduct", Orders.get(position).getProductid());
        intent.putExtra("title", Orders.get(position).getTitle());
        intent.putExtra("quantity", Orders.get(position).getQuantity());
        intent.putExtra("name", Orders.get(position).getName());
        intent.putExtra("street", Orders.get(position).getStreet());
        intent.putExtra("housenr", Orders.get(position).getHousenr());
        intent.putExtra("zip", Orders.get(position).getZip());
        intent.putExtra("city", Orders.get(position).getCity());
        intent.putExtra("email", Orders.get(position).getEmail());
        intent.putExtra("phone", Orders.get(position).getPhone());
        intent.putExtra("status", Orders.get(position).getStatus());
        intent.putExtra("position", position);
        Log.e("postion", "" + position);
        getActivity().startActivity(intent);
    }
}
