package fr.benjo.ramenstagram.utils;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by benjamin on 03/05/2017.
 */
public class SimpleBindingAdapter<T> extends RecyclerView.Adapter<BindableViewHolder> {

    private ItemBinding mItemBinding;
    private List<T> mItems;
    private int mLayoutId;


    public SimpleBindingAdapter(ItemBinding itemBinding, List<T> items, int layoutId) {
        mItemBinding = itemBinding;
        mItems = items;
        mLayoutId = layoutId;

        if (mItems instanceof ObservableList) {

            ((ObservableList<T>) mItems).addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
                @Override
                public void onChanged(ObservableList sender) {
                    notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                    notifyItemRangeChanged(positionStart, itemCount);
                }

                @Override
                public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                    notifyItemRangeInserted(positionStart, itemCount);
                }

                @Override
                public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                    notifyItemRangeChanged(fromPosition, itemCount);
                }

                @Override
                public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                    notifyItemRangeRemoved(positionStart, itemCount);
                }
            });
        }
    }

    @Override
    public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, mLayoutId, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new BindableViewHolder(binding, mItemBinding);
    }

    @Override
    public void onBindViewHolder(BindableViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
