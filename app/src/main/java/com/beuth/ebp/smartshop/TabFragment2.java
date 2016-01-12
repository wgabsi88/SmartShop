package com.beuth.ebp.smartshop;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by waelgabsi on 05.01.16.
 */

public class TabFragment2 extends ListFragment implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> listAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // Create and populate a List of planet names.
        String[] planets = new String[]{"Order 1", "Order 2", "Order 3", "Order 4",
                "Order 5", "Order 6", "Order 7", "Order 8"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll(Arrays.asList(planets));

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simplerow, planetList);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        listAdapter.add( "Order 9" );
        listAdapter.add( "Order 10" );



        // Set the ArrayAdapter as the ListView's adapter.
        setListAdapter(listAdapter );
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
    //   Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Intent inent = new Intent(getActivity(), Produkt_detail.class);
        String aaa = ""+(position+1);

        inent.putExtra("id", aaa);

        getActivity().startActivity(inent);
    }
}