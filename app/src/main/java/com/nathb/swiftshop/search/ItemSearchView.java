package com.nathb.swiftshop.search;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SearchView;

import com.nathb.swiftshop.model.Item;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;

public class ItemSearchView extends SearchView {

    private ItemSearchListener listener;
    private ItemSearchAdapter adapter;
    private Realm realm;

    public interface ItemSearchListener {
        void onAddNewItem(String itemName);
        void onSelectItem(String itemId);
    }

    public ItemSearchView(Context context) {
        super(context);
        init();
    }

    public ItemSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setListener(ItemSearchListener listener) {
        this.listener = listener;
    }

    private void init() {
        realm = Realm.getDefaultInstance();
        adapter = new ItemSearchAdapter(getContext(), new ItemCursor(getContext()), false);
        setSuggestionsAdapter(adapter);
        setOnSuggestionListener(new SuggestionListener());
        setOnQueryTextListener(new QueryListener());
    }

    private void refreshSuggestions(String text) {
        List<Item> items;
        if (text.length() < 2) {
            items = new ArrayList<>();
        } else {
            items = realm.where(Item.class).beginsWith("name", text, Case.INSENSITIVE).findAll();
        }
        adapter.swapCursor(new ItemCursor(getContext(), items, text));
    }

    public void onDestroy() {
        realm.close();
    }

    private class QueryListener implements OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String query) {
            // Do nothing
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            refreshSuggestions(newText);
            return true;
        }
    }

    private class SuggestionListener implements OnSuggestionListener {

        @Override
        public boolean onSuggestionSelect(int position) {
            // Do nothing
            return false;
        }

        @Override
        public boolean onSuggestionClick(int position) {
            ItemCursor cursor = (ItemCursor) adapter.getCursor();
            boolean isAddItemRow = cursor.getInt(cursor.getColumnIndex(ItemCursor.IS_ADD_ITEM_ROW)) == 1;
            if (isAddItemRow) {
                String searchTerm = cursor.getString(cursor.getColumnIndex(ItemCursor.SEARCH_TERM));
                listener.onAddNewItem(searchTerm);
            } else {
                String itemId = cursor.getString(cursor.getColumnIndex(ItemCursor.ITEM_ID));
                listener.onSelectItem(itemId);
            }
            setQuery("", false);
            clearFocus();
            return true;
        }

    }

}
