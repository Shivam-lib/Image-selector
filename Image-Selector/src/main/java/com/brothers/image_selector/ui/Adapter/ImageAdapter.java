package com.brothers.image_selector.ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brothers.image_selector.ui.widget.CheckView;
import com.brothers.image_selector.FullImage.FullImageActivity;
import com.brothers.image_selector.R;
import com.brothers.image_selector.ui.ImageModel.ImageModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;


import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<ImageModel> imageList;
    private static OnItemClickListener onItemClickListener;
    private final static int IMAGE_LIST = 0;
    private final static int IMAGE_PICKER = 1;


    public ImageAdapter(Context context, ArrayList<ImageModel> imageList) {
        this.context = context;
        this.imageList = imageList;

    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IMAGE_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
            return new ImageListViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camera, parent, false);
            return new ImagePickerViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position < 1 ? IMAGE_PICKER : IMAGE_LIST;
    }

    @Override
    public  void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == IMAGE_LIST) {;
            final ImageListViewHolder viewHolder = (ImageListViewHolder) holder;
            Glide.with(context)
                    .load(imageList.get(position).getImage())
                    .placeholder(R.color.codeGray)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(viewHolder.image);

            if (imageList.get(position).isSelected()) {
                viewHolder.checkBox.setChecked(true);
            } else {
                viewHolder.checkBox.setChecked(false);
            }
// Set click listener for the image in the list
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        showFullImage(adapterPosition);
                    }
                }
            });
        } else {
            ImagePickerViewHolder viewHolder = (ImagePickerViewHolder) holder;
            viewHolder.image.setImageResource(imageList.get(position).getResImg());
            viewHolder.title.setText(imageList.get(position).getTitle());
        }
    }

    private void showFullImage(int adapterPosition) {
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra("image_url", imageList.get(adapterPosition).getImage());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageListViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        CheckView checkBox;

        public ImageListViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            checkBox = itemView.findViewById(R.id.circle);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });

        }
    }

    public class ImagePickerViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public ImagePickerViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

}