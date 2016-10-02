package com.nathb.swiftshop.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nathb.swiftshop.R;
import com.nathb.swiftshop.model.ShoppingList;
import com.nathb.swiftshop.task.CreateShoppingListTask;
import com.nathb.swiftshop.task.DeleteShoppingListTask;
import com.nathb.swiftshop.ui.adapter.ShoppingListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class ShoppingListActivity extends BaseActivity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.add_shopping_list) FloatingActionButton addButton;
    @BindView(R.id.empty_view) View emptyView;

    private RealmResults<ShoppingList> results;
    private ShoppingListAdapter adapter;

    private Animation scaleInAnimation;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_shopping_list;
    }

    @Override
    protected int getTitleId() {
        return R.string.title_shopping_list;
    }

    @Override
    protected boolean showBackButton() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        scaleInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_in);

        adapter = new ShoppingListAdapter(new ShoppingListAdapter.OnClickListener() {

            @Override
            public void onShoppingListClicked(ShoppingList shoppingList) {
                navigateToShoppingListDetail(shoppingList.getId());
            }

            @Override
            public void onShoppingListDeleted(ShoppingList shoppingList) {
                new DeleteShoppingListTask(shoppingList).execute();
            }
        });

        results = realm.where(ShoppingList.class).findAllSortedAsync("timestamp", Sort.DESCENDING);
        results.addChangeListener(new RealmChangeListener<RealmResults<ShoppingList>>() {
            @Override
            public void onChange(RealmResults<ShoppingList> element) {
                emptyView.setVisibility(element.isEmpty() ? View.VISIBLE : View.GONE);
                adapter.notifyDataSetChanged();
            }
        });

        adapter.setItems(results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addButton.startAnimation(scaleInAnimation);
    }

    @Override
    protected void onDestroy() {
        results.removeChangeListeners();
        super.onDestroy();
    }

    @OnClick(R.id.add_shopping_list)
    public void add() {
        new CreateShoppingListTask(new ShoppingList(), new CreateShoppingListTask.Callback() {

            @Override
            public void onCreated(String id) {
                navigateToShoppingListDetail(id);
            }

        }).execute();
    }

    private void navigateToShoppingListDetail(String id) {
        startActivity(ShoppingListDetailActivity.getNavigateIntent(this, id));
    }
}
