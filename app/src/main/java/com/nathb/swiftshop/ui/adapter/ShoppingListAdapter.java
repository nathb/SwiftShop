package com.nathb.swiftshop.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nathb.swiftshop.R;
import com.nathb.swiftshop.model.ShoppingList;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList, ShoppingListAdapter.ViewHolder> {

    public interface OnClickListener {
        void onShoppingListClicked(ShoppingList shoppingList);
    }

    private OnClickListener listener;

    public ShoppingListAdapter(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.row_shopping_list, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.shopping_list_title) TextView shoppingListTitle;

        private ShoppingList shoppingList;
        private OnClickListener listener;

        public ViewHolder(View v, OnClickListener listener) {
            super(v);
            this.listener = listener;
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        public void bind(ShoppingList shoppingList) {
            this.shoppingList = shoppingList;
            shoppingListTitle.setText(convertDate(shoppingList.getTimestamp()));
        }

        public CharSequence convertDate(Date date) {
            return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS);
        }

        @Override
        public void onClick(View v) {
            listener.onShoppingListClicked(shoppingList);
        }
    }

}
