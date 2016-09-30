package com.nathb.swiftshop.task;


import com.nathb.swiftshop.model.Item;
import com.nathb.swiftshop.model.ShoppingListItem;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DeleteItemTask extends RealmTask {

    private String itemId;
    private String itemName;

    public DeleteItemTask(Item item) {
        this.itemId = item.getId();
    }

    public DeleteItemTask(String itemName) {
        this.itemName = itemName;
    }

    @Override
    protected Realm.Transaction createTransaction() {
        return new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery<Item> itemQuery = realm.where(Item.class);
                if (itemId != null) {
                    itemQuery.equalTo("id", itemId);
                } else {
                    itemQuery.equalTo("name", itemName);
                }

                Item item = itemQuery.findFirst();
                RealmResults<ShoppingListItem> slis = realm.where(ShoppingListItem.class)
                                                            .equalTo("item.id", item.getId())
                                                            .findAll();
                item.deleteFromRealm();
                slis.deleteAllFromRealm();
            }
        };
    }
}
