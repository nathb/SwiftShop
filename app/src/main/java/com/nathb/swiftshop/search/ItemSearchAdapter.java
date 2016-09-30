package com.nathb.swiftshop.search;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nathb.swiftshop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nathb.swiftshop.search.ItemCursor.IS_ADD_ITEM_ROW;
import static com.nathb.swiftshop.search.ItemCursor.ITEM_NAME;

public class ItemSearchAdapter extends CursorAdapter {

    public ItemSearchAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_search, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.setText(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
        viewHolder.toggleAddButton(cursor.getInt(cursor.getColumnIndex(IS_ADD_ITEM_ROW)) == 1);
    }

    public static class ViewHolder {

        @BindView(R.id.item_name) TextView itemName;
        @BindView(R.id.add_button) ImageView addButton;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

        public void setText(String text) {
            itemName.setText(text);
        }

        public void toggleAddButton(boolean show) {
            addButton.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }

    }
}
