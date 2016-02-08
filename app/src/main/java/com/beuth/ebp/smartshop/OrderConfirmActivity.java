package com.beuth.ebp.smartshop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class OrderConfirmActivity extends AppCompatActivity {
    private String id;
    String productid;
    String title;
    String quantity;
    String name;
    String street;
    int housenr;
    int zip;
    String city;
    String email;
    String phone;
    Button detailProductButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        /*Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            id = extras.getString("id");
           // Log.e("orderdetail", "" + id);
            productid = extras.getString("productid");
           // Log.e("productid",""+productid);
            title = extras.getString("title");
           // Log.e("title",""+title);
            quantity = extras.getString("quantity");
           // Log.e("quantity",""+quantity);
            name = extras.getString("name");
          //  Log.e("name",""+name);
            street = extras.getString("street");
          //  Log.e("street",""+street);
            housenr = extras.getInt("housenr");
            zip = extras.getInt("zip");
           // Log.e("zip",""+zip);
            city = extras.getString("city");
          //  Log.e("city",""+city);
            email = extras.getString("email");
          //  Log.e("email",""+email);
            phone = extras.getString("phone");
          //  Log.e("phone",""+phone); */

        }
    /*    TextView txtid = (TextView)findViewById(R.id.textviewproductname);
        TextView txttitle = (TextView)findViewById(R.id.textviewproductname);
        TextView txtquantity = (TextView)findViewById(R.id.textviewproductquantity);
        TextView txtname = (TextView)findViewById(R.id.textviewrevievername);
        TextView txtstreet = (TextView)findViewById(R.id.textviewrevieverstreet);
        TextView txthousenr = (TextView)findViewById(R.id.textviewrevieverhousenr);
        TextView txtzip = (TextView)findViewById(R.id.textviewrevieverzip);
        TextView txtcity = (TextView)findViewById(R.id.textviewrevievercity);
        TextView txtemail = (TextView)findViewById(R.id.textviewrevievermail);
        TextView txtphone = (TextView)findViewById(R.id.textviewrevieverphone);

        detailProductButton = (Button) findViewById(R.id.detailproductbutton);
        detailProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderConfirmActivity.this, ProductDetailActivity.class);
                intent.putExtra("id", "Fahrrad");
                startActivity(intent);
            }
        });

        txtid.setText(id);
        txttitle.setText(title);
        txtquantity.setText(quantity);
        txtname.setText(name);
        txtstreet.setText(street);
        txthousenr.setText(""+housenr);
        txtzip.setText(""+zip);
        txtcity.setText(city);
        txtemail.setText(email);
        txtphone.setText(phone); */
    }





