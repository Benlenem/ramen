package fr.benjo.ramenstagram.models;

/**
 * Created by ben on 26/11/2017.
 */

public class Tag {
    private Media media;

    public Tag(Media media) {
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
