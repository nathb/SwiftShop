package com.nathb.swiftshop.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nathb.swiftshop.R;
import com.nathb.swiftshop.model.Item;
import com.nathb.swiftshop.model.ShoppingList;
import com.nathb.swiftshop.model.ShoppingListItem;
import com.nathb.swiftshop.task.AddItemToShoppingListTask;
import com.nathb.swiftshop.task.DeleteItemTask;
import com.nathb.swiftshop.task.RemoveItemFromShoppingListTask;
import com.nathb.swiftshop.ui.adapter.AllItemsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class AllItemsActivity extends BaseActivity {

    private static final String EXTRA_SHOPPING_LIST_ID = "shoppingListId";

    public static Intent getNavigateIntent(Context context, String id) {
        Intent intent = new Intent(context, AllItemsActivity.class);
        intent.putExtra(EXTRA_SHOPPING_LIST_ID, id);
        return intent;
    }

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.empty_view) View emptyView;

    private ShoppingList shoppingList;
    private RealmResults<Item> items;
    private AllItemsAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_all_items;
    }

    @Override
    protected int getTitleId() {
        return R.string.title_all_items;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        adapter = createAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        query();
    }

    private AllItemsAdapter createAdapter() {
        return new AllItemsAdapter(new AllItemsAdapter.OnClickListener() {

            @Override
            public void onItemAdded(Item item) {
                new AddItemToShoppingListTask(shoppingList, item.getId()).execute();
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
        shoppingList = realm.where(ShoppingList.class).equalTo("id", id).findFirst();
        shoppingList.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                adapter.setShoppingListItems(shoppingList.getItems());
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setShoppingListItems(shoppingList.getItems());

        items = realm.where(Item.class).findAllAsync().sort("name");
        items.addChangeListener(new RealmChangeListener<RealmResults<Item>>() {
            @Override
            public void onChange(RealmResults<Item> element) {
                emptyView.setVisibility(items.isEmpty()
                        ? View.VISIBLE : View.GONE);
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setItems(items);
    }

    @Override
    protected void onDestroy() {
        items.removeChangeListeners();
        super.onDestroy();
    }

}
