package com.brothers.image_selector.ui.Fragment;

import static com.brothers.image_selector.ui.ImageSelectorActivity.button_apply;
import static com.brothers.image_selector.ui.ImageSelectorActivity.selectedImage;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brothers.image_selector.R;
import com.brothers.image_selector.ui.Adapter.ImageAdapter;
import com.brothers.image_selector.ui.ImageModel.ImageModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllImageFragment extends Fragment {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGES = 2;
    private RecyclerView recyclerView;
    private static ImageAdapter imageAdapter;
    private ArrayList<ImageModel> imageList;
    public static ArrayList<ImageModel> selectedImages;
    ArrayList<String> selectedImageList;
    String[] projection = {MediaStore.Images.Media.DATA};
    String mCurrentPhotoPath;
    int[] resImg = {R.drawable.ic_camera_white_30dp};
    String[] title = {"Camera"};
    File image;
    private ActivityResultLauncher<Intent> imageCaptureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (mCurrentPhotoPath != null) {
                            addImage(mCurrentPhotoPath);
                        }
                    }
                }
            });



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_image, container, false);

        recyclerView = view.findViewById(R.id.image_recycler_view);
        imageList = new ArrayList<>();
        selectedImages = new ArrayList<>();
        getAllImages();
        setImageList();
        return view;
    }

    public void getAllImages() {
        imageList.clear();
        String[] projection = {MediaStore.MediaColumns.DATA};

        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                ImageModel imageModel = new ImageModel();
                imageModel.setImage(absolutePathOfImage);
                imageList.add(imageModel);
            }
            cursor.close();
//            imageAdapter.notifyDataSetChanged();
        }
    }

    public void setImageList() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        imageAdapter = new ImageAdapter(getContext(), imageList);
        recyclerView.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (position == 0) {
                    takePicture();
                } else {
                    try {
                        if (!imageList.get(position).isSelected) {
                            selectImage(position);
                            selectedImage.add(imageList.get(position).getImage());
                        } else {
                            unSelectImage(position);
                            selectedImage.remove(imageList.get(position).getImage());
                        }
                    } catch (ArrayIndexOutOfBoundsException ed) {
                        ed.printStackTrace();
                    }
                    updateImportButton();
                }
            }
        });
        setImagePickerList();
    }

    private void unSelectImage(int position) {
        ImageModel imageModel = imageList.get(position);
        if (selectedImages.contains(imageModel)) {
            imageModel.setSelected(false);
            selectedImages.remove(imageModel);
            imageAdapter.notifyDataSetChanged();
        }
    }

    public void selectImage(int position) {
        ImageModel imageModel = imageList.get(position);
        if (!selectedImages.contains(imageModel)) {
            imageModel.setSelected(true);
            selectedImages.add(imageModel);
            imageAdapter.notifyDataSetChanged();
        }
    }

    public static void resetSelection() {
        selectedImage.clear();
        selectedImages.clear();
        imageAdapter.notifyDataSetChanged();
        updateImportButton();
    }
    public static void updateImportButton() {
        int selectedCount = selectedImages.size();
        if (selectedCount > 0) {
            String importText = "Import (" + selectedCount + ")";
            // Assuming the button_apply TextView is declared and initialized in your activity
            button_apply.setText(importText);
        } else {
            // Assuming the button_apply TextView is declared and initialized in your activity
            button_apply.setText("Import");
        }
    }
    public void setImagePickerList() {
        for (int i = 0; i < resImg.length; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setResImg(resImg[i]);
            imageModel.setTitle(title[i]);
            imageList.add(i, imageModel);
        }
        imageAdapter.notifyDataSetChanged();
    }

    public void takePicture() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = createImageFile();
        if (photoFile != null) {
            Uri photoUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            imageCaptureLauncher.launch(cameraIntent);
        }
    }

    public File createImageFile() {
        String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + dateTime + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    addImage(mCurrentPhotoPath);
                }
            } else if (requestCode == PICK_IMAGES) {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        getImageFilePath(uri);
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    getImageFilePath(uri);
                }
            }
        }
    }
    // Get image file path
    public void getImageFilePath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                if (absolutePathOfImage != null) {
                    checkImage(absolutePathOfImage);
                } else {
                    checkImage(String.valueOf(uri));
                }
            }
        }
    }

    // add image in selectedImageList and imageList
    public void checkImage(String filePath) {
        if (!selectedImageList.contains(filePath)) {
            for (int pos = 0; pos < imageList.size(); pos++) {
                if (imageList.get(pos).getImage() != null) {
                    if (imageList.get(pos).getImage().equalsIgnoreCase(filePath)) {
                        imageList.remove(pos);
                    }
                }
            }
            addImage(filePath);
        }
    }

    // add image in selectedImageList and imageList
    public void addImage(String filePath) {
        ImageModel imageModel = new ImageModel();
        imageModel.setImage(filePath);
        imageModel.setSelected(true);
        imageList.add(2, imageModel);
        selectedImages.add(imageModel);
        imageAdapter.notifyDataSetChanged();

        selectedImage.add(filePath);
        updateImportButton();
    }
}