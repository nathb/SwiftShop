package com.nathb.swiftshop.task;

import com.nathb.swiftshop.model.Item;
import com.nathb.swiftshop.model.ShoppingList;
import com.nathb.swiftshop.model.ShoppingListItem;

import io.realm.Realm;

public class AddItemToShoppingListTask extends RealmTask {

    private String shoppingListId;
    private String itemId;

    public AddItemToShoppingListTask(ShoppingList shoppingList, String itemId) {
        this.shoppingListId = shoppingList.getId();
        this.itemId = itemId;
    }

    @Override
    protected Realm.Transaction createTransaction() {
        return new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ShoppingList shoppingList = realm.where(ShoppingList.class)
                        .equalTo("id", shoppingListId)
                        .findFirst();
                Item item = realm.where(Item.class)
                        .equalTo("id", itemId)
                        .findFirst();
                shoppingList.getItems().add(new ShoppingListItem(item));
            }
        };
    }

}
