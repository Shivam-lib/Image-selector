package com.brothers.image_selector.ui.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brothers.image_selector.R;
import com.brothers.image_selector.ui.FolderData.FolderData;
import com.brothers.image_selector.ui.ImageSelectorActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private List<FolderData> folderList;
    private Context context;

    public FolderAdapter(List<FolderData> folderList, Context context) {
        this.folderList = folderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FolderData folder = folderList.get(position);
        holder.bind(folder);
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView folderNameTextView;
        private ImageView firstImageView;
        private TextView itemCountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderNameTextView = itemView.findViewById(R.id.album_name);
            firstImageView = itemView.findViewById(R.id.album_cover);
            itemCountTextView = itemView.findViewById(R.id.album_media_count);

            itemView.setOnClickListener(this);
        }

        public void bind(FolderData folder) {
            folderNameTextView.setText(folder.getFolderName());

            // Get the count of items in the folder
            int itemCount;

            itemCount = getFolderItemCount(folder.getFolderPath());
            itemCountTextView.setText(String.valueOf(itemCount));

            // Get the path of the first image in the folder
            String firstImagePath = getFirstImagePath(folder.getFolderPath());

            // Load and display the first image using Glide library
            Glide.with(itemView.getContext())
                    .load(firstImagePath)
                    .into(firstImageView); // Replace "yourImageView" with the actual ImageView in your layout


            // Add any additional UI modifications or click listeners if needed
        }

        private int getFolderItemCount(String folderPath) {
            int count = 0;
            Cursor cursor = itemView.getContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + " LIKE ? AND " + MediaStore.Images.Media.DATA + " NOT LIKE ?",
                    new String[]{folderPath + "/%", folderPath + "/%/%"},
                    null
            );

            if (cursor != null) {
                count = cursor.getCount();
                cursor.close();
            }

            return count;
        }

        private String getFirstImagePath(String folderPath) {
            String firstImagePath = null;
            Cursor cursor = itemView.getContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA},
                    MediaStore.Images.Media.DATA + " like ? AND " + MediaStore.Images.Media.DATA + " NOT LIKE ?",
                    new String[]{folderPath + "/%", folderPath + "/%/%"},
                    MediaStore.Images.Media.DATE_MODIFIED + " DESC"
            );

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    firstImagePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }

            return firstImagePath;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                FolderData folder = folderList.get(position);
                openImageGridActivity(itemView.getContext(), folder.getFolderPath(), folder.getFolderName());
            }
        }
    }

    private void openImageGridActivity(Context context, String folderPath, String folderName) {
        if (context instanceof ImageSelectorActivity) {
            ImageSelectorActivity activity = (ImageSelectorActivity) context;
            activity.showImageGridFragment(folderPath, folderName);
        }
        // Close the dropdown menu
        if (context instanceof ImageSelectorActivity) {
            ImageSelectorActivity activity = (ImageSelectorActivity) context;
            activity.hideDropdown();
        }
    }
}
