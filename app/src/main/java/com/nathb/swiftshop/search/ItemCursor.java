package com.nathb.swiftshop.search;

import android.content.Context;
import android.database.MatrixCursor;

import com.nathb.swiftshop.R;
import com.nathb.swiftshop.model.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Converts a list of Items to a MatrixCursor so that it
 * can be used to display suggestions via a SearchView
 */
public class ItemCursor extends MatrixCursor {

    // Cursor adapter requires a filed called _id
    public static String ID = "_id";
    public static String ITEM_ID = "itemId";
    public static String ITEM_NAME = "name";
    public static String SEARCH_TERM = "searchTerm";
    public static String IS_ADD_ITEM_ROW = "isAddItemRow";

    private static final String[] COLUMN_NAMES = new String[]{
            ID, ITEM_ID, ITEM_NAME, SEARCH_TERM, IS_ADD_ITEM_ROW };

    public ItemCursor(Context context) {
        this(context, new ArrayList<Item>(), null);
    }

    public ItemCursor(Context context, List<Item> items, String searchTerm) {
        super(COLUMN_NAMES);
        Set<String> nameSet = new HashSet<>();
        int count = 1;
        for (Item item : items) {
            nameSet.add(item.getName());
            addRow(new Object[]{ count++, item.getId(), item.getName(), searchTerm, 0 });
        }

        if (searchTerm != null && searchTerm.length() > 1 && !nameSet.contains(searchTerm)) {
            String notFoundString = context.getResources().getString(
                    R.string.search_item_not_found, searchTerm);
            addRow(new Object[] { count++, null, notFoundString, searchTerm, 1 });
        }
    }

}
