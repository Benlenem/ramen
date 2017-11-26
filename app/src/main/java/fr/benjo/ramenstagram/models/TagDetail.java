package fr.benjo.ramenstagram.models;

/**
 * Created by ben on 26/11/2017.
 */

public class TagDetail {
    private Tag tag;

    public TagDetail(Tag tag) {
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
