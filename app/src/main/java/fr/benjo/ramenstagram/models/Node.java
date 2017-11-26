package fr.benjo.ramenstagram.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ben on 26/11/2017.
 */

public class Node {
    @SerializedName("thumbnail_src")
    private String thumbnailSrc;

    @SerializedName("display_src")
    private String displaySrc;

    private String caption;

    public Node(String thumbnailSrc, String displaySrc, String caption) {
        this.thumbnailSrc = thumbnailSrc;
        this.displaySrc = displaySrc;
        this.caption = caption;
    }

    public String getThumbnailSrc() {
        return thumbnailSrc;
    }

    public void setThumbnailSrc(String thumbnailSrc) {
        this.thumbnailSrc = thumbnailSrc;
    }

    public String getDisplaySrc() {
        return displaySrc;
    }

    public void setDisplaySrc(String displaySrc) {
        this.displaySrc = displaySrc;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
