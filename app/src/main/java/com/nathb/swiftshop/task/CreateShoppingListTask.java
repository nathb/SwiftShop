package com.nathb.swiftshop.task;

import android.os.AsyncTask;

import com.nathb.swiftshop.model.ShoppingList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CreateShoppingListTask extends RealmTask {

    private static final int MAX_SHOPPING_LISTS = 10;

    public interface Callback {
        void onCreated(String id);
    }

    private ShoppingList shoppingList;
    private Callback callback;

    public CreateShoppingListTask(ShoppingList shoppingList, Callback callback) {
        this.shoppingList = shoppingList;
        this.callback = callback;
    }

    @Override
    protected Realm.Transaction createTransaction() {
        return new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(shoppingList);
                deleteOldShoppingLists(realm);
            }
        };
    }

    @Override
    protected void onComplete() {
        callback.onCreated(shoppingList.getId());
    }

    private void deleteOldShoppingLists(Realm realm) {
        RealmResults<ShoppingList> shoppingLists = realm.where(ShoppingList.class)
                .findAllSorted("timestamp", Sort.DESCENDING);

        if (shoppingLists.size() > MAX_SHOPPING_LISTS) {
            ShoppingList firstToDelete = shoppingLists.get(MAX_SHOPPING_LISTS);

            RealmResults<ShoppingList> toDelete = realm.where(ShoppingList.class)
                    .lessThanOrEqualTo("timestamp", firstToDelete.getTimestamp()).findAll();
            toDelete.deleteAllFromRealm();
        }
    }
}
