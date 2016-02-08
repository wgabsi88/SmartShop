package com.beuth.ebp.smartshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends ListFragment implements AdapterView.OnItemClickListener {

    CustomAdapter adapter;
    private List<RowItem> rowItems;

    private List<Order> Orders;
    Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        args = getArguments();
        Orders = (List<Order>) args.getSerializable("roworders");
      //  Log.e("oRDERSfragment", "" + Orders);
        return inflater.inflate(R.layout.list_product_fragment, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < Orders.size(); i++) {
            RowItem items = new RowItem(Orders.get(i).getOrderid(), Orders.get(i).getStatus());
            rowItems.add(items);
        }

        adapter = new CustomAdapter(getActivity(), rowItems);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), rowItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();

        Intent inent = new Intent(getActivity(), OrderDetailActivity.class);

        inent.putExtra("id", Orders.get(position).getOrderid());
        inent.putExtra("idproduct", Orders.get(position).getProductid());
        inent.putExtra("title", Orders.get(position).getTitle());
        inent.putExtra("quantity", Orders.get(position).getQuantity());
        inent.putExtra("name", Orders.get(position).getName());
        inent.putExtra("street", Orders.get(position).getStreet());
        inent.putExtra("housenr", Orders.get(position).getHousenr());
        inent.putExtra("zip", Orders.get(position).getZip());
        inent.putExtra("city", Orders.get(position).getCity());
        inent.putExtra("email", Orders.get(position).getEmail());
        inent.putExtra("phone", Orders.get(position).getPhone());
        inent.putExtra("position", position);
        getActivity().startActivity(inent);
    }
}
