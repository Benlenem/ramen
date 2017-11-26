package fr.benjo.ramenstagram.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.util.SparseArray;

import java.util.List;

import fr.benjo.ramenstagram.BR;
import fr.benjo.ramenstagram.api.InstagramApi;
import fr.benjo.ramenstagram.utils.ItemBinding;
import fr.benjo.ramenstagram.utils.mvvm.Command1;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ben on 26/11/2017.
 */

public class InstagramNodesViewModel {
    public static final String LAYOUT_MODE_GRID = "grid";
    public static final String LAYOUT_MODE_LIST = "list";
    public ItemBinding ramenBinding;
    public ObservableList<InstagramNodeViewModel> ramenList;
    public ObservableBoolean isLoading = new ObservableBoolean();
    public ObservableBoolean canDownload;
    public ObservableField<String> errorMessage;
    public ObservableField<String> layoutMode;

    private Disposable mDisposable;
    private InstagramApi mInstagramApi;
    private String mTagName;
    private Command1<InstagramNodeViewModel> mOnNodeClicked;

    public InstagramNodesViewModel(InstagramApi instagramApi, String tagName, Command1<InstagramNodeViewModel> onNodeClicked) {
        mInstagramApi = instagramApi;
        mTagName = tagName;
        mOnNodeClicked = onNodeClicked;

        canDownload = new ObservableBoolean(false);
        errorMessage = new ObservableField<>();
        ramenList = new ObservableArrayList<>();
        layoutMode = new ObservableField<>(LAYOUT_MODE_LIST);

        this.ramenBinding = new ItemBinding() {
            @Override
            public Integer getBindingItemVariable() {
                return BR.vm;
            }

            @Override
            public SparseArray<Object> getAdditionalVariables() {
                return null;
            }
        };

        loadNodes();
    }

    public void onRefresh() {
        loadNodes();
    }

    public void onToggleMode() {
        layoutMode.set(LAYOUT_MODE_LIST.equals(layoutMode.get()) ? LAYOUT_MODE_GRID : LAYOUT_MODE_LIST);
    }

    private void loadNodes() {

        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

        isLoading.set(true);
        mDisposable = mInstagramApi
                .getTagDetail(mTagName)
                .flatMap(tagDetail -> Observable.fromIterable(tagDetail.getTag().getMedia().getNodes()))
                .map(n -> new InstagramNodeViewModel(n, mOnNodeClicked))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<InstagramNodeViewModel>>() {
                    @Override
                    public void onSuccess(List<InstagramNodeViewModel> nodeViewModels) {
                        ramenList.clear();
                        ramenList.addAll(nodeViewModels);
                        isLoading.set(false);
                        canDownload.set(!ramenList.isEmpty());
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading.set(false);
                        errorMessage.set(e.getMessage());
                    }
                });
    }
}
