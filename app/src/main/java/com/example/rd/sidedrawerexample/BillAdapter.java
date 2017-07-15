package com.example.rd.sidedrawerexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * Created by rd on 11-02-2017.
 */
public class BillAdapter extends ArrayAdapter<BillPayment> {



    private FirebaseStorage mFirebaseStorage;

    //want to access chat-photos folder inside the storage thats why we need this variable
    private StorageReference mBillPhotosStorageReference;

    private Button button;



    public BillAdapter(Context context, int resource, ArrayList<BillPayment> objects) {
        super(context, resource, objects);
    }




    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item, parent, false);
        }


        TextView numberTextView = (TextView) convertView.findViewById(R.id.number);
        TextView supplierTextView = (TextView) convertView.findViewById(R.id.supplier_name);
        TextView buyerTextView = (TextView) convertView.findViewById(R.id.buyer);
        TextView amountTextView = (TextView) convertView.findViewById(R.id.amount);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_bill);
        mFirebaseStorage = FirebaseStorage.getInstance();


        BillPayment bill = getItem(position);

        int amount = bill.getAmount();
        String amountString = Integer.toString(amount);


            numberTextView.setText(bill.getBillNumber());
            supplierTextView.setText("Supplier: "+bill.getSupplierName());
        buyerTextView.setText("Buyer: "+bill.getBuyerName());
        amountTextView.setText("Rs."+amountString);
        dateTextView.setText(bill.getDate());

        mBillPhotosStorageReference = mFirebaseStorage.getReference().child(bill.getPhotoUrl());
        Glide.with(imageView.getContext())
                 .using(new FirebaseImageLoader())
                .load(mBillPhotosStorageReference)
                .into(imageView);



        return convertView;
    }






}
