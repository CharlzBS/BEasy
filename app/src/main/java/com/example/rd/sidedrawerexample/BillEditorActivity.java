package com.example.rd.sidedrawerexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.github.brunodles.compressor.BitmapCompressor;
import com.github.brunodles.pic_picker.PicPicker;
import com.github.brunodles.pic_picker.impl.WritePermissionAsker;
import com.github.brunodles.pic_picker.listener.ActivityStarter;
import com.github.brunodles.pic_picker.listener.CantFindCameraAppErrorListener;
import com.github.brunodles.pic_picker.listener.ErrorCreatingTempFileForCameraListener;
import com.github.brunodles.pic_picker.listener.PicResultListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import retrofit2.http.Url;

/**
 * Created by rd on 25-01-2017.
 */
public class BillEditorActivity extends AppCompatActivity implements ActivityStarter, View.OnClickListener {

    /**
     * EditText field to enter the pet's name
     */
    private EditText mSupplierEditNameText;



    /**
     * EditText field to enter the pet's breed
     */
    private EditText mBuyerEditNameText;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText mBillNumberEditText;

    private EditText mAmountEditText;


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

    private LinearLayout galery;
    private LinearLayout camera;
    private ImageView image;
    private WritePermissionAsker writePermissionAsker;
    private PicPicker picPicker;
    int n;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_editor);



        // Find all relevant views that we will need to read user input from
        mSupplierEditNameText = (EditText) findViewById(R.id.edit_supplier_name);
        mBuyerEditNameText = (EditText) findViewById(R.id.edit_buyer_name);
        mBillNumberEditText = (EditText) findViewById(R.id.edit_bill_number);
        mAmountEditText = (EditText) findViewById(R.id.edit_bill_amount);



        //getInstance is called on each entry point for each feature ,,,, i.e. FirebaseDatabase, FirebaseAuth, FirebaseStorage
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        Random rand = new Random();

        n = rand.nextInt(300) + 1;

        mBillPhotosStorageReference = mFirebaseStorage.getReference().child("bill_photos_"+n);


        //get a reference to the root node and then specifically messages portion of the database
        mBillsDatabaseReference = mFirebaseDatabase.getReference().child("bills");



        galery = (LinearLayout) findViewById(R.id.galery);
        camera = (LinearLayout) findViewById(R.id.camera);
        image = (ImageView) findViewById(R.id.image);

        // This is the default implementation of the permission asker,
        // but you can write your another implementation, or ask the permission for your own.
        writePermissionAsker = new WritePermissionAsker(this, RC_WRITE_EXTERNAL_STORAGE,
                R.string.permission_message);

        // Prepare the picPicker
        picPicker = new PicPicker(this, picResultListener)
                // the will be called when we get a picture from the camera
                .setFileForCameraListener(fileForCameraListener)
                // this will be called when we got a error from the camera app,
                // sometimes it even doesn't exists.
                .setCameraAppErrorListener(cameraAppErrorListener)
                // this will be called when we need a permission, for android 6+
                .setPermissionErrorListener(writePermissionAsker);

        galery.setOnClickListener(this);
        camera.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if (v == galery)
            picPicker.gallery(); // Here you call the gallery app
        else if (v == camera)
            picPicker.camera(); // Here you call the camera app
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // You need to pass the permission result to writePermissionAsker, so the lib can check
        // if the app have write permission. Having permission we start the camera.
        if (writePermissionAsker.onRequestPermissionsResult(requestCode, permissions, grantResults))
            picPicker.camera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // You need to pass the activity result to picPicker, so the lib can check the response from
        // the gallery or camera. It will return true if found the image.
        if (!picPicker.onActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    // This listener will receive the bitmap from the picPicture, so you don't need to worry about from
    // where we got the image.
    private PicResultListener picResultListener = new PicResultListener() {
        @Override
        public void onPictureResult(final Bitmap bitmap) {
            Log.d(TAG, "onPictureResult: ");
            image.setImageBitmap(bitmap); // the the bitmap on a ImageView
            // This is the BitmapCompressor, it will compress the image to a better size.
            // We may not need a full sized picture on our services.
            // This is a AsyncTask, so we need to implement the onPostExecute
            // This could even be a field on the Activity, it's here just to show that it's optional.



        }
    };
    private CantFindCameraAppErrorListener cameraAppErrorListener = new CantFindCameraAppErrorListener() {
        @Override
        public void cantFindCameraApp() {
            Log.e(TAG, "cantFindCameraApp: ");
            Toast.makeText(BillEditorActivity.this, "Can't find the camera app", Toast.LENGTH_SHORT).show();
        }
    };
    private ErrorCreatingTempFileForCameraListener fileForCameraListener = new ErrorCreatingTempFileForCameraListener() {
        @Override
        public void errorCreatingTempFileForCamera() {
            Log.e(TAG, "errorCreatingTempFileForCamera: ");
            Toast.makeText(BillEditorActivity.this, "Error starting camera", Toast.LENGTH_SHORT).show();
        }
    };









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
                saveBillDetails();

                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(BillEditorActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String downloadU;

    public void saveBillDetails() {




        // Get the data from an ImageView as bytes
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = image.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mBillPhotosStorageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                downloadU = downloadUrl.toString();
            }

        });

        BillPayment friendlyMessage = new BillPayment(101, "4 March 2017", mSupplierEditNameText.getText().toString(), mBuyerEditNameText.getText().toString(), mBillNumberEditText.getText().toString(), Integer.parseInt(mAmountEditText.getText().toString()),"bill_photos_"+n);

        //then this object created is pushed into the messages object of the JSON tree
        mBillsDatabaseReference.push().setValue(friendlyMessage);

        // Clear input box

    }


}
