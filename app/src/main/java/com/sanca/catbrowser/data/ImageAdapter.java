package com.sanca.catbrowser.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.sanca.catbrowser.R;
import com.sanca.catbrowser.model.ImageData;
import com.sanca.catbrowser.networking.VolleySingleton;

import java.util.List;

/**
 * Created by sanca on 04.08.15.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final static String TAG = ImageAdapter.class.getSimpleName();

    private List<ImageData> mData;
    private ImageClickListener mListener = null;

    public interface ImageClickListener {
        void onImageClicked(ImageData imageData);
    }

    public ImageAdapter(List<ImageData> data) {
        this.mData = data;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView mImage;
        public TextView mDescription;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImage = (NetworkImageView) itemView.findViewById(R.id.img_view);
            mDescription = (TextView) itemView.findViewById(R.id.txt_description);
        }
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_image, viewGroup, false);
        return new ImageViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder imageViewHolder, int i) {
        final ImageData image = this.mData.get(i);
        imageViewHolder.mImage.setImageUrl(image.getPreviewUrl(),
                VolleySingleton.getInstance().getImageLoader());
        imageViewHolder.mImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Refresh on long click
                v.requestLayout();
                return true;
            }
        });
        imageViewHolder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onImageClicked(image);
                }
            }
        });
        imageViewHolder.mImage.setDefaultImageResId(R.drawable.ic_shutterstock_default);
        imageViewHolder.mImage.setErrorImageResId(R.drawable.ic_shutterstock_default);
        imageViewHolder.mDescription.setText(image.getDescription());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setImageClickListener(ImageClickListener listener) {
        this.mListener = listener;
    }
}
