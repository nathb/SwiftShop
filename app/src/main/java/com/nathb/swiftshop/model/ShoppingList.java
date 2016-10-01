package com.nathb.swiftshop.model;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ShoppingList extends RealmObject {

    @PrimaryKey private String id;
    private RealmList<ShoppingListItem> slis;
    private Date timestamp;

    public ShoppingList() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = new Date();
    }

    public String getId() {
        return id;
    }

    public RealmList<ShoppingListItem> getItems() {
        return slis;
    }

    public Date getTimestamp() {
        return timestamp;
    }

}
