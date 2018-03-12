package bharat.otouch.www.solutionexecutive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by shaan on 08/07/2017.
 */

public class OpenAccount extends AppCompatActivity implements View.OnClickListener {

    EditText mname, mmobile, memail, mpassword;
    Button msubmit;
    String name, mobile, email, pass;
    ImageView mimage;
    private static int RESULT_LOAD_IMG = 1;
    String encodedImage, imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openaccount);

        mname = (EditText) findViewById(R.id.name);
        mmobile = (EditText) findViewById(R.id.phone);
        memail = (EditText) findViewById(R.id.email);
        msubmit = (Button) findViewById(R.id.submit);
        mimage = (ImageView) findViewById(R.id.openimage);
        mpassword = (EditText) findViewById(R.id.password);

        msubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {
            register();
        }
    }

    public void register() {
        //validation

        if (mname.getText().toString().length() == 0) {
            mname.setError("Name can not be blanked");
            return;
        } else if (mmobile.getText().toString().length() == 0) {
            mmobile.setError("Fill mobile number");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(memail.getText().toString()).matches()) {//Validation for Invalid Email Address
            memail.setError("Invalid Email");
            return;
        } else if(mpassword.getText().toString().length() ==0){
            mpassword.setError("Invalid Password");
            return;
        }
        else {
            Toast.makeText(this, "All Fields Validated", Toast.LENGTH_SHORT).show();
        }
//validation END
        name = mname.getText().toString().trim();
        mobile = mmobile.getText().toString().trim();
        email = memail.getText().toString().trim();
        pass = mpassword.getText().toString().trim();
      //  Toast.makeText(this, "result" + name + " " + mobile + " " + email+""+ pass, Toast.LENGTH_SHORT).show();
        if (isOnline()) {
            String method = "register";
            Toast.makeText(this, "connection is ok", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "bt start");
            BackgroundTask backgroundTask = new BackgroundTask(OpenAccount.this);
            backgroundTask.execute(method, name, mobile, email, pass, encodedImage);
            Log.d("TAG", "bt end");

        } else {
            Toast.makeText(OpenAccount.this, "Connection is Offline", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    public void takePicture(View View) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//MediaStore is type of dtabse whwere image and video storeed.
      /*  imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Test.jpg");//directory path and file name two argument in file
        Toast.makeText(LabourRegistration.this, "Picture Clicked" + imageFile, Toast.LENGTH_SHORT).show();

        //generate path Uri
        Uri value = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, value);//Extraoutput show path for saving file
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//define image quality 0 for low and 1 for high quality
        */
        startActivityForResult(intent, 0);
    }

    public void browseImage(View v) {

// Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra("crop", "true");
        galleryIntent.putExtra("outputX", 200);
        galleryIntent.putExtra("outputY", 260);
        galleryIntent.putExtra("aspectX", 1);
        galleryIntent.putExtra("aspectY", 1);
        galleryIntent.putExtra("scale", true);
        galleryIntent.putExtra("return-data", true);
// Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == RESULT_LOAD_IMG) {
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Toast.makeText(this, "Picture saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                        Toast.makeText(OpenAccount.this, "ImageSet", Toast.LENGTH_SHORT).show();
                        mimage.setImageBitmap(thumbnail);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        if (thumbnail != null) {
                            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object//0 for low quality
                        }
                        byte[] b = baos.toByteArray();
                        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        Toast.makeText(OpenAccount.this, "Wait for moment ....", Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                     //   Toast.makeText(this, "Activity.RESULT_CANCELLED", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;


                }

            }//onActivityCamera-END
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                if (cursor != null) {
                    cursor.moveToFirst();
                }

                int columnIndex = 0;
                if (cursor != null) {
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                }
                if (cursor != null) {
                    imgDecodableString = cursor.getString(columnIndex);
                }
                if (cursor != null) {
                    cursor.close();
                }
                // Set the Image in ImageView after decoding the String
                mimage.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                //imageUploadSTART

                Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object//0 for low quality
                byte[] b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                Toast.makeText(OpenAccount.this, "ImageSet", Toast.LENGTH_SHORT).show();
                Toast.makeText(OpenAccount.this, "Wait for moment ....", Toast.LENGTH_SHORT).show();
                Log.d("error", "images" + encodedImage);
                //close
            }
        } catch (Exception e) {
            Toast.makeText(this, "Problem Detected!", Toast.LENGTH_LONG)
                    .show();
        }

    }

}



