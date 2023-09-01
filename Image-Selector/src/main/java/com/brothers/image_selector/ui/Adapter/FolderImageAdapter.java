package com.brothers.image_selector.ui.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brothers.image_selector.FullImage.FullImageActivity;
import com.brothers.image_selector.R;
import com.brothers.image_selector.ui.widget.CheckView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FolderImageAdapter extends RecyclerView.Adapter<FolderImageAdapter.ViewHolder>{
    List<String> paths;
    Context context;
    List<Boolean> checkedStates;


    public FolderImageAdapter(List<String> paths, Context context) {
        this.paths = paths;
        this.context = context;
        checkedStates = new ArrayList<>(Collections.nCopies(paths.size(), false)); // Initialize checkedStates
    }

    @NonNull
    @Override
    public FolderImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderImageAdapter.ViewHolder holder, int position) {
        final FolderImageAdapter.ViewHolder viewHolder = holder;
        String imagePath = paths.get(position);
        Glide.with(context)
                .load(imagePath)
                .placeholder(R.color.codeGray)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(viewHolder.imageView);

        // Set the checked state of the checkbox
        viewHolder.checkBox.setChecked(checkedStates.get(position));

        // Set click listener for the checkbox
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckView check = (CheckView) v;
                check.setChecked(!check.isChecked());
                int clickedPosition = viewHolder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    checkedStates.set(clickedPosition, check.isChecked()); // Update the checked state
                }
                if (checkBoxClickListener != null) {
                    checkBoxClickListener.onCheckBoxClick(clickedPosition, check.isChecked());
                }
            }
        });

        // Set click listener for the image view
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open FullImageActivity and pass the clicked image URI or path
                Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra("imagePath", imagePath);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    public interface CheckBoxClickListener {
        void onCheckBoxClick(int position, boolean isChecked);
    }

    private CheckBoxClickListener checkBoxClickListener;

    public void setCheckBoxClickListener(CheckBoxClickListener listener) {
        checkBoxClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        CheckView checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            checkBox = itemView.findViewById(R.id.circle);
        }
    }
}
