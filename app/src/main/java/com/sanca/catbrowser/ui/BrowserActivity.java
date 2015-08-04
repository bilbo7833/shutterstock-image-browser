package com.sanca.catbrowser.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sanca.catbrowser.data.EndlessRecyclerOnScrollListener;
import com.sanca.catbrowser.data.ImageAdapter;
import com.sanca.catbrowser.logic.ImageDataManager;
import com.sanca.catbrowser.R;
import com.sanca.catbrowser.model.ImageData;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BrowserActivity extends AppCompatActivity implements ImageAdapter.ImageClickListener {

    public static final String TAG = BrowserActivity.class.getSimpleName();

    @InjectView(R.id.recycler_images) RecyclerView mImageRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.inject(this);

        ImageDataManager imageManager = ImageDataManager.getInstance();
        imageManager.getImageAdapter().setImageClickListener(this);
        Log.d(TAG, "Initializing the RecyclerView");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mImageRecyclerView.setLayoutManager(mLayoutManager);
        mImageRecyclerView.setAdapter(imageManager.getImageAdapter());

        mImageRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                ImageDataManager.getInstance().downloadImageData();
            }
        });

    }



    @Override
    public void onImageClicked(ImageData imageData) {
        ImageFragment imageFragment = ImageFragment.newInstance(imageData.getPreviewUrl());
        FragmentManager fm = getSupportFragmentManager();
        imageFragment.show(fm, "ImageFragment");

    }
}
