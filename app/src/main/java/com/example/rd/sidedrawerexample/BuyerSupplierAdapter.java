package com.example.rd.sidedrawerexample;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rd on 26-04-2017.
 */
public class BuyerSupplierAdapter  extends ArrayAdapter<BuyerSupplier> {

    public BuyerSupplierAdapter(Context context, int resource, ArrayList<BuyerSupplier> objects) {
        super(context, resource, objects);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item_bs, parent, false);
        }


        TextView buyerNameTextView = (TextView) convertView.findViewById(R.id.name_of_buyer);
        TextView buyerLocationTextView = (TextView) convertView.findViewById(R.id.location_of_buyer);



        BuyerSupplier buyerSupplier = getItem(position);




        buyerNameTextView.setText(buyerSupplier.getBuyerName());
        buyerLocationTextView.setText(buyerSupplier.getBuyerLocation());



        return convertView;
    }
}
