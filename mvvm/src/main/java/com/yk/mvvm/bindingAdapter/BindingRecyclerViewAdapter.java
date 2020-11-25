package com.yk.mvvm.bindingAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yk.itg_util.Utils;
import com.yk.mvvm.common.BindingCollectionAdapter;
import com.yk.mvvm.common.ItemBinding;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.OnRebindCallback;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class BindingRecyclerViewAdapter<T> extends RecyclerView.Adapter<ViewHolder> implements BindingCollectionAdapter<T> {

    private static final Object DATA_INVALIDATION = new Object();

    private LayoutInflater inflater;
    private ViewHolderFactory viewHolderFactory;
    private List<T> items;
    private ItemBinding<T> itemBinding;
    private RecyclerView recyclerView;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    private ItemIds<? super T> itemIds;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ViewDataBinding binding = onCreateBinding(inflater, viewType, parent);
        final ViewHolder holder = onCreateViewHolder(binding);
        binding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public boolean onPreBind(ViewDataBinding binding) {
                return recyclerView != null && recyclerView.isComputingLayout();
            }

            @Override
            public void onCanceled(ViewDataBinding binding) {
                if (recyclerView == null || recyclerView.isComputingLayout()) {
                    return;
                }
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, DATA_INVALIDATION);
                }
            }

        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T t = items.get(position);
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        onBindBinding(binding, itemBinding.variableId(), itemBinding.layoutRes(), position, t);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (isForDataBinding(payloads)) {
            ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
            binding.executePendingBindings();
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }


    @Override
    public int getItemViewType(int position) {
        itemBinding.onItemBind(position, items.get(position));
        return itemBinding.layoutRes();
    }

    @Override
    public long getItemId(int position) {
        return itemIds == null ? position : itemIds.getItemId(position, items.get(position));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    public ViewHolder onCreateViewHolder(ViewDataBinding binding) {
        if (viewHolderFactory != null) {
            return viewHolderFactory.createViewHolder(binding);
        } else {
            return new BindingViewHolder(binding);
        }
    }


    @Override
    public void setItemBinding(ItemBinding<T> itemBinding) {

        this.itemBinding = itemBinding;
    }

    @Override
    public ItemBinding<T> getItemBinding() {
        return null;
    }

    @Override
    public void setItems(@Nullable List<T> items) {
        if (this.items == items) {
            return;
        }
        if (this.items instanceof ObservableList) {
            ((ObservableList<T>) this.items).removeOnListChangedCallback(callback);
        }
        if (items instanceof ObservableList) {
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public T getAdapterItem(int position) {
        return items.get(position);
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, int layoutRes, ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int variableId, int layoutRes, int position, T item) {
        if (itemBinding.bind(binding, item)) {
            binding.executePendingBindings();
        }
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

        if (items != null && items instanceof ObservableList) {
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        this.recyclerView = recyclerView;

    }


    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        if (items != null && items instanceof ObservableList) {
            ((ObservableList<T>) items).removeOnListChangedCallback(callback);
        }
        this.recyclerView = null;

    }

    private boolean isForDataBinding(List<Object> payloads) {
        if (payloads == null || payloads.size() == 0) {
            return false;
        }
        for (int i = 0; i < payloads.size(); i++) {
            Object obj = payloads.get(i);
            if (obj != DATA_INVALIDATION) {
                return false;
            }
        }
        return true;
    }


    public void setViewHolderFactory(@Nullable ViewHolderFactory factory) {
        viewHolderFactory = factory;
    }

    public void setItemIds(@Nullable ItemIds<? super T> itemIds) {
        if (this.itemIds != itemIds) {
            this.itemIds = itemIds;
            setHasStableIds(itemIds != null);
        }
    }

    private static class BindingViewHolder extends ViewHolder {
        public BindingViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
        }
    }

    public interface ViewHolderFactory {
        RecyclerView.ViewHolder createViewHolder(ViewDataBinding binding);
    }

    public interface ItemIds<T> {
        long getItemId(int position, T item);
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {

        final WeakReference<BindingRecyclerViewAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallback(BindingRecyclerViewAdapter<T> adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }
        @Override
        public void onChanged(ObservableList<T> sender) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            for (int i = 0; i < itemCount; i++) {
                adapter.notifyItemMoved(fromPosition + i, toPosition + i);
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
            BindingRecyclerViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }
}
