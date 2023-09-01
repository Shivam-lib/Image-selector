package com.brothers.image_selector;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import com.brothers.image_selector.ui.ImageSelectorActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class ImageSelector {

    private Activity activity;
    public static final String EXTRA_RESULT_SELECTION_PATH = "extra_result_selection_path";
    private int requestCode;

    private ImageSelector(Activity activity) {
        this.activity = activity;
    }

    public static ImageSelector from(Activity activity) {
        return new ImageSelector(activity);
    }

    public ImageSelector requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public void show() {
        Dexter.withContext(activity)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            startImageSelectorActivity();
                        } else {
                            // Handle permission denial
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        // Handle permission rationale
                    }
                }).check();
    }

    private void startImageSelectorActivity() {
        Intent intent = new Intent(activity, ImageSelectorActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

}
