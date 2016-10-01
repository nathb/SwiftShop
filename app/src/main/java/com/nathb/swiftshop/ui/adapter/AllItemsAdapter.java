package com.nathb.swiftshop.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
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

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ROW = 1;

    private OnClickListener listener;

    // A map of itemName to ShoppingListItem use to quickly
    // determine if the item is in the current shopping list
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
        int layoutId = viewType == VIEW_TYPE_HEADER
                ? R.layout.row_all_items_header
                : R.layout.row_all_items;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new AllItemsAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(AllItemsAdapter.ViewHolder holder, int position) {
        Item item = getItem(position);
        ShoppingListItem sli = itemMap.get(item.getName());
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            holder.bindHeader(item, sli);
        } else {
            holder.bindRow(item, sli);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }

        String prevName = getItem(position - 1).getCategory().getName();
        String currentName = getItem(position).getCategory().getName();

        if (!currentName.equals(prevName)) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ROW;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener {

        @BindView(R.id.item_name) TextView textView;
        @BindView(R.id.item_toggle) ImageView itemToggle;

        private Item item;
        private ShoppingListItem sli;
        private OnClickListener listener;
        private Drawable checkBoxChecked;
        private Drawable checkBoxUnchecked;
        private TextView categoryName;

        public ViewHolder(View v, OnClickListener listener) {
            super(v);
            this.listener = listener;
            ButterKnife.bind(this, v);
            v.setOnCreateContextMenuListener(this);
            itemToggle.setOnClickListener(this);
            checkBoxChecked = ContextCompat.getDrawable(v.getContext(), R.drawable.check_box_checked);
            checkBoxUnchecked = ContextCompat.getDrawable(v.getContext(), R.drawable.check_box_unchecked);

            // Category only on header so will be null on regular row
            // Use findViewById in this case to avoid Butterknife exception
            categoryName = (TextView) v.findViewById(R.id.category_name);
        }

        public void bindHeader(Item item, ShoppingListItem sli) {
            categoryName.setText(item.getCategory().getName());
            bindRow(item, sli);
        }

        public void bindRow(Item item, ShoppingListItem sli) {
            this.item = item;
            this.sli = sli;
            textView.setText(item.getName());
            itemToggle.setImageDrawable(sli != null ? checkBoxChecked : checkBoxUnchecked);
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
