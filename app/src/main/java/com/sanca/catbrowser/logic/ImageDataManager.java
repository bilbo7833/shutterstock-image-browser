package com.sanca.catbrowser.logic;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sanca.catbrowser.data.ImageAdapter;
import com.sanca.catbrowser.model.ImageData;
import com.sanca.catbrowser.networking.JsonAuthorizedRequest;
import com.sanca.catbrowser.networking.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class in charge of logic. Downloads image data from the Shutterstock API and keeps hold
 * of it. Keeps the RecyclerView Adapter and the list of ImageData as private members, since it's a
 * singleton and thus unaffected by orientation changes and other activity lifecyle events.
 */
public class ImageDataManager {

    private final static String TAG = ImageDataManager.class.getSimpleName();

    private static ImageDataManager sInstance;
    private ImageAdapter mImageAdapter;
    private List<ImageData> mImageList = new ArrayList<ImageData>();

    private final static String URL = "https://api.shutterstock.com/v2/images/search";
    private final static String SHUTTERSTOCK_CLIENT_ID = "5b70b7784a48b140ccf3";
    private final static String SHUTTERSTOCK_CLIENT_SECRET = "b4d9e6121296280f84d5124003844fd290bca957";
    private final static String CATEGORY = "cat";
    private final static int RESULTS_PER_PAGE = 100;

    private int downloadCount;
    private int pageCount;
    private int totalCount;

    public interface ImageClickListener {
        void onImageClicked(ImageData imageData);
    }

    public ImageDataManager() {
        resetCounters();
        downloadImageData();
        mImageAdapter = new ImageAdapter(mImageList);
    }

    public static ImageDataManager getInstance(){
        if(sInstance == null){
            sInstance = new ImageDataManager();
        }
        return sInstance;
    }

    public ImageAdapter getImageAdapter() {
        return mImageAdapter;
    }


    private void resetCounters() {
        downloadCount = 0;
        pageCount = 0;
    }


    private String buildDataDownloadUrl() {
        String url = URL + "?query=" + CATEGORY
                + "&page=" + ++pageCount
                + "&per_page=" + RESULTS_PER_PAGE
                + "&fields=data(id,description,assets/preview/url)";
        return url;
    }

    public void downloadImageData() {
        String dataUrl = buildDataDownloadUrl();
        JsonAuthorizedRequest imageDataRequest = new JsonAuthorizedRequest(dataUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "ImageDataRequest successful. Response: " + response);
                        List<ImageData> newImageData = ImageData.parseFromJson(response);
                        int startOfNewItems = mImageList.size();
                        mImageList.addAll(newImageData);
                        mImageAdapter.notifyItemRangeInserted(startOfNewItems, newImageData.size());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg = "";
                        if (error != null && error.networkResponse != null
                                && error.networkResponse.data != null) {
                            errorMsg = new String(error.networkResponse.data);
                        }
                        Log.e(TAG, "Error in ImageDataRequest: " + errorMsg);
                    }
                });
        imageDataRequest.addAuthorizationHeader(SHUTTERSTOCK_CLIENT_ID, SHUTTERSTOCK_CLIENT_SECRET);
        Log.i(TAG, "Sending image data request: " + dataUrl);
        VolleySingleton.getInstance().getRequestQueue().add(imageDataRequest);
    }
}
