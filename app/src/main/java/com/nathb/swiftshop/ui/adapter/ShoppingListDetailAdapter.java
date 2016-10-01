package com.nathb.swiftshop.ui.adapter;

import android.graphics.Paint;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingListDetailAdapter extends ArrayAdapter<ShoppingListItem, ShoppingListDetailAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(ShoppingListItem sli);
        void onItemRemoved(ShoppingListItem sli);
        void onItemDeleted(Item item);
    }

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ROW = 1;
    private static final int VIEW_TYPE_FIRST_CHECKED_ROW = 2;

    private OnClickListener listener;

    public ShoppingListDetailAdapter(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = viewType == VIEW_TYPE_ROW
                ? R.layout.row_shopping_list_detail_item
                : R.layout.row_shopping_list_detail_header;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ShoppingListItem sli = getItem(position);
        switch (viewType) {
            case VIEW_TYPE_HEADER: holder.bindHeader(sli); break;
            case VIEW_TYPE_ROW: holder.bindRow(sli); break;
            case VIEW_TYPE_FIRST_CHECKED_ROW: holder.bindFirstCheckRow(sli); break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        ShoppingListItem currentItem = getItem(position);
        if (position == 0) {
            return currentItem.isChecked()
                ? VIEW_TYPE_FIRST_CHECKED_ROW
                : VIEW_TYPE_HEADER;
        }

        ShoppingListItem prevItem = getItem(position - 1);

        if (!currentItem.isChecked()
                && !currentItem.getItem().getCategory().getName().equals(
                        prevItem.getItem().getCategory().getName())) {
            return VIEW_TYPE_HEADER;
        } else if (currentItem.isChecked() && !prevItem.isChecked()) {
            return VIEW_TYPE_FIRST_CHECKED_ROW;
        } else {
            return VIEW_TYPE_ROW;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener {

        @BindView(R.id.item_name) TextView itemName;
        @BindView(R.id.remove_item) ImageView removeItemButton;

        private ShoppingListItem sli;
        private OnClickListener listener;
        private TextView categoryName;

        public ViewHolder(View v, OnClickListener listener) {
            super(v);
            this.listener = listener;
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
            removeItemButton.setOnClickListener(this);

            // Category only on header so will be null on regular row
            // Use findViewById in this case to avoid Butterknife exception
            categoryName = (TextView) v.findViewById(R.id.category_name);
        }

        public void bindFirstCheckRow(ShoppingListItem sli) {
            categoryName.setText(categoryName.getContext().getString(R.string.collected_items));
            bindRow(sli);
        }

        public void bindHeader(ShoppingListItem sli) {
            categoryName.setText(sli.getItem().getCategory().getName());
            bindRow(sli);
        }

        public void bindRow(ShoppingListItem sli) {
            this.sli = sli;
            itemName.setText(sli.getName());
            int paintFlags = itemName.getPaintFlags();
            if (sli.isChecked()) {
                paintFlags |= Paint.STRIKE_THRU_TEXT_FLAG;
            } else {
                paintFlags &= ~Paint.STRIKE_THRU_TEXT_FLAG;
            }
            itemName.setPaintFlags(paintFlags);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == removeItemButton.getId()) {
                listener.onItemRemoved(sli);
            } else {
                listener.onItemClicked(sli);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(R.string.permanently_remove)
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            listener.onItemDeleted(sli.getItem());
                            return true;
                        }
                    });
        }
    }

}
