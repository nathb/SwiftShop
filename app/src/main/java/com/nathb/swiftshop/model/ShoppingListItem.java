package com.nathb.swiftshop.model;

import java.util.UUID;

import io.realm.RealmObject;

public class ShoppingListItem extends RealmObject implements Comparable<ShoppingListItem> {

    private String id;
    private Item item;
    private boolean isChecked = false;

    public ShoppingListItem() { }

    public ShoppingListItem(Item item) {
        this.id = UUID.randomUUID().toString();
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return item.getName();
    }

    public Item getItem() {
        return item;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int compareTo(ShoppingListItem o) {
        if (o == null) {
            return 1;
        }

        // Order by checked status
        Boolean a = Boolean.valueOf(this.isChecked());
        Boolean b = Boolean.valueOf(o.isChecked());
        int isCheckedComparison = a.compareTo(b);
        if (isCheckedComparison != 0) {
            return isCheckedComparison;
        }

        // Then order alphabetically
        return this.getName().compareTo(o.getName());
    }
}
