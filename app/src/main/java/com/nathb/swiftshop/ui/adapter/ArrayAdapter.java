package com.nathb.swiftshop.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class ArrayAdapter<T, VH extends RecyclerView.ViewHolder>
                        extends RecyclerView.Adapter<VH> {

    protected List<T> items = new ArrayList<>();

    public void setItems(List<T> items) {
        this.items = items;
    }

    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
