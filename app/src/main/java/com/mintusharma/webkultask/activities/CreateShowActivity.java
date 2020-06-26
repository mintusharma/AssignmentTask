package com.mintusharma.webkultask.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mintusharma.webkultask.R;
import com.mintusharma.webkultask.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreateShowActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String imageEncoded;
    List<String> imagesEncodedList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    ViewPager mViewPager;
    boolean for_image = false;
    boolean for_video = false;
    ImageAdapter adapterView;
    private VideoView mVideo;
    Button loadFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_show);
        setTitle("Show Created");
        Toolbar toolbar = findViewById(R.id.toolbar_more_details);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initView();
    }

    private void initView() {
        imagesEncodedList = new ArrayList<>();
        mVideo = findViewById(R.id.video_view);
        imageView = findViewById(R.id.imageView2);
        loadFile = findViewById(R.id.buttonLoadFile);
        loadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (for_image) {
                    openImageGallery();
                } else if (for_video) {
                    openVideoGallery();
                }
            }
        });
        showDialogForCreateShow();

    }

    private void showDialogForCreateShow() {
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = LayoutInflater.from(this).inflate(R.layout.create_show_dialog,null, false);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        Button select_image = view.findViewById(R.id.download);
        Button select_video = view.findViewById(R.id.cancel);
        TextView close_dialog = view.findViewById(R.id.close_dialog);

        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for_image = true;
                openImageGallery();
                // new CreateShowActivity().openGallery(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Select Images..", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        select_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for_video = true;
                openVideoGallery();
                Toast.makeText(getApplicationContext(), "Select Video..", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    public void openImageGallery() {
        imageView.setVisibility(View.VISIBLE);
        mVideo.setVisibility(View.GONE);
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(gallery, PICK_IMAGE);
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
    }

    public void openVideoGallery() {
        imageView.setVisibility(View.GONE);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }*/
        loadFile.setVisibility(View.VISIBLE);
        if (for_image) {
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
                System.out.println("uriiii " + imageUri);
            }
        } else if (for_video) {
            // When an Image is picked
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                try {
                    Uri vidFile = data.getData();
                    mVideo.setVideoURI(vidFile);
                    mVideo.setMediaController(new MediaController(this));
                    mVideo.setVisibility(View.VISIBLE);
                    mVideo.bringToFront();
                    mVideo.requestFocus();
                    mVideo.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
