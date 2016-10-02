package com.nathb.swiftshop.task;


import com.nathb.swiftshop.model.ShoppingList;

import io.realm.Realm;

public class DeleteShoppingListTask extends RealmTask {

    private String shoppingListId;

    public DeleteShoppingListTask(ShoppingList shoppingList) {
        this.shoppingListId = shoppingList.getId();
    }

    @Override
    protected Realm.Transaction createTransaction() {
        return new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(ShoppingList.class)
                        .equalTo("id", shoppingListId)
                        .findFirst()
                        .deleteFromRealm();
            }
        };
    }
}
