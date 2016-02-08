package com.beuth.ebp.smartshop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ProductDetailActivity extends AppCompatActivity {
    String title;
    String description;
    String quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produkt_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("title");
            description = extras.getString("description");
            quantity = extras.getString("quantity");
        }
        TextView productNameTextView = (TextView) findViewById(R.id.textviewproductname);
        productNameTextView.setText(title);

        TextView quantityTextView = (TextView) findViewById(R.id.textviewproductquantity);
        quantityTextView.setText(quantity);

        TextView descriptionTextView = (TextView) findViewById(R.id.textviewproductdescription);
        descriptionTextView.setText(description);
    }

}
