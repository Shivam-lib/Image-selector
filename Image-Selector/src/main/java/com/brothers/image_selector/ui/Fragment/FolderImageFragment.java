package com.brothers.image_selector.ui.Fragment;


import static com.brothers.image_selector.ui.ImageSelectorActivity.button_apply;
import static com.brothers.image_selector.ui.ImageSelectorActivity.selectedImage;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brothers.image_selector.R;
import com.brothers.image_selector.ui.Adapter.FolderImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class FolderImageFragment extends Fragment implements FolderImageAdapter.CheckBoxClickListener{
    private String folderName;
    private String folderPath;

    private FolderImageAdapter imageAdapter;
    private List<String> imagePathList;
    RecyclerView imagesRecyclerview;
    public static int importCount = 0;

    public static FolderImageFragment newInstance(String folderPath, String folderName) {
        FolderImageFragment fragment = new FolderImageFragment();
        Bundle args = new Bundle();
        args.putString("folderPath", folderPath);
        args.putString("folderName", folderName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            folderPath = getArguments().getString("folderPath");
            folderName = getArguments().getString("folderName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder_image, container, false);



        imagePathList = getImagePaths(folderPath);

        imagesRecyclerview = view.findViewById(R.id.image_recyclerview);
        imagesRecyclerview.setLayoutManager(new GridLayoutManager(requireContext(),3));
        imageAdapter = new FolderImageAdapter(imagePathList, requireContext());
        imageAdapter.setCheckBoxClickListener(this);
        imagesRecyclerview.setAdapter(imageAdapter);

        return view;
    }

    @Override
    public void onCheckBoxClick(int position, boolean isChecked) {
        String imagePath = imagePathList.get(position);

        if (isChecked) {
            // Checkbox is checked, add the image to the selectedImages list
            selectedImage.add(imagePath);
            importCount++;
        } else {
            // Checkbox is unchecked, remove the image from the selectedImages list
            selectedImage.remove(imagePath);
            importCount--;
        }

        updateImportButton();
    }

    private void updateImportButton() {
        String importText = "Import (" + importCount + ")";
        button_apply.setText(importText);
    }

    private List<String> getImagePaths(String folderPath) {
        List<String> paths = new ArrayList<>();

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.DATA + " LIKE ? AND " + MediaStore.Images.Media.DATA + " NOT LIKE ?";
        String[] selectionArgs = {folderPath + "/%", folderPath + "/%/%"};
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";

        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );

        if (cursor != null) {
            while (cursor.moveToNext()) { String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                paths.add(imagePath);
            }
            cursor.close();
        }

        return paths;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Reset the selection in AllImageFragment when exiting FolderImageFragment
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
            if (fragment instanceof AllImageFragment) {
                AllImageFragment allImageFragment = (AllImageFragment) fragment;
                allImageFragment.resetSelection();
            }
        }
    }
}