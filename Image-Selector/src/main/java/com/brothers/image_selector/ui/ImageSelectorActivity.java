package com.brothers.image_selector.ui;

import static com.brothers.image_selector.ImageSelector.EXTRA_RESULT_SELECTION_PATH;
import static com.brothers.image_selector.ui.Fragment.FolderImageFragment.importCount;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.brothers.image_selector.R;
import com.brothers.image_selector.SelectionSpec.SelectionSpec;
import com.brothers.image_selector.ui.Adapter.FolderAdapter;
import com.brothers.image_selector.ui.FolderData.FolderData;
import com.brothers.image_selector.ui.Fragment.AllImageFragment;
import com.brothers.image_selector.ui.Fragment.FolderImageFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageSelectorActivity extends AppCompatActivity {

    public static Button button_apply;
    private RecyclerView dropdownRecyclerView;
    private List<FolderData> folderList;
    private PopupWindow popupWindow;
    private FolderAdapter adapter;
    private boolean isDropdownVisible = false;
    public static TextView selectedAlbumTextView;
    public static List<String> selectedImage;
    public static Toolbar toolbar;
    private SelectionSpec mSpec;


    public ImageSelectorActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int themeId = SelectionSpec.getInstance().getTheme();
        // Set the theme to the activity
        setTheme(themeId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_up_white);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform action when home/up button is clicked
                finish();
            }
        });

        button_apply = findViewById(R.id.button_apply);

        selectedImage = new ArrayList<>();
        button_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                importCount = 0;
                resultIntent.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, (ArrayList<String>) selectedImage);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


        AllImageFragment fragment = new AllImageFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        folderList = new ArrayList<>();

        selectedAlbumTextView = findViewById(R.id.selected_album);

        selectedAlbumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDropdownVisible) {
                    showDropdown();
                } else {
                    hideDropdown();
                }
            }
        });


        dropdownRecyclerView = createDropdownRecyclerView();
        retrieveImageFolders();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dropdownRecyclerView.setLayoutManager(layoutManager);
        adapter = new FolderAdapter(folderList, this);
        dropdownRecyclerView.setAdapter(adapter);
    }

    public void showImageGridFragment(String folderPath, String folderName) {
        // Create a new instance of the ImageGridFragment and pass the folderPath and folderName as arguments
        FolderImageFragment fragment = FolderImageFragment.newInstance(folderPath, folderName);
        AllImageFragment.resetSelection();
        // Replace the current fragment with the new ImageGridFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        toolbar.setTitle(folderName);

        // Hide the selected album text view
        selectedAlbumTextView.setVisibility(View.GONE);
    }

    private void retrieveImageFolders() {
        folderList.clear();


        Set<String> folderPaths = new HashSet<>();

        String[] projection = {MediaStore.Images.Media.DATA};
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        );


        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(columnIndex);
                File imageFile = new File(imagePath);
                File parentFolder = imageFile.getParentFile();
                if (parentFolder != null) {
                    String folderPath = parentFolder.getAbsolutePath();
                    if (!folderPaths.contains(folderPath)) {
                        folderPaths.add(folderPath);
                        FolderData folder = new FolderData(parentFolder.getName(), folderPath, 0);
                        folderList.add(folder);
                    }
                }
            }
            cursor.close();
        }

        // Sort the folderList alphabetically
        Collections.sort(folderList, (folder1, folder2) ->
                folder1.getFolderName().compareToIgnoreCase(folder2.getFolderName()));

//        adapter.notifyDataSetChanged();

    }

    private RecyclerView createDropdownRecyclerView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dropdownView = inflater.inflate(R.layout.dropdown_menu_layout, null);
        RecyclerView recyclerView = dropdownView.findViewById(R.id.dropdown_recyclerView);

        popupWindow = new PopupWindow(dropdownView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(null);

        return recyclerView;
    }

    private void showDropdown() {
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(selectedAlbumTextView);
        isDropdownVisible = true;

        // Dismiss the dropdown when clicked outside
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideDropdown();
            }
        });
    }

    public void hideDropdown() {
        popupWindow.dismiss();
        isDropdownVisible = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Reset the toolbar title to its initial state
        toolbar.setTitle("");

        importCount = 0;
        // Show the selected album text view
        selectedAlbumTextView.setVisibility(View.VISIBLE);
    }

}