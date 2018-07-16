package edu.sdsu.tvidhate.vanilla;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MyPlacesActivity extends AppCompatActivity {

    private ImageView mDisplayImage;
    private TextView mDisplayPlace;
    private int height,width;
    private StorageReference firebaseStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places);

        mDisplayImage = findViewById(R.id.displayImage);
//        mDisplayPlace = findViewById(R.id.displayPlace);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        firebaseStorageRef = FirebaseStorage.getInstance().getReference();
        Log.i("TANVI",firebaseStorageRef.child("Photos").child("crater_lake.jpg").toString());


        firebaseStorageRef.child("Photos").child("crater_lake.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext()).load(uri).resize(width/2,height/4).into(mDisplayImage);
            }
        });
       // Glide.with(this).load(firebaseStorageRef.child("Photos").child("crater_lake.jpg")).into(mDisplayImage);
      //  mDisplayImage.setImageURI(firebaseStorageRef.child("Photos").getFile("crater_lake.jpg"));

    }
}
