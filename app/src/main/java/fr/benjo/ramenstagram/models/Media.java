package fr.benjo.ramenstagram.models;

import java.util.List;

/**
 * Created by ben on 26/11/2017.
 */

public class Media {
    public List<Node> nodes;

    public Media(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
