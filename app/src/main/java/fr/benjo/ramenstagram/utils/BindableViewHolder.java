package fr.benjo.ramenstagram.utils;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

/**
 * Created by ben on 12/05/2017.
 */ // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class BindableViewHolder extends RecyclerView.ViewHolder {
    private final ViewDataBinding mBinding;
    private ItemBinding mItemBinding;

    public BindableViewHolder(ViewDataBinding binding, ItemBinding itemBinding) {
        super(binding.getRoot());
        this.mBinding = binding;
        this.mItemBinding = itemBinding;
    }

    public void bind(Object obj) {
        mBinding.setVariable(mItemBinding.getBindingItemVariable(), obj);
        SparseArray<Object> variables = mItemBinding.getAdditionalVariables();

        if (variables != null) {
            for (int i = 0; i < variables.size(); i++) {
                int key = variables.keyAt(i);
                Object value = variables.get(key);
                mBinding.setVariable(key, value);
            }
        }

        mBinding.executePendingBindings();
    }
}
