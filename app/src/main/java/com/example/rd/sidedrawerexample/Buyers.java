package com.example.rd.sidedrawerexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rd on 26-04-2017.
 */
public class Buyers extends AppCompatActivity {

    //variable for successfull sign in and photo picker
    public static final int RC_SIGN_IN = 1;

    private BuyerSupplierAdapter adapter;

    private ArrayList<BuyerSupplier> buyerSuppliers;

    //for starting purpose where authentication wasnt required
    public static final String ANONYMOUS = "anonymous";

    //variable for username of logged in user
    private String mUsername;


    private ListView mbuyerSupplierListView;

    // entry point for app to access database ,,, We will initialize it later
    private FirebaseDatabase mFirebaseDatabase;

    //references a specific part of the database ,,, in this case we need to access only messages object in the JSON tree thats why named it as mMessagesDBReference ,,, initialize later
    private DatabaseReference mBillsDatabaseReference;

    //Child event listener as we traverse in the JSON tree
    private ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyersupplier);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBuyers);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToBuyerEditor = new Intent(Buyers.this, BuyerEditor.class);
                startActivity(intentToBuyerEditor);

            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //get a reference to the root node and then specifically messages portion of the database
        mBillsDatabaseReference = mFirebaseDatabase.getReference().child("buyers");


        mbuyerSupplierListView = (ListView) findViewById(R.id.list_bs);


        buyerSuppliers = new ArrayList<>();

        adapter = new BuyerSupplierAdapter(this, R.layout.list_item_bs, buyerSuppliers);


        mbuyerSupplierListView.setAdapter(adapter);

        mbuyerSupplierListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BuyerSupplier buyer = buyerSuppliers.get(position);
                Intent in = new Intent(Buyers.this, BuyerInfo.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("Buyer", (Serializable) buyer);

                in.putExtras(bundle);

                startActivity(in);
            }
        });


        attachDatabaseReadListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseReadListener();

    }


    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        adapter.clear();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BuyerSupplier friendlyMessage = dataSnapshot.getValue(BuyerSupplier.class);
                    adapter.add(friendlyMessage);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mBillsDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mBillsDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


}
