package com.nathb.swiftshop.task;

import com.nathb.swiftshop.model.ShoppingListItem;

import io.realm.Realm;

public class ToggleCheckStatusTask extends RealmTask {

    private String shoppingItemId;

    public ToggleCheckStatusTask(ShoppingListItem sli) {
        this.shoppingItemId = sli.getId();
    }

    @Override
    protected Realm.Transaction createTransaction() {
        return new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ShoppingListItem sli = realm.where(ShoppingListItem.class)
                                                .equalTo("id", shoppingItemId)
                                                .findFirst();
                sli.setChecked(!sli.isChecked());
            }
        };
    }

}
