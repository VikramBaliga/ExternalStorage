package projects.android.my.externalstorage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    ImageView loadedImage;
    String fname = "baymax.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find imageview to display
        loadedImage = (ImageView) findViewById(R.id.loadedImage);

        // check for permission to read and write external device
        String[] externalStoragePermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            //Request Permission for reading and writting of external storage

            ActivityCompat.requestPermissions(this,externalStoragePermissions,100);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Action on permission reqiest
        if(requestCode == 100)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
                //Check if external Storage is there
            }
        }
    }

    public void SaveImage(View view)
    {
        //Get image to save
        Bitmap imageToSave = BitmapFactory.decodeResource(getResources(),R.drawable.picdemo);

        //get path to external storeage
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/resources");
        myDir.mkdirs();

        // check if file exists if present delete
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try
        {
            //create file
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e)
        {
           Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this,"Image Saved",Toast.LENGTH_LONG).show();
    }

    public void LoadImage(View view)
    {

        //get file from external
        File sdcard = Environment.getExternalStorageDirectory();
        File imgFile = new File(sdcard+"/resources",fname);

        if(imgFile.exists())
        {
            //Display in image view
            Bitmap imgBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            loadedImage.setImageBitmap(imgBitmap);
        }

    }
}