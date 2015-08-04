package com.sanca.catbrowser.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.sanca.catbrowser.R;
import com.sanca.catbrowser.networking.VolleySingleton;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ImageFragment extends DialogFragment {

    @InjectView(R.id.img_view) NetworkImageView mImage;


    public static ImageFragment newInstance(String url) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.inject(this, rootView);
        if (getArguments() != null) {
            mImage.setImageUrl(getArguments().getString("url"),
                    VolleySingleton.getInstance().getImageLoader());
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
    

}
