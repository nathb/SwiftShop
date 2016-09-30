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

    private OnClickListener listener;

    public ShoppingListDetailAdapter(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.row_shopping_list_detail_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener {

        @BindView(R.id.item_name) TextView itemName;
        @BindView(R.id.remove_item) ImageView removeItemButton;

        private ShoppingListItem sli;
        private OnClickListener listener;

        public ViewHolder(View v, OnClickListener listener) {
            super(v);
            this.listener = listener;
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
            removeItemButton.setOnClickListener(this);
        }

        public void bind(ShoppingListItem sli) {
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
