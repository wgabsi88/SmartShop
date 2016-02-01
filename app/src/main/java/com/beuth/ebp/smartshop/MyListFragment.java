package com.beuth.ebp.smartshop;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyListFragment extends ListFragment implements OnItemClickListener {


	 String [] menutitles={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
	String [] menuIcons={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
	CustomAdapter adapter;
	private List<RowItem> rowItems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.list_fragment, null, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);



		rowItems = new ArrayList<RowItem>();

		for (int i = 0; i < menutitles.length; i++) {
			RowItem items = new RowItem(menutitles[i], menuIcons[i]);

			rowItems.add(items);
		}

		adapter = new CustomAdapter(getActivity(), rowItems);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Toast.makeText(getActivity(), menutitles[position], Toast.LENGTH_SHORT)
				.show();

	}

}
