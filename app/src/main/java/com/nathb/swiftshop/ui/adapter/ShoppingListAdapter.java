package com.nathb.swiftshop.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
        void onShoppingListDeleted(ShoppingList shoppingList);
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener {

        @BindView(R.id.shopping_list_title) TextView shoppingListTitle;

        private ShoppingList shoppingList;
        private OnClickListener listener;

        public ViewHolder(View v, OnClickListener listener) {
            super(v);
            this.listener = listener;
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(R.string.permanently_remove)
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            listener.onShoppingListDeleted(ViewHolder.this.shoppingList);
                            return true;
                        }
                    });
        }
    }

}
