package com.nathb.swiftshop.search;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
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
import static com.nathb.swiftshop.search.ItemCursor.SEARCH_TERM;

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
        String searchTerm = cursor.getString(cursor.getColumnIndex(SEARCH_TERM));
        String itemName = cursor.getString(cursor.getColumnIndex(ITEM_NAME));
        boolean isAddItemRow = cursor.getInt(cursor.getColumnIndex(IS_ADD_ITEM_ROW)) == 1;

        if (!isAddItemRow) {
            itemName = itemName.replace(searchTerm, "<b>" + searchTerm + "</b>");
            itemName = itemName.replace(searchTerm.toLowerCase(), "<b>" + searchTerm.toLowerCase() + "</b>");
        }
        viewHolder.setHtmlText(itemName);
        viewHolder.toggleAddButton(cursor.getInt(cursor.getColumnIndex(IS_ADD_ITEM_ROW)) == 1);
    }

    public static class ViewHolder {

        @BindView(R.id.item_name) TextView itemName;
        @BindView(R.id.add_button) ImageView addButton;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

        public void setHtmlText(String text) {
            Spanned result;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                result = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
            } else {
                result = Html.fromHtml(text);
            }
            itemName.setText(result);
        }

        public void toggleAddButton(boolean show) {
            addButton.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }

    }
}
