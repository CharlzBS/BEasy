package com.example.rd.sidedrawerexample;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //variable for successfull sign in and photo picker
    public static final int RC_SIGN_IN = 1;

    private BillAdapter adapter;

    private ArrayList<BillPayment> billPayments;

    //for starting purpose where authentication wasnt required
    public static final String ANONYMOUS = "anonymous";

    //variable for username of logged in user
    private String mUsername;

    private Button mEditBill;


    private  ListView mBillPaymentListView;

    // entry point for app to access database ,,, We will initialize it later
    private FirebaseDatabase mFirebaseDatabase;

    //references a specific part of the database ,,, in this case we need to access only messages object in the JSON tree thats why named it as mMessagesDBReference ,,, initialize later
    private DatabaseReference mBillsDatabaseReference;

    //Child event listener as we traverse in the JSON tree
    private ChildEventListener mChildEventListener;

    private FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mFirebaseAuthStateListener;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Bills");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToBillEditor = new Intent(MainActivity.this,BillEditorActivity.class);
                startActivity(intentToBillEditor);

            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();



        //get a reference to the root node and then specifically messages portion of the database
        mBillsDatabaseReference = mFirebaseDatabase.getReference().child("bills");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        mBillPaymentListView = (ListView) findViewById(R.id.list);
        mBillPaymentListView.setEmptyView(findViewById(R.id.empty_view));


        billPayments = new ArrayList<>();

        adapter = new BillAdapter(this, R.layout.list_item, billPayments);

        mBillPaymentListView.setAdapter(adapter);
       0






        mFirebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    // user is logged in
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // user is logged out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                    AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER
                                    )
                                    .setLogo(R.mipmap.ic_launcher)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }






    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AuthUI.getInstance().signOut(this);
            return true;
        }
        else if(id==R.id.menuSearch) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        //FragmentManager fm = getFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_bills) {
            // Handle the intent to Main Activity
            Intent intentToBillsActivity = new Intent(MainActivity.this,MainActivity.class);
            startActivity(intentToBillsActivity);

        } else if (id == R.id.nav_payments) {
            Intent intentToPaymentsActivity = new Intent(MainActivity.this,PaymentsActivity.class);
            startActivity(intentToPaymentsActivity);

        } else if (id == R.id.nav_buyers) {

            Intent intentToBuyersActivity = new Intent(MainActivity.this,Buyers.class);
            startActivity(intentToBuyersActivity);

        } else if (id == R.id.nav_suppliers) {

            Intent intentToSupplierActivity = new Intent(MainActivity.this,Suppliers.class);
            startActivity(intentToSupplierActivity);


        } else if (id == R.id.nav_settings) {

        }

        else if(id == R.id.nav_logout)
        {
            AuthUI.getInstance().signOut(this);
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (requestCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Signed In", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_LONG).show();
            finish();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        if(mFirebaseAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthStateListener);
        }
        detachDatabaseReadListener();
        adapter.clear();
    }

    private void onSignedInInitialize(String user) {
        mUsername = user;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        adapter.clear();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if(mChildEventListener == null) {
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
        if(mChildEventListener != null) {
            mBillsDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }



}
