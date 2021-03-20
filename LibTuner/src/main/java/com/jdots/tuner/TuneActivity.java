package com.jdots.tuner;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TuneActivity extends AppCompatActivity implements EditImageFragment.EditImageFragmentListener, RotateImage.OnFragmentInteractionListener{


    @BindView(R2.id.viewpager)
    ViewPager viewPager;

    @BindView(R2.id.image_preview)
    ImageView imageView;

    @BindView(R2.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;


    private float rotation = 0;
    Bitmap bitmap;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int SELECT_A_PHOTO = 2;

    Button btn_take;
    Button btn_list;
    Button btn_load;
    Button btn_wallpaper;
    ImageView imageView_photo;

    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initializeButtons();
        ButterKnife.bind(this);
        toolbar();


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setContentView(R.layout.activity_main);
        //finishAffinity();
        finish();
        System.exit(0);
    }
    public void toolbar(){

        bitmap = BitmapFactory.decodeResource(getResources(),R.id.image_preview);



        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Edit Image"));
        tabLayout.addTab(tabLayout.newTab().setText("Rotate Image"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewpager = findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView img= (ImageView) findViewById(R.id.image_preview);
        img.setImageResource(R.drawable.paint_1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {

            imageView.setImageBitmap(viewToBitmap(imageView, imageView.getWidth(), imageView.getHeight()));

            try {
                galleryAddPic();
            } catch (IOException e) {
                //Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                resetColorFilter();
            }
            return true;
        }
        if (id == R.id.action_open){
            resetColorFilter();
            resetSliders();
            openImageFromGallery();
            return true;
        }
        if (id == R.id.action_back){

          finish();
          System.exit(0);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void openImageFromGallery() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, SELECT_A_PHOTO);
                            //Toast.makeText(getApplicationContext(),"Select a photo!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void initializeButtons(){
    //    btn_take = findViewById(R.id.action_takePicture);
        btn_list = findViewById(R.id.action_save);
        btn_load = findViewById(R.id.action_open);
       // btn_wallpaper = findViewById(R.id.WALLPAPER);

        imageView_photo = findViewById(R.id.image_preview);
    }

    @Override
    public void onBackPressed() {
       try {
           finish();
           System.exit(0);
       }catch(Exception ignored){}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            ImageView iv_photo;
            iv_photo = findViewById(R.id.image_preview);

            Glide.with(this).load(currentPhotoPath).into(iv_photo);
        }

        if (requestCode == SELECT_A_PHOTO && resultCode == RESULT_OK){
            Uri selectedPhoto = data.getData();
            ImageView iv_photo;
            iv_photo = findViewById(R.id.image_preview);

            Glide.with(this).load(selectedPhoto).into(iv_photo);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Toast.makeText(getApplicationContext(), "Take a Picture!", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Something went wrong. Could not create file.", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android_photo_editor_2k19.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "paint_studio_image_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/PaintStudio/");
        File image = File.createTempFile(
                imageFileName,          /* prefix */
                ".jpg",         /* suffix */
                storageDir           /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents



        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() throws IOException {


            BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = draw.getBitmap();

            FileOutputStream outStream = null;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

                    +"/PaintStudio/");

        try{
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }catch(Exception ignored){}


        Dexter.withContext(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(getApplicationContext(), "No Permission", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();


        if (!dir.exists()) {
           dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/");
        }


            String fileName = "paint_studio_"+System.currentTimeMillis()+".jpg";
            File outFile = new File(dir, fileName);
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            Toast.makeText(this, "Picture is saved", Toast.LENGTH_SHORT).show();
        }





    public Bitmap viewToBitmap(View view, int width, int height) {

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void shareButton(){

        String text = "Look at my awesome picture";
        Uri pictureUri = Uri.parse("Android/data/com.example.android_photo_editor_2k19/files/Pictures/" + this);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM,  pictureUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT,  text);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent,"Share via"));
    }

    public void resetColorFilter(){
        imageView.setColorFilter(new ColorMatrixColorFilter(
                new float[]{
                        1, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0}
        ));
    }

    public void rotateBitmap(float degrees){

        imageView.setRotation(rotation + degrees);
        rotation += degrees;

    }

    public void resetSliders(){
        SeekBar b = (SeekBar)findViewById(R.id.seekbar_brightness);
        b.setProgress(100);
        SeekBar c = (SeekBar)findViewById(R.id.seekbar_contrast);
        c.setProgress(0);
        SeekBar s = (SeekBar)findViewById(R.id.seekbar_saturation);
        s.setProgress(255);
    }

    public void onClickRight(View v) {

        rotateBitmap(90);
    }
    public void onClickLeft(View v){
        rotateBitmap(-90);
    }



}
