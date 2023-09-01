package com.brothers.image_selector.FullImage;

import static com.brothers.image_selector.ui.Fragment.AllImageFragment.selectedImages;
import static com.brothers.image_selector.ui.Fragment.AllImageFragment.updateImportButton;
import static com.brothers.image_selector.ui.Fragment.FolderImageFragment.importCount;
import static com.brothers.image_selector.ui.ImageSelectorActivity.selectedImage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.brothers.image_selector.R;
import com.brothers.image_selector.SelectionSpec.SelectionSpec;
import com.brothers.image_selector.ui.ImageModel.ImageModel;
import com.bumptech.glide.Glide;

public class FullImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private boolean isChecked = false;
    CheckBox checkbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int themeId = SelectionSpec.getInstance().getTheme();
        // Set the theme to the activity
        setTheme(themeId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        imageView = findViewById(R.id.imageView);
//        checkbox = findViewById(R.id.checkbox);

        // Retrieve the image URL from the intent
        String imageUrl = getIntent().getStringExtra("image_url");
        String imagePath = getIntent().getStringExtra("imagePath");

        // Load and display the full image using Glide
        if (imageUrl != null) {
            // Load and display the image from URL using Glide
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        } else if (imagePath != null) {
            // Load and display the image from local path using Glide
            Glide.with(this)
                    .load(imagePath)
                    .into(imageView);
        }

        // Check if the image is already selected
//        if (imageUrl != null && selectedImage.contains(imageUrl)) {
//            isChecked = true;
//        } else if (imagePath != null && selectedImage.contains(imagePath)) {
//            isChecked = true;
//        } else {
//            isChecked = false;
//        }



        // Set the initial checked state of the checkbox
//        checkbox.setChecked(isChecked);
//        checkbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isChecked = checkbox.isChecked();
//
//                if (isChecked) {
//                    // Add the image to the selectedImage list
//                    if (imageUrl != null) {
//                        // Load and display the image from URL using Glide
//                        selectedImage.add(imageUrl);
//                        ImageModel imageModel = new ImageModel();
//                        selectedImages.add(imageModel);
//                        updateImportButton();
//                    } else if (imagePath != null) {
//                        // Load and display the image from local path using Glide
//                        selectedImage.add(imagePath);
//                        importCount++;
//                    }
//
//                } else {
//                    // Remove the image from the selectedImage list
//                    if (imageUrl != null) {
//                        // Load and display the image from URL using Glide
//                        selectedImage.remove(imageUrl);
//                        ImageModel imageModel = new ImageModel();
//                        selectedImages.remove(imageModel);
//                        updateImportButton();
//                    } else if (imagePath != null) {
//                        // Load and display the image from local path using Glide
//                        selectedImage.remove(imagePath);
//                        importCount--;
//                    }
//                }
//
//            }
//        });

    }

//    @Override
//    public void onBackPressed() {
//        // Set the result based on the checked state of the checkbox
//        isChecked = checkbox.isChecked();
//
//        // Set the result based on the checked state of the checkbox
//        if (isChecked) {
//            setResult(RESULT_OK);
//        } else {
//            setResult(RESULT_CANCELED);
//        }
//        super.onBackPressed();
//    }

}