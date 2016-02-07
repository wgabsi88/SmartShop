package com.beuth.ebp.smartshop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Produkt_detail extends AppCompatActivity {
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produkt_detail);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            value = extras.getString("id");

        }
        TextView txt = (TextView)findViewById(R.id.textviewproductname);
        txt.setText(value);
    }

}
