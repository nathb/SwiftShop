package com.nathb.swiftshop.task;

import com.nathb.swiftshop.model.Item;
import com.nathb.swiftshop.model.ShoppingList;
import com.nathb.swiftshop.model.ShoppingListItem;

import io.realm.Realm;

public class CreateItemAndAddToShoppingListTask extends RealmTask {

    private String shoppingListId;
    private String itemName;

    public CreateItemAndAddToShoppingListTask(ShoppingList shoppingList, String itemName) {
        this.shoppingListId = shoppingList.getId();
        this.itemName = itemName;
    }

    @Override
    protected Realm.Transaction createTransaction() {
        return new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item item = new Item(itemName);
                item = realm.copyToRealm(item);
                ShoppingList shoppingList = realm.where(ShoppingList.class)
                        .equalTo("id", shoppingListId)
                        .findFirst();
                shoppingList.getItems().add(new ShoppingListItem(item));
            }
        };
    }

}
