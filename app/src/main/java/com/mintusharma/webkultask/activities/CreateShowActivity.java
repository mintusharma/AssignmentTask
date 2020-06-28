package com.mintusharma.webkultask.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.mintusharma.webkultask.adapter.SliderAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateShowActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    List<String> imagesEncodedList;
    private AlertDialog dialog;
    boolean for_image = false;
    boolean for_video = false;
    private VideoView mVideo;
    Button loadFile;

    ImageView showImages;
    List<Bitmap> bitmapList;
    ViewPager2 viewPager;
    private Handler handler = new Handler();


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
        loadFile = findViewById(R.id.buttonLoadFile);
        showImages = findViewById(R.id.imageView);
        viewPager = findViewById(R.id.viewpager);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = LayoutInflater.from(this).inflate(R.layout.create_show_dialog, null, false);
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
                Toast.makeText(getApplicationContext(), "Select 2 or more Picture", Toast.LENGTH_LONG).show();
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
        viewPager.setVisibility(View.VISIBLE);
        mVideo.setVisibility(View.GONE);
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
    }

    public void openVideoGallery() {
        viewPager.setVisibility(View.GONE);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadFile.setVisibility(View.VISIBLE);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            if (for_image) {
                loadImages(data);
            } else if (for_video) {
                //get the video from gallery and play automatically
                loadVideo(data);
            }
        }
    }

    private void loadVideo(Intent data) {
        if (data != null) {
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
        } else {
            Toast.makeText(getApplicationContext(), "Select Video..", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImages(Intent data) {
        bitmapList = new ArrayList<>();
        bitmapList.clear();
        if (Objects.requireNonNull(data.getClipData()).getItemCount() <= 5) {
            if(data.getClipData().getItemCount()!=1) {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    //multiple images selecetd
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        Log.d("URI", imageUri.toString());
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            bitmapList.add(bitmap);
                        } catch (Exception e) {
                            Log.e("MainActivity", Objects.requireNonNull(e.getMessage()));
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Images..", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Select more than 1 Images..", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Select only 5 Images..", Toast.LENGTH_SHORT).show();
        }

        SliderAdapter imageAdapter = new SliderAdapter(getApplicationContext(), bitmapList, viewPager);
        viewPager.setAdapter(imageAdapter);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager.setPageTransformer(compositePageTransformer);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(sliderRunnable);
                handler.postDelayed(sliderRunnable, 4000);
            }
        });

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
