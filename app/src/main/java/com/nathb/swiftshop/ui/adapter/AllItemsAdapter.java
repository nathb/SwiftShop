package com.nathb.swiftshop.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nathb.swiftshop.R;
import com.nathb.swiftshop.model.Item;
import com.nathb.swiftshop.model.ShoppingListItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllItemsAdapter extends ArrayAdapter<Item, AllItemsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemAdded(Item item);
        void onItemRemoved(ShoppingListItem sli);
        void onItemDeleted(Item item);
    }

    private OnClickListener listener;
    private Map<String, ShoppingListItem> itemMap = new HashMap<>();

    public AllItemsAdapter(OnClickListener listener) {
        this.listener = listener;
    }

    public void setShoppingListItems(List<ShoppingListItem> shoppingListItems) {
        itemMap.clear();
        for (ShoppingListItem sli : shoppingListItems) {
            itemMap.put(sli.getName(), sli);
        }
    }

    @Override
    public AllItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_all_items, parent, false);
        return new AllItemsAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(AllItemsAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        ShoppingListItem sli = itemMap.get(item.getName());
        holder.bind(item, sli);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener {

        @BindView(R.id.item_name) TextView textView;
        @BindView(R.id.item_toggle) ImageView itemToggle;

        private Item item;
        private ShoppingListItem sli;
        private OnClickListener listener;

        public ViewHolder(View v, OnClickListener listener) {
            super(v);
            this.listener = listener;
            ButterKnife.bind(this, v);
            v.setOnCreateContextMenuListener(this);
            itemToggle.setOnClickListener(this);
        }

        public void bind(Item item, ShoppingListItem sli) {
            this.item = item;
            this.sli = sli;
            textView.setText(item.getName());

            int drawable = sli != null
                    ? R.drawable.check_box_checked
                    : R.drawable.check_box_unchecked;
            itemToggle.setImageResource(drawable);
        }

        @Override
        public void onClick(View v) {
            if (sli != null) {
                listener.onItemRemoved(sli);
            } else {
                listener.onItemAdded(item);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(R.string.permanently_remove)
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            listener.onItemDeleted(ViewHolder.this.item);
                            return true;
                        }
                    });
        }
    }

}
