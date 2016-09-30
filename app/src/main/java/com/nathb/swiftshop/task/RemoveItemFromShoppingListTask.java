package com.nathb.swiftshop.task;

import com.nathb.swiftshop.model.ShoppingListItem;

import io.realm.Realm;

public class RemoveItemFromShoppingListTask extends RealmTask {

    private String shoppingListItemId;

    public RemoveItemFromShoppingListTask(ShoppingListItem sli) {
        this.shoppingListItemId = sli.getId();
    }

    @Override
    protected Realm.Transaction createTransaction() {
        return new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ShoppingListItem sli = realm.where(ShoppingListItem.class)
                        .equalTo("id", shoppingListItemId)
                        .findFirst();
                sli.deleteFromRealm();
            }
        };
    }

}
