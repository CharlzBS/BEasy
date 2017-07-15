package com.example.rd.sidedrawerexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by rd on 26-04-2017.
 */
public class BuyerInfo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_info);

        Intent intent = getIntent();
        BuyerSupplier buyerSupplier = (BuyerSupplier) intent.getSerializableExtra("Buyer");
        TextView buyerNameTextView = (TextView) findViewById(R.id.buyer_name);
        TextView buyerLocationTextView = (TextView) findViewById(R.id.buyer_location);
        TextView buyerMailTextView = (TextView) findViewById(R.id.buyer_mail);
        TextView buyerWebsiteTextView = (TextView) findViewById(R.id.buyer_website);
        TextView buyerContactTextView = (TextView) findViewById(R.id.buyer_contact);
        TextView buyerProductTextView = (TextView) findViewById(R.id.buyer_products);

        buyerNameTextView.setText(buyerSupplier.getBuyerName().toString());
        buyerLocationTextView.setText(buyerSupplier.getBuyerLocation().toString());
        buyerMailTextView.setText(buyerSupplier.getBuyerMail().toString());
        buyerWebsiteTextView.setText(buyerSupplier.getBuyerWebsite().toString());
        buyerContactTextView.setText(buyerSupplier.getBuyerContact().toString());
        buyerProductTextView.setText(buyerSupplier.getProducts().toString());
    }
}
