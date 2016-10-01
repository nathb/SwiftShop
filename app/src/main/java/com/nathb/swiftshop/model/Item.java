package com.nathb.swiftshop.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject implements Comparable<Item> {

    @PrimaryKey private String id;
    private String name;
    private Category category;

    public Item() { }

    public Item(String name, Category category) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public int compareTo(Item o) {
        if (o == null) {
            return 1;
        }

        // Order first by category order
        Integer a = this.getCategory().getOrder();
        Integer b = o.getCategory().getOrder();
        int orderComparison = a.compareTo(b);
        if (orderComparison != 0) {
            return orderComparison;
        }

        // Then order alphabetically
        return this.name.compareTo(o.name);
    }
}
