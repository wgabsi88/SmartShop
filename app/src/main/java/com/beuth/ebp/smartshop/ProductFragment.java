package com.beuth.ebp.smartshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends ListFragment implements OnItemClickListener {

    CustomAdapter adapter;
    private List<RowItem> rowItems;

    private List<Item> items;
    Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        args = getArguments();
        items = (List<Item>) args.getSerializable("rowitems");
        return inflater.inflate(R.layout.list_product_fragment, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < items.size(); i++) {
            RowItem items = new RowItem(this.items.get(i).getTitle(), this.items.get(i).getQuantity());
            rowItems.add(items);
        }
        adapter = new CustomAdapter(getActivity(), rowItems);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra("title", items.get(position).getTitle());
        intent.putExtra("description", items.get(position).getDescription());
        intent.putExtra("quantity", items.get(position).getQuantity());
        getActivity().startActivity(intent);
    }
}
