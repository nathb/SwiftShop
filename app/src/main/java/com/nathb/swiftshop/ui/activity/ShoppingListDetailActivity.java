package com.nathb.swiftshop.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.nathb.swiftshop.R;
import com.nathb.swiftshop.model.Category;
import com.nathb.swiftshop.model.Item;
import com.nathb.swiftshop.model.ShoppingList;
import com.nathb.swiftshop.model.ShoppingListItem;
import com.nathb.swiftshop.search.ItemSearchView;
import com.nathb.swiftshop.task.AddItemToShoppingListTask;
import com.nathb.swiftshop.task.CreateItemAndAddToShoppingListTask;
import com.nathb.swiftshop.task.DeleteItemTask;
import com.nathb.swiftshop.task.RemoveItemFromShoppingListTask;
import com.nathb.swiftshop.task.ToggleCheckStatusTask;
import com.nathb.swiftshop.ui.adapter.ShoppingListDetailAdapter;
import com.nathb.swiftshop.ui.dialog.SelectCategoryDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class ShoppingListDetailActivity extends BaseActivity {

    private static final String EXTRA_SHOPPING_LIST_ID = "shoppingListId";

    public static Intent getNavigateIntent(Context context, String id) {
        Intent intent = new Intent(context, ShoppingListDetailActivity.class);
        intent.putExtra(EXTRA_SHOPPING_LIST_ID, id);
        return intent;
    }

    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.search_view) ItemSearchView searchView;
    @BindView(R.id.view_all_items) Button viewAllItemsButton;
    @BindView(R.id.empty_view) View emptyView;

    private ShoppingList shoppingList;
    private ShoppingListDetailAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shopping_list_detail;
    }

    @Override
    protected int getTitleId() {
        return R.string.title_shopping_list_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        adapter = createAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        searchView.setListener(new ItemSearchListener());
        viewAllItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AllItemsActivity.getNavigateIntent(
                        ShoppingListDetailActivity.this, shoppingList.getId()));
            }
        });
        query();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.requestFocus();
    }

    private ShoppingListDetailAdapter createAdapter() {
        return new ShoppingListDetailAdapter(new ShoppingListDetailAdapter.OnClickListener() {

            @Override
            public void onItemClicked(ShoppingListItem sli) {
                new ToggleCheckStatusTask(sli).execute();
            }

            @Override
            public void onItemRemoved(ShoppingListItem sli) {
                new RemoveItemFromShoppingListTask(sli).execute();
            }

            @Override
            public void onItemDeleted(Item item) {
                new DeleteItemTask(item).execute();
            }
        });
    }

    private void query() {
        String id = getIntent().getStringExtra(EXTRA_SHOPPING_LIST_ID);
        shoppingList = realm.where(ShoppingList.class).equalTo("id", id).findFirstAsync();
        shoppingList.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                // Realm does not support sorting on child fields, therefore using a custom comparator.
                // Realm also does not allow a Collections.sort() outside of a transaction so create new list.
                List<ShoppingListItem> items = new ArrayList<>(shoppingList.getItems());
                Collections.sort(items);
                adapter.setItems(items);
                adapter.notifyDataSetChanged();
                emptyView.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        shoppingList.removeChangeListeners();
        searchView.onDestroy();
        super.onDestroy();
    }

    private class ItemSearchListener implements ItemSearchView.ItemSearchListener {

        @Override
        public void onAddNewItem(final String itemName) {
            SelectCategoryDialog dialog = new SelectCategoryDialog();
            dialog.setItemName(itemName);
            dialog.setCallback(new SelectCategoryDialog.Callback() {
                @Override
                public void onCategorySelected(Category category) {
                    new CreateItemAndAddToShoppingListTask(shoppingList, category, itemName).execute();
                    showUndoSnackBar(itemName);
                }
            });
            dialog.show(getFragmentManager(), SelectCategoryDialog.TAG);
        }

        @Override
        public void onSelectItem(String itemId) {
            new AddItemToShoppingListTask(shoppingList, itemId).execute();
        }

    }

    private void showUndoSnackBar(final String itemName) {
        String text = getString(R.string.created_new_item, itemName);
        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.undo_uppercase, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DeleteItemTask(itemName).execute();
                    }
                })
                .setDuration(7000)
                .show();
    }
}
