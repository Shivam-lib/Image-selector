package com.brothers.sample;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.brothers.image_selector.ImageSelector;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    private static final int REQUEST_CODE_IMAGE_SELECTOR = 1;
    ImageView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSelector.from(MainActivity.this).requestCode(REQUEST_CODE_IMAGE_SELECTOR).show();
            }
        });


        test = findViewById(R.id.image_view);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_SELECTOR && resultCode == RESULT_OK) {
            ArrayList<String> selectedImagePaths = data.getStringArrayListExtra(ImageSelector.EXTRA_RESULT_SELECTION_PATH);

            if (selectedImagePaths != null && !selectedImagePaths.isEmpty()) {
                String imagePath = selectedImagePaths.get(0);
//                Glide.with(this)
//                        .load(imagePath)
//                        .into(test);
            }
        }
    }
}