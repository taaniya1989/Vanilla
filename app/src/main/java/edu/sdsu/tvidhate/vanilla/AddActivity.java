package edu.sdsu.tvidhate.vanilla;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.sql.Timestamp;
import java.io.File;

public class AddActivity extends AppCompatActivity implements View.OnClickListener
{

    private StorageReference mStorage;
    private ProgressBar mProgressBar;
    private Uri mUri;
    private ImageView selectedImage;
    private TextView placeName;
    private String photoPath;

    private static final int IMAGE_SELECTED_RESULT = 2;
    private static final int IMAGE_CAPTURED_RESULT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button uploadImageButton = findViewById(R.id.uploadImageButton);
        Button selectImageButton = findViewById(R.id.selectImageButton);
        ImageButton openCameraButton = findViewById(R.id.openCameraButton);
        selectedImage = findViewById(R.id.selectedImage);
        placeName = findViewById(R.id.placeName);

        openCameraButton.setOnClickListener(this);
        selectImageButton.setOnClickListener(this);
        uploadImageButton.setOnClickListener(this);

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressBar = findViewById(R.id.uploadProgressBar);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_SELECTED_RESULT && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            mUri = data.getData();
            selectedImage.setImageURI(mUri);
            Log.i("VANILLA_INFO",mUri.toString());
        }
        if(requestCode == IMAGE_CAPTURED_RESULT && resultCode == RESULT_OK
                && data != null ) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mUri = data.getData();
            //selectedImage.setImageURI(mUri);
            selectedImage.setImageBitmap(photo);

            Log.i("VANILLA_INFO",photo.toString());
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.selectImageButton:
                Intent selectImageFromGallery = new Intent(Intent.ACTION_GET_CONTENT);

                selectImageFromGallery.setType("image/*");
                startActivityForResult(selectImageFromGallery, IMAGE_SELECTED_RESULT);
                break;

            case R.id.uploadImageButton:
                mProgressBar.setVisibility(View.INVISIBLE);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                photoPath = placeName.getText().toString().replaceAll(" ", "_").toLowerCase()
                        +"_"+String.valueOf(timestamp.getTime())+"_"+user.getUid().toString().toLowerCase()+".jpg";

                Log.i("PhotoPath",photoPath);
                StorageReference filePath = mStorage.child("Photos").child(photoPath);

                filePath.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Uploaded Image",Toast.LENGTH_LONG).show();
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
                finish();
                break;

            case R.id.openCameraButton:
                Intent getImageFromCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //getImageFromCamera.setType("images/*");
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myImage.jpg");
                Uri outputFileUri = Uri.fromFile(file);

                //cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getImageFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(getImageFromCamera,IMAGE_CAPTURED_RESULT);
                break;

        }
    }
}
