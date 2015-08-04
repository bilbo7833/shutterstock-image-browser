package com.sanca.catbrowser.model;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanca on 04.08.15.
 */
public class ImageData {

    private final static String TAG = ImageData.class.getSimpleName();

    private String id;
    private String description;
    private String previewUrl;

    public ImageData() {};

    public ImageData(String id, String description, String previewUrl) {
        this.id = id;
        this.description = description;
        this.previewUrl = previewUrl;
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", previewUrl='" + previewUrl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getMosaicUrl() {
        return "http://image.shutterstock.com/mosaic_250/0/0/" + this.id + ".jpg";
    }

    /**
     * Parses JSON Data and extracts ids and urls for images.
     * Returns a list of ImageData objects.
     * @param obj
     * @return
     */
    public static List<ImageData> parseFromJson(JSONObject obj) {
        List<ImageData> images = new ArrayList<ImageData>();
        JSONArray imgArray = obj.optJSONArray("data");
        if (imgArray != null) {
            for (int i=0; i<imgArray.length(); i++) {
                JSONObject imgObj = imgArray.optJSONObject(i);
                if (imgObj != null && imgObj.has("id")) {
                    ImageData image = new ImageData();
                    image.setId((String) imgObj.opt("id"));
                    image.setDescription((String) imgObj.opt("description"));
                    JSONObject assets = (JSONObject) imgObj.opt("assets");
                    if (assets != null) {
                        JSONObject preview = (JSONObject) assets.opt("preview");
                        if (preview != null) {
                            image.setPreviewUrl((String) preview.opt("url"));
                        }
                    }
                    images.add(image);
                }
            }
        }
        return images;
    }

}
