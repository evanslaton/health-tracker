package com.evanslaton.health_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// https://github.com/NJCrain/health-tracker/blob/master/app/src/main/java/com/njcrain/android/healthtracker/activity/ProfileActivity.java
// https://developer.android.com/training/camera/photobasics
// https://developer.android.com/training/permissions/requesting
public class Profile extends AppCompatActivity {
    private final int MY_PERMISSIONS_ID = 0;
    private boolean CAMERA_PERMISSION_GRANTED = false;
    private boolean FILES_PERMISSION_GRANTED = false;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE = 2;
    private int REQUEST_TAKE_PHOTO = 3;
    String currentPhotoPath = null;
    private ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profilePic = findViewById(R.id.profilePicture);
        updateProfilePicture(currentPhotoPath);
    }

    // Allows the user to take and save a new photo
    public void takePicture(View v) {
        // Checks to see if required permission have been granted
        checkForPermissions();
        // If required permission have been granted, invoke intent to take picture
        if (CAMERA_PERMISSION_GRANTED
                && FILES_PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException error) {
                    // Error occurred while creating the File
                    Log.i("ErrorCreatingFile", error.toString());
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    // Creates a collision-resistant file name for the photo that was taken
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",   // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // https://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore
    // Converts the image URI gotten from the user selecting an image file to a String of the pathway to that image
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
    public void chooseProfilePicFromFiles(View v) {
        // Checks to see if the user has granted permissions to access the files
        checkForPermissions();
        if (FILES_PERMISSION_GRANTED) {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);
        }
    }

    // Updates the profilePicture ImageView with a new photo or one from the user's files
    // Automatically invoked after a user interaction
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the interaction was taking a photo
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            updateProfilePicture(currentPhotoPath);
            // If the interaction was selecting a photo from files
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri imagePath = data.getData();
            currentPhotoPath = getRealPathFromURI(this, imagePath);
            updateProfilePicture(currentPhotoPath);
        }
    }

    // Changes the profilePicture to be what the user set it to
    private void updateProfilePicture(String imagePath) {
        if (imagePath == null) {
            profilePic.setImageResource(R.drawable.default_profile_pic);
        } else {
            File image = new File(imagePath);
            Bitmap avatarImage = BitmapFactory.decodeFile(image.getAbsolutePath());
            profilePic.setImageBitmap(avatarImage);
            // Saves the filepath to sharedPreferences
            saveProfilePic(currentPhotoPath);
        }
    }

    // Checks to see if the user has granted necessary permissions
    public void checkForPermissions() {
        // Checks to see if the user has already granted CAMERA and WRITE_EXTERNAL_STORAGE permission
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)) {

            // Request the permissions if not
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_ID);
        } else {
            // 'Remembers' if permission has been granted
            CAMERA_PERMISSION_GRANTED = true;
            FILES_PERMISSION_GRANTED = true;
        }
    }

    // Automatically invoked after the user responds to the permission request in checkForPermissions()
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ID: {
                // If request is cancelled, the result arrays are empty
                // Checks to see if CAMERA permission was granted
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // CAMERA permission was granted
                    CAMERA_PERMISSION_GRANTED = true;
                }

                // Checks to see if WRITE_EXTERNAL_STORAGE permission was granted
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // WRITE_EXTERNAL_STORAGE permission was granted
                    FILES_PERMISSION_GRANTED = true;
                }
            }
        }
    }

    // Saves user's profile pic
    public void saveProfilePic(String imagePath) {
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.profile_pic), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.profile_pic), imagePath);
        editor.commit();
    }
}
