package fr.benjo.ramenstagram.viewmodels;

import android.databinding.ObservableField;

import fr.benjo.ramenstagram.models.Node;
import fr.benjo.ramenstagram.utils.mvvm.Command1;
import io.reactivex.annotations.Nullable;

/**
 * Created by ben on 26/11/2017.
 */

public class InstagramNodeViewModel {

    public ObservableField<String> thumbnailUrl;
    public ObservableField<String> nodeName;
    public Command1<InstagramNodeViewModel> onNodeClicked;
    public Command1<Integer> onPrimaryColorRetrieved;

    private Node mNode;
    private @Nullable
    Integer primaryColor;

    public InstagramNodeViewModel(Node node, Command1<InstagramNodeViewModel> onNodeClicked) {
        this.mNode = node;

        this.thumbnailUrl = new ObservableField<>(node.getThumbnailSrc());
        this.nodeName = new ObservableField<>(node.getCaption());
        this.onNodeClicked = onNodeClicked;

        this.onPrimaryColorRetrieved = new Command1<Integer>() {
            @Override
            public void execute(Integer param) {
                primaryColor = param;
            }
        };
    }

    public void onClick() {
        if (onNodeClicked.canExecute()) {
            onNodeClicked.execute(this);
        }
    }

    public Node getNode() {
        return mNode;
    }

    public @Nullable
    Integer getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Integer primaryColor) {
        this.primaryColor = primaryColor;
    }
}
