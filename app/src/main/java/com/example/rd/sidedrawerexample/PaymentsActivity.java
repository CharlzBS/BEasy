package com.example.rd.sidedrawerexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by rd on 26-01-2017.
 */
public class PaymentsActivity extends AppCompatActivity {


    //variable for successfull sign in and photo picker
    public static final int RC_SIGN_IN = 1;

    private BillAdapter adapter;

    private ArrayList<BillPayment> billPayments;

    //for starting purpose where authentication wasnt required
    public static final String ANONYMOUS = "anonymous";

    //variable for username of logged in user
    private String mUsername;


    private ImageView billImageView;
    private ListView mBillPaymentListView;

    // entry point for app to access database ,,, We will initialize it later
    private FirebaseDatabase mFirebaseDatabase;

    //references a specific part of the database ,,, in this case we need to access only messages object in the JSON tree thats why named it as mMessagesDBReference ,,, initialize later
    private DatabaseReference mBillsDatabaseReference;

    //Child event listener as we traverse in the JSON tree
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPayment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToPaymentEditor = new Intent(PaymentsActivity.this, PaymentEditorActivity.class);
                startActivity(intentToPaymentEditor);

            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //get a reference to the root node and then specifically messages portion of the database
        mBillsDatabaseReference = mFirebaseDatabase.getReference().child("payments");


        mBillPaymentListView = (ListView) findViewById(R.id.list);
        mBillPaymentListView.setEmptyView(findViewById(R.id.empty_view));

        billPayments = new ArrayList<>();

        adapter = new BillAdapter(this, R.layout.list_item, billPayments);


        mBillPaymentListView.setAdapter(adapter);


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
                    BillPayment friendlyMessage = dataSnapshot.getValue(BillPayment.class);
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