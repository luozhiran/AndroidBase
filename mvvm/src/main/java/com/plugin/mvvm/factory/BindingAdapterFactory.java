package com.plugin.mvvm.factory;

import android.content.Context;
import android.widget.AdapterView;


import com.google.android.material.tabs.TabLayout;
import com.plugin.mvvm.bindingAdapter.BindingListViewAdapter;
import com.plugin.mvvm.bindingAdapter.BindingRecyclerViewAdapter;
import com.plugin.mvvm.bindingAdapter.ItgFragmentPagerAdapter;
import com.plugin.mvvm.bindingAdapter.ItgBigFragmentStatePagerAdapter;
import com.plugin.mvvm.bindingAdapter.LayoutManagers;
import com.plugin.mvvm.common.ItemBinding;
import com.plugin.mvvm.common.OnItemBind;
import com.plugin.mvvm.viewpagerModel.PageStateFragmentModel;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BindingAdapterFactory {


    @BindingAdapter(value = {"fragmentFactory", "fragmentTitle", "fragmentSize"}, requireAll = false)
    public static void fragmentStatePagerAdapter(ViewPager viewPager, PageStateFragmentModel.FragmentFactory factory, List<String> title, int fragmentSize) {
        if (factory != null && fragmentSize > 0) {
            PagerAdapter adapter = viewPager.getAdapter();
            Context context = viewPager.getContext();
            if (adapter == null) {
                if (context instanceof FragmentActivity) {
                    ItgBigFragmentStatePagerAdapter pagerAdapter = ItgBigFragmentStatePagerAdapter.of(((FragmentActivity) context).getSupportFragmentManager(), factory, fragmentSize, title);
                    viewPager.setOffscreenPageLimit(Math.min(5, fragmentSize));
                    viewPager.setAdapter(pagerAdapter);

                } else {
                    throw new IllegalStateException("must is activity");
                }
            }
        }
    }


    @BindingAdapter(value = {"tabItems", "tabCount"}, requireAll = false)
    public static void newTabItem(TabLayout tableLayout, List<String> list, int size) {
        if (tableLayout.getTabCount() == 0) {
            for (String str : list) {
                TabLayout.Tab tab = tableLayout.newTab();
                tab.setText(str);
                tableLayout.addTab(tab);
            }
        }

    }


    @BindingAdapter(value = {"fragments", "fragmentTitle", "fragmentSize"}, requireAll = false)
    public static void fragmentPagerAdapter(ViewPager viewPager, List<Fragment> list, List<String> title, int fragmentSize) {
        if (!list.isEmpty()) {
            PagerAdapter adapter = viewPager.getAdapter();
            Context context = viewPager.getContext();
            if (adapter == null) {
                if (context instanceof FragmentActivity) {
                    ItgFragmentPagerAdapter pagerAdapter = ItgFragmentPagerAdapter.of(((FragmentActivity) context).getSupportFragmentManager(), list, title);
                    viewPager.setOffscreenPageLimit(fragmentSize);
                    viewPager.setAdapter(pagerAdapter);
                } else {
                    throw new IllegalStateException("must is activity");
                }
            }
        }
    }


    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }


    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder"}, requireAll = false)
    public static <T> void recyclerViewAdapter(RecyclerView recyclerView,
                                               ItemBinding<T> itemBinding,
                                               List<T> items,
                                               BindingRecyclerViewAdapter<T> adapter,
                                               BindingRecyclerViewAdapter.ItemIds<? super T> itemIds,
                                               BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory) {
        if (itemBinding == null) {
            throw new IllegalArgumentException("itemBinding must not be null");
        }
        BindingRecyclerViewAdapter oldAdapter = (BindingRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            if (oldAdapter == null) {
                adapter = new BindingRecyclerViewAdapter<>();
            } else {
                adapter = oldAdapter;
            }
        }
        adapter.setItemBinding(itemBinding);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
        adapter.setViewHolderFactory(viewHolderFactory);

        if (oldAdapter != adapter) {
            recyclerView.setAdapter(adapter);
        }
    }


    @BindingAdapter(value = {"itemBinding", "itemTypeCount", "items", "adapter", "itemIds", "itemIsEnabled"}, requireAll = false)
    public static <T> void setAdapter(AdapterView adapterView, ItemBinding<T> itemBinding, Integer itemTypeCount, List items, BindingListViewAdapter<T> adapter, BindingListViewAdapter.ItemIds<? super T> itemIds, BindingListViewAdapter.ItemIsEnabled<? super T> itemIsEnabled) {
        if (itemBinding == null) {
            throw new IllegalArgumentException("onItemBind must not be null");
        }
        boolean setAdapter = false;
        if (adapter == null) {
            int count = itemTypeCount != null ? itemTypeCount : 1;
            setAdapter = true;
            adapter = new BindingListViewAdapter<>(count);
        }
        adapter.setItemBinding(itemBinding);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
        adapter.setItemIsEnabled(itemIsEnabled);

        if (setAdapter) {
            adapterView.setAdapter(adapter);
        }


    }

    @BindingConversion
    public static <T> ItemBinding<T> toItemBinding(OnItemBind<T> onItemBind) {
        return ItemBinding.of(onItemBind);
    }

}
