package com.beuth.ebp.smartshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderDetailActivity extends AppCompatActivity {
    private String productTitle;
    String productid;
    String title;
    String quantity;
    String name;
    String street;
    String status;
    int housenr;
    int zip;
    int position;
    String city;
    String email;
    String phone;
    Button detailProductButton;
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productTitle = extras.getString("titleproduct");
            productid = extras.getString("productid");
            title = extras.getString("title");
            quantity = extras.getString("quantity");
            name = extras.getString("name");
            street = extras.getString("street");
            housenr = extras.getInt("housenr");
            zip = extras.getInt("zip");
            city = extras.getString("city");
            email = extras.getString("email");
            phone = extras.getString("phone");
            position = extras.getInt("position");
            status = extras.getString("status");
        }

        TextView txttitle = (TextView) findViewById(R.id.textviewproductname);
        TextView txtquantity = (TextView) findViewById(R.id.textviewproductquantity);
        TextView txtname = (TextView) findViewById(R.id.textviewrevievername);
        TextView txtstreet = (TextView) findViewById(R.id.textviewrevieverstreet);
        TextView txthousenr = (TextView) findViewById(R.id.textviewrevieverhousenr);
        TextView txtzip = (TextView) findViewById(R.id.textviewrevieverzip);
        TextView txtcity = (TextView) findViewById(R.id.textviewrevievercity);
        TextView txtemail = (TextView) findViewById(R.id.textviewrevievermail);
        TextView txtphone = (TextView) findViewById(R.id.textviewrevieverphone);

        detailProductButton = (Button) findViewById(R.id.detailproductbutton);
        detailProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, ProductDetailActivity.class);
                intent.putExtra("title", productTitle);
                intent.putExtra("description", "this is a simple product description");
                intent.putExtra("quantity", "23");
                startActivity(intent);
            }
        });

        confirmButton = (Button) findViewById(R.id.confirmbtn);
        if(status.equals("done")){

            confirmButton.setVisibility(View.GONE);

        }
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, OrderConfirmActivity.class);

                intent.putExtra("name", name);
                intent.putExtra("street", street);
                intent.putExtra("housenr", housenr);
                intent.putExtra("zip", zip);
                intent.putExtra("city", city);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        txttitle.setText(title);
        txtquantity.setText(quantity);
        txtname.setText(name);
        txtstreet.setText(street);
        txthousenr.setText("" + housenr);
        txtzip.setText("" + zip);
        txtcity.setText(city);
        txtemail.setText(email);
        txtphone.setText(phone);
    }

}

