package com.yk.mvvm;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ListChangeRegistry;
import androidx.databinding.ObservableList;

public class MergeObservableList<T> extends AbstractList<T> implements ObservableList<T> {
    private final ArrayList<List<? extends T>> lists = new ArrayList<>();
    private final ListChangeCallback callback = new ListChangeCallback();
    private final ListChangeRegistry listeners = new ListChangeRegistry();

    @Override
    public void addOnListChangedCallback(OnListChangedCallback<? extends ObservableList<T>> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeOnListChangedCallback(OnListChangedCallback<? extends ObservableList<T>> listener) {
        listeners.remove(listener);
    }

    public MergeObservableList<T> insertItem(T object) {
        lists.add(Collections.singletonList(object));
        modCount += 1;
        listeners.notifyInserted(this, size() - 1, 1);
        return this;
    }


    @SuppressWarnings("unchecked")
    public MergeObservableList<T> insertList(@NonNull ObservableList<? extends T> list) {
        list.addOnListChangedCallback(callback);
        int oldSize = size();
        lists.add(list);
        modCount += 1;
        if (!list.isEmpty()) {
            listeners.notifyInserted(this, oldSize, list.size());
        }
        return this;
    }


    public boolean removeItem(T object) {
        int size = 0;
        for (int i = 0, listsSize = lists.size(); i < listsSize; i++) {
            List<? extends T> list = lists.get(i);
            if (list.size() == 1) {
                Object item = list.get(0);
                if ((object == null) ? (item == null) : object.equals(item)) {
                    lists.remove(i);
                    modCount += 1;
                    listeners.notifyRemoved(this, size, 1);
                    return true;
                }
            }
            size += list.size();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean removeList(ObservableList<? extends T> listToRemove) {
        int size = 0;
        for (int i = 0, listsSize = lists.size(); i < listsSize; i++) {
            List<? extends T> list = lists.get(i);
            if (list == listToRemove) {
                listToRemove.removeOnListChangedCallback(callback);
                lists.remove(i);
                modCount += 1;
                listeners.notifyRemoved(this, size, list.size());
                return true;
            }
            size += list.size();
        }
        return false;
    }


    public int mergeToBackingIndex(ObservableList<? extends T> backingList, int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        int size = 0;
        for (int i = 0, listsSize = lists.size(); i < listsSize; i++) {
            List<? extends T> list = lists.get(i);
            if (backingList == list) {
                if (index < list.size()) {
                    return size + index;
                } else {
                    throw new IndexOutOfBoundsException();
                }
            }
            size += list.size();
        }
        throw new IllegalArgumentException();
    }


    public int backingIndexToMerge(ObservableList<? extends T> backingList, int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        int size = 0;
        for (int i = 0, listsSize = lists.size(); i < listsSize; i++) {
            List<? extends T> list = lists.get(i);
            if (backingList == list) {
                if (index - size < list.size()) {
                    return index - size;
                } else {
                    throw new IndexOutOfBoundsException();
                }
            }
            size += list.size();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public T get(int location) {
        if (location < 0) {
            throw new IndexOutOfBoundsException();
        }
        int size = 0;
        for (int i = 0, listsSize = lists.size(); i < listsSize; i++) {
            List<? extends T> list = lists.get(i);
            if (location - size < list.size()) {
                return list.get(location - size);
            }
            size += list.size();
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int size() {
        int size = 0;
        for (int i = 0, listsSize = lists.size(); i < listsSize; i++) {
            List<? extends T> list = lists.get(i);
            size += list.size();
        }
        return size;
    }

    class ListChangeCallback extends OnListChangedCallback {

        @Override
        public void onChanged(ObservableList sender) {
            modCount += 1;
            listeners.notifyChanged(MergeObservableList.this);
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            int size = 0;
            for (int i = 0, listsSize = lists.size(); i < listsSize; i++) {
                List list = lists.get(i);
                if (list == sender) {
                    listeners.notifyChanged(MergeObservableList.this, size + positionStart, itemCount);
                    return;
                }
                size += list.size();
            }
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            modCount += 1;
            int size = 0;
            for (int i = 0, listsSize = lists.size(); i < listsSize; i++) {
                List list = lists.get(i);
                if (list == sender) {
                    listeners.notifyInserted(MergeObservableList.this, size + positionStart, itemCount);
                    return;
                }
                size += list.size();
            }
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            int size = 0;
            for (int i = 0, listsSize = lists.size(); i < listsSize; i++) {
                List list = lists.get(i);
                if (list == sender) {
                    listeners.notifyMoved(MergeObservableList.this, size + fromPosition, size + toPosition, itemCount);
                    return;
                }
                size += list.size();
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            modCount += 1;
            int size = 0;
            for (int i = 0, listsSize = lists.size(); i < listsSize; i++) {
                List list = lists.get(i);
                if (list == sender) {
                    listeners.notifyRemoved(MergeObservableList.this, size + positionStart, itemCount);
                    return;
                }
                size += list.size();
            }
        }
    }
}
