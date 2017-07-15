package com.example.rd.sidedrawerexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.github.brunodles.pic_picker.listener.ActivityStarter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by rd on 26-04-2017.
 */
public class BuyerEditor extends AppCompatActivity implements ActivityStarter {

    /**
     * EditText field to enter the pet's name
     */
    private EditText mBuyerSupplierEditNameText;


    /**
     * EditText field to enter the pet's weight
     */
    private EditText mBuyerLocationEditText;

    private EditText mBuyerMailEditText;

    private EditText mBuyerWebsiteEditText;

    private EditText mBuyerContactEditText;

    private EditText mBuyerProductsEditText;



    //variable for username of logged in user
    private String mUsername;

    // entry point for app to access database ,,, We will initialize it later
    private FirebaseDatabase mFirebaseDatabase;

    //references a specific part of the database ,,, in this case we need to access only messages object in the JSON tree thats why named it as mMessagesDBReference ,,, initialize later
    private DatabaseReference mBillsDatabaseReference;


    //Child event listener as we traverse in the JSON tree
    private ChildEventListener mChildEventListener;

    //variable for accessing entry point of the FirebaseStorage
    private FirebaseStorage mFirebaseStorage;

    //want to access chat-photos folder inside the storage thats why we need this variable
    private StorageReference mBillPhotosStorageReference;




    // AUTHENTICATION VARIABLES
    //private FirebaseAuth mFirebaseAuth;

    //for checking each time whether user is logged in or not
    //private FirebaseAuth.AuthStateListener mAuthStateListener;




    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */

    private static final String TAG = "MainActivity";
    // This is the request code used to ask write permission
    private static final int RC_WRITE_EXTERNAL_STORAGE = 42;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_editor);

        // Find all relevant views that we will need to read user input from
        mBuyerSupplierEditNameText = (EditText) findViewById(R.id.edit_buyersupplier_name);
        mBuyerLocationEditText = (EditText) findViewById(R.id.edit_buyer_location);
        mBuyerContactEditText = (EditText) findViewById(R.id.edit_buyer_contact);
        mBuyerWebsiteEditText = (EditText) findViewById(R.id.edit_buyer_website);
        mBuyerMailEditText = (EditText) findViewById(R.id.edit_buyer_email);
        mBuyerProductsEditText = (EditText) findViewById(R.id.edit_buyer_products);


        //getInstance is called on each entry point for each feature ,,,, i.e. FirebaseDatabase, FirebaseAuth, FirebaseStorage
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        //get a reference to the root node and then specifically messages portion of the database
        mBillsDatabaseReference = mFirebaseDatabase.getReference().child("buyers");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_bill_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                saveBuyerDetails();

                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(BuyerEditor.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveBuyerDetails() {
        BuyerSupplier buyerSupp = new BuyerSupplier(mBuyerSupplierEditNameText.getText().toString(),mBuyerLocationEditText.getText().toString(),mBuyerContactEditText.getText().toString(),mBuyerWebsiteEditText.getText().toString(),mBuyerMailEditText.getText().toString(),mBuyerProductsEditText.getText().toString());

        //then this object created is pushed into the messages object of the JSON tree
        mBillsDatabaseReference.push().setValue(buyerSupp);
    }



}
