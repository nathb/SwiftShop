package com.nathb.swiftshop.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ShoppingListItem extends RealmObject implements Comparable<ShoppingListItem> {

    @PrimaryKey private String id;
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

        // First order by checked status
        Boolean aChecked = Boolean.valueOf(this.isChecked());
        Boolean bChecked = Boolean.valueOf(o.isChecked());
        int isCheckedComparison = aChecked.compareTo(bChecked);
        if (isCheckedComparison != 0) {
            return isCheckedComparison;
        }

        // Then order by category
        Integer aOrder = this.getItem().getCategory().getOrder();
        Integer bOrder = o.getItem().getCategory().getOrder();
        int orderComparison = aOrder.compareTo(bOrder);
        if (orderComparison != 0) {
            return orderComparison;
        }

        // Then order alphabetically
        return this.getName().compareTo(o.getName());
    }
}
