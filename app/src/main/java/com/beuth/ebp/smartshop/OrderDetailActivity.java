package com.beuth.ebp.smartshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderDetailActivity extends AppCompatActivity {
    String value;
    Button detailProductButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            value = extras.getString("id");
        }
        detailProductButton = (Button) findViewById(R.id.detailproductbutton);
        detailProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, ProductDetailActivity.class);
                intent.putExtra("id", "Fahrrad");
                startActivity(intent);
            }
        });
        TextView txt = (TextView)findViewById(R.id.textviewproductname);
        txt.setText(value);
    }
}
